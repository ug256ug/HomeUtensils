package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HomeUtensilsPackage(
    val displayName: String,
    val activationAmountUgx: Long,
    val durationDays: Int,
    val welcomeBonusUgx: Long,
    val initialTasksCount: Int,
    val taskRewardUgx: Long,
    val reactivationAmountUgx: Long,
    val reactivationDurationDays: Int,
    val reactivationInitialTasks: Int,
    val reactivationTaskRewardUgx: Long,
    val paidCapitalUgx: Long // Paid Capital in financial details
) {
    STARTER(
        displayName = "STARTER PACKAGE",
        activationAmountUgx = 5000L,
        durationDays = 60,
        welcomeBonusUgx = 500L,
        initialTasksCount = 3,
        taskRewardUgx = 500L,
        reactivationAmountUgx = 5000L,
        reactivationDurationDays = 60,
        reactivationInitialTasks = 4,
        reactivationTaskRewardUgx = 500L,
        paidCapitalUgx = 5000L
    ),
    STANDARD(
        displayName = "STANDARD PACKAGE",
        activationAmountUgx = 10000L,
        durationDays = 60,
        welcomeBonusUgx = 1000L,
        initialTasksCount = 5,
        taskRewardUgx = 500L,
        reactivationAmountUgx = 10000L,
        reactivationDurationDays = 60,
        reactivationInitialTasks = 6,
        reactivationTaskRewardUgx = 500L,
        paidCapitalUgx = 10000L
    ),
    PLUS(
        displayName = "PLUS PACKAGE",
        activationAmountUgx = 20000L,
        durationDays = 90,
        welcomeBonusUgx = 2000L,
        initialTasksCount = 6,
        taskRewardUgx = 1000L,
        reactivationAmountUgx = 20000L,
        reactivationDurationDays = 90,
        reactivationInitialTasks = 7,
        reactivationTaskRewardUgx = 1000L,
        paidCapitalUgx = 20000L
    );

    val activationCommissionUgx: Long
        get() = (activationAmountUgx * 0.10f).toLong()

    companion object {
        fun fromName(name: String?): HomeUtensilsPackage {
            val upper = name?.trim()?.uppercase() ?: ""
            return when {
                upper.contains("PLUS") -> PLUS
                upper.contains("STARTER") -> STARTER
                else -> STANDARD
            }
        }
    }
}

@Entity(tableName = "activation_cycles")
data class ActivationCycleEntity(
    @PrimaryKey val id: String, // CYCLE-xxx
    val userId: String = "current_user",
    val memberId: String,
    val packageTier: String, // STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE
    val cycleType: String, // FIRST_ACTIVATION, REACTIVATION, UPGRADE
    val amountUgx: Long,
    val initialTasksAllowed: Int,
    val activatedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long,
    val status: String = "ACTIVE", // ACTIVE, EXPIRED, SUPERSEDED
    val facilitatedByAgentId: String? = null
)

@Entity(tableName = "utensil_tasks")
data class UtensilTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val rewardUgx: Long,
    val description: String,
    val instructions: String,
    val imageResName: String,
    val status: String = "ACTIVE", // ACTIVE, PAUSED, COMPLETED
    val questionPrompt: String,
    val optionsString: String = "YES,NO", // Comma-separated options
    val correctAnswer: String = "YES", // Expected correct answer ("YES" or "NO")
    val estimatedMinutes: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "current_user",
    val memberId: String = "HU7K4M92Q",
    val fullName: String,
    val phoneNumber: String,
    val region: String = "Central", // Central, Eastern, Northern, Western
    val district: String = "Kampala Central",
    val referralCode: String,
    val inviterId: String = "",
    val packageName: String = "STANDARD PACKAGE", // STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE
    val paidCapitalUgx: Long = 10000L,
    val status: String = "ACTIVE", // ACTIVE, PENDING
    val walletBalanceUgx: Long = 18500L, // Withdrawable Balance
    val pendingBalanceUgx: Long = 0L,
    val totalEarnedUgx: Long = 32000L,
    val hasReceivedWelcomeBonus: Boolean = true,
    val isPackageActivated: Boolean = true,
    val packageActivatedAt: Long = System.currentTimeMillis() - 13 * 86400 * 1000L,
    val packageExpiresAt: Long = System.currentTimeMillis() + 47 * 86400 * 1000L,
    val isPackageExpired: Boolean = false,
    val currentCycleId: String = "CYCLE-INIT-01",
    val role: String = "USER", // USER
    val transactionPin: String = "1234" // 4-digit transaction security PIN
)

@Entity(tableName = "task_submissions")
data class TaskSubmissionEntity(
    @PrimaryKey val id: String,
    val taskId: String,
    val taskTitle: String,
    val rewardUgx: Long,
    val selectedAnswer: String,
    val notes: String = "",
    val status: String = "APPROVED", // APPROVED, REJECTED
    val isCorrect: Boolean = false,
    val imageResName: String = "",
    val questionPrompt: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long? = null,
    val feedback: String? = null
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // TASK_REWARD, REFERRAL_COMMISSION, WELCOME_BONUS, WITHDRAWAL, P2P_TRANSFER, AGENT_FACILITATION
    val amountUgx: Long,
    val status: String = "CREDITED", // CREDITED, PENDING, CANCELLED, REJECTED
    val eventKey: String = "", // Idempotency key to prevent duplicates
    val timestamp: Long = System.currentTimeMillis(),
    val feeUgx: Long = 0L,
    val netAmountUgx: Long = 0L,
    val referenceNumber: String = "",
    val counterpartyName: String = "",
    val counterpartyId: String = "",
    val runningBalanceUgx: Long = 0L,
    val paymentMethod: String = "",
    val notes: String = ""
)

@Entity(tableName = "referrals")
data class ReferralEntity(
    @PrimaryKey val id: String,
    val friendName: String,
    val friendPhone: String,
    val district: String,
    val status: String = "ACTIVE", // ACTIVE, PENDING
    val bonusUgx: Long = 1000L,
    val referralPackage: String = "STANDARD PACKAGE",
    val isCommissionCredited: Boolean = true,
    val joinedDate: String
)

@Entity(tableName = "p2p_transfers")
data class P2PTransferEntity(
    @PrimaryKey val id: String, // TXN-P2P-xxx
    val senderUserId: String = "current_user",
    val senderMemberId: String,
    val senderName: String,
    val recipientMemberId: String,
    val recipientName: String,
    val amountUgx: Long, // Transfer amount (exact amount received by recipient)
    val feeUgx: Long, // 10% fee charged ON TOP
    val totalDeductedUgx: Long, // amountUgx + feeUgx deducted from sender
    val netRecipientAmountUgx: Long, // exact recipient amount (= amountUgx)
    val isActivationFunding: Boolean = false,
    val targetPackage: String = "", // STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE
    val status: String = "CONFIRMED", // PENDING, CONFIRMED, FAILED, CANCELLED, DISPUTED
    val transactionPinVerified: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "activation_funding_targets")
data class ActivationFundingTargetEntity(
    @PrimaryKey val id: String, // TGT-xxx
    val memberId: String,
    val targetPackageName: String, // STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE
    val targetAmountUgx: Long,
    val contributedAmountUgx: Long = 0L,
    val contributorsCount: Int = 0,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "agent_facilitations")
data class AgentFacilitationEntity(
    @PrimaryKey val id: String, // AGT-FAC-xxx
    val agentId: String,
    val recipientMemberId: String,
    val recipientName: String,
    val packageName: String,
    val amountUgx: Long,
    val feeUgx: Long = 0L, // 0% normal P2P fee for agent package facilitation
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_working_float")
data class AgentWorkingFloatEntity(
    @PrimaryKey val agentId: String = "AGT-CENTRAL-001",
    val agentName: String = "Ronald Mukasa",
    val memberId: String = "HU7K4M92Q",
    val territory: String = "Central Region (Kampala & Wakiso)",
    val region: String = "Central",
    val isAvailable: Boolean = true, // Availability: Available / Unavailable
    val workingFloatUgx: Long = 100000L, // Agent working float: UGX 100,000 (Operational - Non-Personal)
    val totalFacilitatedUgx: Long = 35000L,
    val commissionEarnedUgx: Long = 14500L,
    val status: String = "ACTIVE", // ACTIVE, SUSPENDED, DEACTIVATED
    val photoUri: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
    val phone: String = "+256 772 458 928",
    val supportedPackages: String = "STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE",
    val unresolvedCasesCount: Int = 0,
    val isRestrictedFromNewRequests: Boolean = false,
    val restrictionReason: String = "",
    val whatsappNumber: String = "+256 772 458 928",
    val officialPaymentMethod: String = "MTN Mobile Money",
    val officialPaymentAccount: String = "0772458928",
    val officialBeneficiaryName: String = "Ronald Mukasa"
)

@Entity(tableName = "agent_applications")
data class AgentApplicationEntity(
    @PrimaryKey val id: String, // AGT-APP-xxx
    val memberId: String,
    val fullName: String,
    val phoneNumber: String,
    val region: String, // Central, Eastern, Northern, Western
    val idType: String, // National ID/NIN, Passport, Driving Licence
    val idNumber: String,
    val idDocumentUri: String, // Document / photo scan
    val profilePhotoUri: String = "", // Optional profile photo
    val applicationReason: String,
    val agreedToResponsibilities: Boolean = true,
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED, SUSPENDED, DEACTIVATED
    val submittedAt: Long = System.currentTimeMillis(),
    val reviewedByAdmin: String? = null,
    val reviewedAt: Long? = null,
    val reviewNotes: String? = null
)

@Entity(tableName = "agent_commission_logs")
data class AgentCommissionLogEntity(
    @PrimaryKey val id: String, // COMM-xxx
    val agentId: String,
    val memberId: String,
    val cycleId: String,
    val taskId: String,
    val taskTitle: String,
    val taskRewardUgx: Long,
    val commissionRate: Double = 0.02, // 2% Agent commission
    val commissionUgx: Long, // 2% of qualifying task income
    val timestamp: Long = System.currentTimeMillis()
)

fun maskDestination(destination: String): String {
    val clean = destination.trim()
    if (clean.length <= 4) return clean
    return if (clean.startsWith("+256")) {
        val digits = clean.filter { it.isDigit() }
        val suffix = digits.takeLast(3)
        "+256 77••••$suffix"
    } else if (clean.startsWith("07") || clean.startsWith("03")) {
        val suffix = clean.takeLast(3)
        "07•••••$suffix"
    } else if (clean.startsWith("0x", ignoreCase = true)) {
        val start = clean.take(6)
        val end = clean.takeLast(4)
        "$start••••$end"
    } else if (clean.length >= 8) {
        val start = clean.take(2)
        val end = clean.takeLast(3)
        "$start•••••$end"
    } else {
        "••••${clean.takeLast(3)}"
    }
}

fun maskBeneficiaryName(name: String): String {
    val trimmed = name.trim()
    val parts = trimmed.split(" ")
    return if (parts.size >= 2) {
        val first = parts[0]
        val second = parts[1]
        val maskedSecond = if (second.isNotEmpty()) "${second.first()}••••" else "••••"
        "$first $maskedSecond"
    } else if (trimmed.length > 2) {
        "${trimmed.take(2)}••••"
    } else {
        trimmed
    }
}

fun maskAccount(account: String): String = maskDestination(account)

fun maskAgentId(id: String): String {
    val clean = id.trim()
    if (clean.length <= 4) return clean
    return if (clean.startsWith("AGT-", ignoreCase = true)) {
        val last3 = clean.takeLast(3)
        "AG•••$last3"
    } else {
        "AG•••${clean.takeLast(3)}"
    }
}

fun maskMemberId(id: String): String {
    val clean = id.trim()
    if (clean.length <= 4) return clean
    return if (clean.length >= 7) {
        val first3 = clean.take(3)
        val last3 = clean.takeLast(3)
        "$first3•••$last3"
    } else {
        val first2 = clean.take(2)
        val last2 = clean.takeLast(2)
        "$first2•••$last2"
    }
}

data class LiveActivityEvent(
    val id: String,
    val memberId: String,
    val maskedMember: String,
    val eventType: String, // "PACKAGE_ACTIVATION", "PACKAGE_UPGRADE", "PACKAGE_REACTIVATION", "TASK_REWARD", "P2P_TRANSFER", "REFERRAL_COMMISSION", "WITHDRAWAL", "AGENT_FACILITATION", "FLOAT_BONUS"
    val title: String,
    val detail: String,
    val timestamp: Long,
    val iconType: String = "VERIFIED"
)

@Entity(tableName = "agent_activation_requests")
data class AgentActivationRequestEntity(
    @PrimaryKey val id: String, // APR-xxx
    val memberId: String,
    val memberName: String,
    val memberPhone: String = "",
    val agentId: String,
    val agentName: String,
    val agentPhotoUri: String = "",
    val packageName: String, // STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE
    val amountUgx: Long,
    val region: String,
    val status: String = "Requested", // Requested, Agent Accepted, Awaiting Member Payment, Payment Reported, Awaiting Agent Confirmation, Confirmed, Activation Completed, Payment Disputed, Admin Review, Cancelled, Resolved, Agent Response Overdue
    val createdAt: Long = System.currentTimeMillis(),
    val agentAcceptedAt: Long? = null,
    val paymentReportedAt: Long? = null,
    val responseDeadline: Long? = null,
    val paymentMethod: String = "",
    val paymentReference: String = "",
    val paymentProofUri: String = "",
    val paymentProofUploadedAt: Long? = null,
    val paymentProofUploader: String = "",
    val memberNote: String = "",
    val agentStatement: String = "",
    val adminInvestigationNotes: String = "",
    val activationCycleId: String? = null,
    val isResolvedByAdmin: Boolean = false,
    val resolvedAt: Long? = null,
    val resolutionOutcome: String = "",
    val isDisputed: Boolean = false,
    val agentPhone: String = "+256 772 458 928",
    val agentWhatsapp: String = "+256 772 458 928",
    val officialPaymentMethod: String = "MTN Mobile Money",
    val officialPaymentAccount: String = "0772458928",
    val officialBeneficiaryName: String = "Ronald Mukasa"
)

@Entity(tableName = "agent_payment_protection_cases")
data class AgentPaymentProtectionCaseEntity(
    @PrimaryKey val id: String, // CASE-PROT-xxx
    val requestId: String,
    val memberId: String,
    val memberName: String = "",
    val agentId: String,
    val agentName: String = "",
    val amountUgx: Long,
    val reportedAt: Long = System.currentTimeMillis(),
    val reason: String, // e.g. "Agent not responding", "Payment Disputed by Agent", "Payment Verification Claim"
    val status: String = "OPEN", // OPEN, AWAITING_AGENT, ADMIN_REVIEW, RESOLVED, CANCELLED
    val memberStatement: String = "",
    val agentStatement: String = "",
    val adminNotes: String = "",
    val resolutionSummary: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_payment_config")
data class AgentPaymentConfigEntity(
    @PrimaryKey val id: String = "global_config",
    val responseTimeoutMinutes: Int = 60, // Admin configurable deadline in minutes
    val maxUnresolvedCasesThreshold: Int = 2, // Admin configurable threshold
    val autoRestrictNewAssignments: Boolean = true
)

@Entity(tableName = "agent_photo_audits")
data class AgentPhotoAuditEntity(
    @PrimaryKey val id: String, // AUDIT-PHOTO-xxx
    val agentId: String,
    val agentName: String = "",
    val previousPhotoUri: String = "",
    val newPhotoUri: String = "",
    val changedBy: String, // "AGENT", "ADMIN"
    val action: String, // "UPLOAD", "REPLACE", "APPROVE", "REMOVE", "INVESTIGATE"
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "payment_proof_access_logs")
data class PaymentProofAccessLogEntity(
    @PrimaryKey val id: String, // LOG-ACCESS-xxx
    val caseOrRequestId: String,
    val accessedBy: String,
    val userRole: String, // "MEMBER", "AGENT", "ADMIN"
    val action: String = "VIEW", // "VIEW", "DOWNLOAD", "VERIFY"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawal_requests")
data class WithdrawalRequestEntity(
    @PrimaryKey val id: String, // WTH-xxx
    val userId: String = "current_user",
    val memberId: String,
    val memberName: String,
    val requestedAmountUgx: Long,
    val feeUgx: Long, // 10% fee deducted from requested amount
    val netAmountUgx: Long, // 90% member receives
    val payoutMethodId: String,
    val payoutMethodName: String,
    val beneficiaryAccount: String,
    val beneficiaryName: String,
    val status: String = "PENDING", // PENDING, PROCESSING, COMPLETED, REJECTED, DISPUTED
    val processedByAdmin: String? = null,
    val processedAt: Long? = null,
    val rejectionReason: String? = null,
    val externalRef: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawal_methods")
data class WithdrawalMethodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val supportedCountries: String,
    val currency: String,
    val requiredAccountInfo: String,
    val instructions: String,
    val isActive: Boolean = true,
    val minimumAmountUgx: Long = 2000L,
    val isDefault: Boolean = false
)

@Entity(tableName = "beneficiary_details")
data class BeneficiaryDetailsEntity(
    @PrimaryKey val userId: String = "current_user",
    val memberId: String = "HU7K4M92Q",
    val beneficiaryName: String = "Sarah Nalubega",
    val accountIdentifier: String = "+256 772 458 921",
    val methodId: String = "METHOD-MOMO",
    val methodName: String = "MTN Mobile Money Uganda",
    val binanceAddressType: String = "ID", // "ID" or "WALLET"
    val network: String = "TRC20", // TRC20, BEP20
    val assetToken: String = "USDT",
    val bankName: String = "",
    val isVerified: Boolean = true,
    val verifiedAt: Long = System.currentTimeMillis() - 30 * 86400 * 1000L,
    val changeLockReason: String = "Verified / Locked"
)

@Entity(tableName = "beneficiary_audit_logs")
data class BeneficiaryAuditLogEntity(
    @PrimaryKey val id: String, // AUDIT-xxx
    val memberId: String,
    val oldDetails: String,
    val newDetails: String,
    val changedBy: String,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "uganda_holidays")
data class UgandaHolidayEntity(
    @PrimaryKey val id: String, // e.g. HOL-2026-IND-01 or HOL-UUID
    val name: String, // e.g. "Independence Day", "Eid al-Fitr"
    val dateIso: String, // "YYYY-MM-DD" e.g. "2026-10-09"
    val isOfficialPublicHoliday: Boolean = true,
    val isSpecialDeclared: Boolean = false,
    val description: String = "Official Uganda Public Holiday",
    val year: Int = 2026,
    val addedBy: String = "SYSTEM",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "holiday_audit_logs")
data class UgandaHolidayAuditLogEntity(
    @PrimaryKey val id: String,
    val holidayId: String,
    val holidayName: String,
    val dateIso: String,
    val action: String, // "ADDED", "UPDATED", "DELETED"
    val performedBy: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

