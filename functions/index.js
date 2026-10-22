const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

initializeApp();

const db = getFirestore();

/**

* HomeUtensils backend health check.
* Requires the caller to be signed in.
* Does not read or change member financial records.
  */
  exports.healthCheck = onCall(async (request) => {
  if (!request.auth) {
  throw new HttpsError(
  "unauthenticated",
  "Please sign in to HomeUtensils first."
  );
  }

return {
ok: true,
service: "HomeUtensils",
message: "Trusted backend is running.",
};
});
