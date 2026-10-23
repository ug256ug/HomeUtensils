
const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue } = require("firebase-admin/firestore");

initializeApp();

const db = getFirestore();

/**
 * Confirms that the caller is signed in.
 */
function requireSignedIn(request) {
  if (!request.auth) {
    throw new HttpsError(
      "unauthenticated",
      "Please sign in to HomeUtensils first."
    );
  }

  return request.auth.uid;
}

/**
 * Confirms that the caller has an enabled administrator record.
 * Expected document: admins/{uid}
 */
async function requireAdmin(request) {
  const uid = requireSignedIn(request);
  const adminRef = db.collection("admins").doc(uid);
  const adminSnap = await adminRef.get();

  if (!adminSnap.exists) {
    throw new HttpsError(
      "permission-denied",
      "Administrator access is required."
    );
  }

  const admin = adminSnap.data();

  if (admin.enabled === false || admin.active === false) {
    throw new HttpsError(
      "permission-denied",
      "This administrator account is disabled."
    );
  }

  if (admin.role && admin.role !== "admin") {
    throw new HttpsError(
      "permission-denied",
      "Administrator access is required."
    );
  }

  return uid;
}

/**
 * Backend health check.
 * Does not read or change financial records.
 */
exports.healthCheck = onCall(async (request) => {
  requireSignedIn(request);

  return {
    ok: true,
    service: "HomeUtensils",
    message: "Trusted backend is running."
  };
});

/**
 * Allows an authenticated administrator to verify access
 * and write a basic server-generated audit event.
 *
 * This does not approve withdrawals, change balances,
 * or create P2P transfers.
 */
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
