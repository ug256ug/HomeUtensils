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
