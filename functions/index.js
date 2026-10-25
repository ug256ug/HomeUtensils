const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const {
  getFirestore,
  FieldValue,
  Timestamp
} = require("firebase-admin/firestore");

initializeApp();

const db = getFirestore();

function requireSignedIn(request) {
  if (!request.auth) {
    throw new HttpsError(
      "unauthenticated",
      "Please sign in to HomeUtensils first."
    );
  }

  return request.auth.uid;
}

async function requireAdmin(request) {
  const uid = requireSignedIn(request);
  const adminSnap = await db.collection("admins").doc(uid).get();

  if (
    !adminSnap.exists ||
    adminSnap.data().role !== "admin" ||
    adminSnap.data().enabled !== true
  ) {
    throw new HttpsError(
      "permission-denied",
      "Enabled administrator access is required."
    );
  }

  return uid;
}

exports.sendP2PFloat = onCall(async (request) => {
const senderUid = requireSignedIn(request);

const recipientMemberId = String(
request.data?.recipientMemberId || ""
).trim();

const amount = request.data?.amount;
const requestId = String(
request.data?.requestId || ""
).trim();

if (
!recipientMemberId ||
recipientMemberId.length > 100 ||
!Number.isSafeInteger(amount) ||
amount <= 0 ||
amount > 100000000 ||
!/^[A-Za-z0-9_-]{16,100}$/.test(requestId)
) {
throw new HttpsError(
"invalid-argument",
"Enter a valid recipient, whole-number UGX amount, and request ID."
);
}

const senderProfileRef = db
.collection("memberProfiles")
.doc(senderUid);

const recipientQuery = await db
.collection("memberProfiles")
.where("memberId", "==", recipientMemberId)
.limit(2)
.get();

if (recipientQuery.size !== 1) {
throw new HttpsError(
"not-found",
"The recipient was not found or the member ID is not unique."
);
}

const recipientUid = recipientQuery.docs[0].id;

if (senderUid === recipientUid) {
throw new HttpsError(
"invalid-argument",
"You cannot send P2P float to your own account."
);
}

const senderBalanceRef = db
.collection("memberP2PBalances")
.doc(senderUid);

const recipientBalanceRef = db
.collection("memberP2PBalances")
.doc(recipientUid);

const transferRef = db
.collection("p2pTransfers")
.doc(requestId);

await db.runTransaction(async (transaction) => {
const existingTransfer = await transaction.get(transferRef);

if (existingTransfer.exists) {
  const existing = existingTransfer.data();

  if (
    existing.senderUid === senderUid &&
    existing.recipientUid === recipientUid &&
    existing.amount === amount
  ) {
    return;
  }

  throw new HttpsError(
    "already-exists",
    "This request ID has already been used."
  );
}

const senderBalance = await transaction.get(senderBalanceRef);
const recipientBalance = await transaction.get(recipientBalanceRef);

if (!senderBalance.exists || !recipientBalance.exists) {
  throw new HttpsError(
    "failed-precondition",
    "Verified P2P balances are not set up for both members yet. No transfer was made."
  );
}

const senderData = senderBalance.data();
const recipientData = recipientBalance.data();

if (
  senderData.verified !== true ||
  recipientData.verified !== true ||
  senderData.currency !== "UGX" ||
  recipientData.currency !== "UGX" ||
  !Number.isSafeInteger(senderData.availableBalance) ||
  !Number.isSafeInteger(recipientData.availableBalance) ||
  senderData.availableBalance < 0 ||
  recipientData.availableBalance < 0
) {
  throw new HttpsError(
    "failed-precondition",
    "The member balances are not verified or are invalid."
  );
}

if (senderData.availableBalance < amount) {
  throw new HttpsError(
    "failed-precondition",
    "Your verified P2P balance is insufficient."
  );
}

if (
  !Number.isSafeInteger(
    recipientData.availableBalance + amount
  )
) {
  throw new HttpsError(
    "out-of-range",
    "The recipient balance would exceed the supported limit."
  );
}

transaction.update(senderBalanceRef, {
  availableBalance: senderData.availableBalance - amount,
  updatedAt: FieldValue.serverTimestamp()
});

transaction.update(recipientBalanceRef, {
  availableBalance: recipientData.availableBalance + amount,
  updatedAt: FieldValue.serverTimestamp()
});

transaction.create(transferRef, {
  senderUid,
  recipientUid,
  recipientMemberId,
  amount,
  currency: "UGX",
  status: "completed",
  requestId,
  createdAt: FieldValue.serverTimestamp(),
  createdBy: "trusted_backend"
});

transaction.create(
  db.collection("walletTransactions")
    .doc("p2p_" + requestId + "_debit"),
  {
    uid: senderUid,
    type: "p2p_debit",
    amount,
    currency: "UGX",
    transferId: requestId,
    createdAt: FieldValue.serverTimestamp()
  }
);

transaction.create(
  db.collection("walletTransactions")
    .doc("p2p_" + requestId + "_credit"),
  {
    uid: recipientUid,
    type: "p2p_credit",
    amount,
    currency: "UGX",
    transferId: requestId,
    createdAt: FieldValue.serverTimestamp()
  }
);

});

return {
ok: true,
message: "P2P transfer processed or already completed.",
transferId: requestId
};
});
exports.requestManualWithdrawal = onCall(async (request) => {
const uid = requireSignedIn(request);

const amount = request.data?.amount;
const method = String(request.data?.method || "").trim();
const payoutDetails = String(
request.data?.payoutDetails || ""
).trim();

const allowedMethods = [
"Mobile Money",
"Bank Transfer"
];

if (
!Number.isSafeInteger(amount) ||
amount < 2000 ||
amount > 100000000 ||
!allowedMethods.includes(method) ||
payoutDetails.length < 3 ||
payoutDetails.length > 200
) {
throw new HttpsError(
"invalid-argument",
"Enter a valid whole-number amount of at least UGX 2,000, payout method, and payout details."
);
}

const fee = Math.ceil(amount * 0.10);
const totalDeduction = amount + fee;

const profileRef = db
.collection("memberProfiles")
.doc(uid);

const pendingQuery = db
.collection("withdrawalRequests")
.where("uid", "==", uid)
.where("status", "==", "pending");

const requestRef = db
.collection("withdrawalRequests")
.doc();

await db.runTransaction(async (transaction) => {
const profile = await transaction.get(profileRef);
const pending = await transaction.get(pendingQuery);

if (!profile.exists) {
  throw new HttpsError(
    "failed-precondition",
    "Your member profile was not found."
  );
}

if (pending.size > 0) {
  throw new HttpsError(
    "failed-precondition",
    "You already have a pending withdrawal request."
  );
}

transaction.create(requestRef, {
  uid,
  amount,
  fee,
  totalDeduction,
  method,
  payoutDetails,
  currency: "UGX",
  status: "pending",
  paymentStatus: "not_paid",
  reviewNote: "",
  createdAt: FieldValue.serverTimestamp(),
  updatedAt: FieldValue.serverTimestamp()
});

});

return {
ok: true,
requestId: requestRef.id,
status: "pending",
amount,
fee,
totalDeduction,
message: "Withdrawal request submitted for manual admin review. No balance has been deducted and no payment has been sent."
};
});
exports.healthCheck = onCall(async (request) => {
  requireSignedIn(request);

  return {
    ok: true,
    service: "HomeUtensils",
    message: "Trusted backend is running."
  };
});

exports.verifyAdminAccess = onCall(async (request) => {
  const uid = await requireAdmin(request);

  await db.collection("adminAuditLogs").add({
    actorUid: uid,
    action: "ADMIN_ACCESS_VERIFIED",
    targetType: "admin",
    targetId: uid,
    createdAt: FieldValue.serverTimestamp(),
    source: "trusted_backend"
  });

  return {
    ok: true,
    message: "Administrator access verified."
  };
});

/**
 * Approves or rejects a pending activation request.
 * Referral commissions are recorded only by this trusted backend.
 * This function does not credit a wallet or send money.
 */
exports.reviewActivationRequest = onCall(async (request) => {
  const adminUid = await requireAdmin(request);
  const requestId = request.data && request.data.requestId;
  const status = request.data && request.data.status;

  if (
    typeof requestId !== "string" ||
    requestId.length < 1 ||
    requestId.length > 150 ||
    requestId.includes("/")
  ) {
    throw new HttpsError(
      "invalid-argument",
      "A valid activation request ID is required."
    );
  }

  if (!["approved", "rejected"].includes(status)) {
    throw new HttpsError(
      "invalid-argument",
      "Status must be approved or rejected."
    );
  }

  const activationRef = db
    .collection("activationRequests")
    .doc(requestId);

  const packageDays = {
    Starter: 60,
    Standard: 60,
    Plus: 90
  };

  const commissionAmounts = {
    Starter: 500,
    Standard: 1000,
    Plus: 2000
  };

  const result = await db.runTransaction(async (transaction) => {
    const activationSnap = await transaction.get(activationRef);

    if (!activationSnap.exists) {
      throw new HttpsError(
        "not-found",
        "Activation request not found."
      );
    }

    const activation = activationSnap.data();

    if (activation.status !== "pending") {
      throw new HttpsError(
        "failed-precondition",
        "This activation request has already been reviewed."
      );
    }

    if (
      typeof activation.uid !== "string" ||
      !activation.uid
    ) {
      throw new HttpsError(
        "failed-precondition",
        "The activation request has no valid member ID."
      );
    }

    const profileRef = db
      .collection("memberProfiles")
      .doc(activation.uid);

    const profileSnap = await transaction.get(profileRef);

    if (!profileSnap.exists) {
      throw new HttpsError(
        "not-found",
        "Member profile not found."
      );
    }

    const profile = profileSnap.data();

    let commissionRef = null;
    let commissionAmount = 0;
    let validReferrer = null;
    let expiry = null;
    let activationDate = null;

    if (status === "approved") {
      const packageName = activation.packageRequested;

      if (
        !Object.prototype.hasOwnProperty.call(
          packageDays,
          packageName
        )
      ) {
        throw new HttpsError(
          "failed-precondition",
          "Unknown package. Request was not approved."
        );
      }

      activationDate = new Date();
      expiry = new Date(activationDate);

      expiry.setDate(
        expiry.getDate() + packageDays[packageName]
      );

      if (
        typeof profile.referredByUid === "string" &&
        typeof profile.referredByMemberId === "string" &&
        profile.referredByUid !== activation.uid
      ) {
        const sponsorRef = db
          .collection("memberProfiles")
          .doc(profile.referredByUid);

        const sponsorSnap = await transaction.get(sponsorRef);

        if (
          sponsorSnap.exists &&
          sponsorSnap.data().memberId ===
            profile.referredByMemberId
        ) {
          const possibleCommissionRef = db
            .collection("referralCommissions")
            .doc(requestId);

          const existingCommission = await transaction.get(
            possibleCommissionRef
          );

          if (!existingCommission.exists) {
            validReferrer = {
              uid: profile.referredByUid,
              memberId: profile.referredByMemberId
            };

            commissionRef = possibleCommissionRef;
            commissionAmount = commissionAmounts[packageName];
          }
        }
      }
    }

    // All transaction reads are complete before these writes.
    if (status === "approved") {
      transaction.update(profileRef, {
        package: activation.packageRequested,
        packageStatus: "active",
        activationDate: Timestamp.fromDate(activationDate),
        expiryDate: Timestamp.fromDate(expiry),
        paymentVerifiedByAdmin: true,
        paymentVerifiedAt: FieldValue.serverTimestamp(),
        paymentVerifiedBy: adminUid
      });
    }

    transaction.update(activationRef, {
      status: status,
      reviewedAt: FieldValue.serverTimestamp(),
      reviewedBy: adminUid
    });

    if (commissionRef && validReferrer && commissionAmount > 0) {
      transaction.create(commissionRef, {
        activationRequestId: requestId,
        referrerUid: validReferrer.uid,
        referrerMemberId: validReferrer.memberId,
        referredUid: activation.uid,
        packageName: activation.packageRequested,
        amount: commissionAmount,
        currency: "UGX",
        status: "pending_ledger_review",
        createdAt: FieldValue.serverTimestamp(),
        createdBy: "trusted_backend"
      });
    }

    return {
      memberUid: activation.uid,
      commissionRecorded: Boolean(commissionRef),
      commissionAmount: commissionRef ? commissionAmount : 0
    };
  });

  await db.collection("adminAuditLogs").add({
    actorUid: adminUid,
    action: status === "approved"
      ? "ACTIVATION_APPROVED_BY_BACKEND"
      : "ACTIVATION_REJECTED_BY_BACKEND",
    targetType: "activationRequest",
    targetId: requestId,
    memberUid: result.memberUid,
    commissionRecorded: result.commissionRecorded,
    commissionAmount: result.commissionAmount,
    currency: "UGX",
    createdAt: FieldValue.serverTimestamp(),
    source: "trusted_backend"
  });

  return {
    ok: true,
    status: status,
    commissionRecorded: result.commissionRecorded,
    commissionAmount: result.commissionAmount,
    currency: "UGX",
    message: status === "approved"
      ? "Activation approved. Any referral commission is recorded for ledger review; no wallet balance was credited."
      : "Activation request rejected. The member package was not activated."
  };
});

/**
 * Securely reviews a task submission and records a reward
 * only when an enabled administrator approves it.
 *
 * This records earnings in the trusted ledger.
 * It does not send money or directly pay a withdrawal.
 */
exports.reviewTaskSubmission = onCall(async (request) => {
  const adminUid = await requireAdmin(request);

  const submissionId =
    request.data && request.data.submissionId;
  const status = request.data && request.data.status;
  const feedback =
    typeof (request.data && request.data.feedback) === "string"
      ? request.data.feedback.trim().slice(0, 500)
      : "";

  if (
    typeof submissionId !== "string" ||
    !submissionId ||
    submissionId.length > 150 ||
    submissionId.includes("/")
  ) {
    throw new HttpsError(
      "invalid-argument",
      "A valid task submission ID is required."
    );
  }

  if (!["approved", "rejected"].includes(status)) {
    throw new HttpsError(
      "invalid-argument",
      "Status must be approved or rejected."
    );
  }

  const submissionRef = db
    .collection("taskSubmissions")
    .doc(submissionId);

  const result = await db.runTransaction(async (transaction) => {
    const submissionSnap = await transaction.get(submissionRef);

    if (!submissionSnap.exists) {
      throw new HttpsError(
        "not-found",
        "Task submission not found."
      );
    }

    const submission = submissionSnap.data();

    if (submission.status !== "pending") {
      throw new HttpsError(
        "failed-precondition",
        "This task has already been reviewed."
      );
    }

    if (
      typeof submission.uid !== "string" ||
      !submission.uid ||
      typeof submission.taskId !== "string" ||
      !submission.taskId
    ) {
      throw new HttpsError(
        "failed-precondition",
        "The submission is missing its member or task ID."
      );
    }

    const profileRef = db
      .collection("memberProfiles")
      .doc(submission.uid);

    const profileSnap = await transaction.get(profileRef);

    if (!profileSnap.exists) {
      throw new HttpsError(
        "failed-precondition",
        "Member profile not found."
      );
    }

    const profile = profileSnap.data();
    let reward = 0;
    let rewardRef = null;

    if (status === "approved") {
      if (profile.packageStatus !== "active") {
        throw new HttpsError(
          "failed-precondition",
          "The member's package is not active."
        );
      }

      const packageRules = {
        Starter: { limit: 3, reward: 500 },
        Standard: { limit: 5, reward: 500 },
        Plus: { limit: 6, reward: 1000 }
      };

      const rule = packageRules[profile.package];

      if (!rule) {
        throw new HttpsError(
          "failed-precondition",
          "The member's package is not recognised."
        );
      }

      const approvedQuery = db
        .collection("taskSubmissions")
        .where("uid", "==", submission.uid)
        .where("status", "==", "approved");

      const approvedSnap = await transaction.get(approvedQuery);

      if (approvedSnap.size >= rule.limit) {
        throw new HttpsError(
          "failed-precondition",
          "The initial approved-task limit for this package has been reached."
        );
      }

      // Stable reward ID prevents this submission being rewarded twice.
      rewardRef = db
        .collection("walletTransactions")
        .doc("task_reward_" + submissionId);

      const existingReward = await transaction.get(rewardRef);

      if (existingReward.exists) {
        throw new HttpsError(
          "already-exists",
          "A reward ledger entry already exists for this submission."
        );
      }

      reward = rule.reward;
    }

    transaction.update(submissionRef, {
      status,
      feedback,
      reviewedAt: FieldValue.serverTimestamp(),
      reviewedBy: adminUid
    });

    if (rewardRef && reward > 0) {
      transaction.create(rewardRef, {
        uid: submission.uid,
        type: "TASK_REWARD",
        taskId: submission.taskId,
        taskSubmissionId: submissionId,
        amount: reward,
        currency: "UGX",
        status: "recorded",
        createdAt: FieldValue.serverTimestamp(),
        createdBy: "trusted_backend"
      });
    }

    return {
      memberUid: submission.uid,
      reward,
      currency: "UGX"
    };
  });

  await db.collection("adminAuditLogs").add({
    actorUid: adminUid,
    action: status === "approved"
      ? "TASK_APPROVED_BY_BACKEND"
      : "TASK_REJECTED_BY_BACKEND",
    targetType: "taskSubmission",
    targetId: submissionId,
    memberUid: result.memberUid,
    reward: result.reward,
    currency: result.currency,
    createdAt: FieldValue.serverTimestamp(),
    source: "trusted_backend"
  });

  return {
    ok: true,
    status,
    reward: result.reward,
    currency: result.currency,
    message: status === "approved"
      ? "Task approved and reward recorded in the trusted ledger."
      : "Task rejected. No reward was recorded."
  };
});
