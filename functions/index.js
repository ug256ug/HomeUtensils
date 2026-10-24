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