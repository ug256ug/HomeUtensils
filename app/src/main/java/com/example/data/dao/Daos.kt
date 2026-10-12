package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.ActivationFundingTargetEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.AgentCommissionLogEntity
import com.example.data.model.AgentFacilitationEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.model.BeneficiaryAuditLogEntity
import com.example.data.model.BeneficiaryDetailsEntity
import com.example.data.model.P2PTransferEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentPaymentConfigEntity
import com.example.data.model.AgentPhotoAuditEntity
import com.example.data.model.PaymentProofAccessLogEntity
import com.example.data.model.UgandaHolidayEntity
import com.example.data.model.UgandaHolidayAuditLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UtensilTaskDao {
    @Query("SELECT * FROM utensil_tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<UtensilTaskEntity>>

    @Query("SELECT * FROM utensil_tasks WHERE id = :id")
    suspend fun getTaskById(id: String): UtensilTaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: UtensilTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<UtensilTaskEntity>)

    @Query("UPDATE utensil_tasks SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: String, status: String)

    @Query("DELETE FROM utensil_tasks WHERE id = :id")
    suspend fun deleteTask(id: String)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile ORDER BY packageActivatedAt DESC")
    fun getAllProfiles(): Flow<List<UserProfileEntity>>

    @Query("SELECT * FROM user_profile")
    suspend fun getAllProfilesOnce(): List<UserProfileEntity>

    @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
    suspend fun getProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<UserProfileEntity>)

    @Query("UPDATE user_profile SET status = :status WHERE id = :memberId OR memberId = :memberId")
    suspend fun updateMemberStatus(memberId: String, status: String)

    @Query("SELECT * FROM user_profile WHERE memberId = :memberId LIMIT 1")
    suspend fun getProfileByMemberId(memberId: String): UserProfileEntity?

    @Query("UPDATE user_profile SET role = :newRole WHERE id = 'current_user' OR memberId = :memberId")
    suspend fun updateRoleForMember(memberId: String, newRole: String)

    @Query("UPDATE user_profile SET role = :newRole WHERE id = 'current_user'")
    suspend fun updateRole(newRole: String)

    @Query("UPDATE user_profile SET district = :district WHERE id = 'current_user'")
    suspend fun updateDistrict(district: String)

    @Query("UPDATE user_profile SET walletBalanceUgx = walletBalanceUgx + :amount, totalEarnedUgx = totalEarnedUgx + :amount WHERE id = 'current_user'")
    suspend fun addCompletedEarnings(amount: Long)

    @Query("UPDATE user_profile SET walletBalanceUgx = CASE WHEN walletBalanceUgx >= :amount THEN walletBalanceUgx - :amount ELSE 0 END WHERE id = 'current_user'")
    suspend fun deductWalletBalance(amount: Long)

    @Query("UPDATE user_profile SET pendingBalanceUgx = pendingBalanceUgx + :amount WHERE id = 'current_user'")
    suspend fun addPendingReward(amount: Long)

    @Query("UPDATE user_profile SET pendingBalanceUgx = CASE WHEN pendingBalanceUgx >= :amount THEN pendingBalanceUgx - :amount ELSE 0 END, walletBalanceUgx = walletBalanceUgx + :amount, totalEarnedUgx = totalEarnedUgx + :amount WHERE id = 'current_user'")
    suspend fun approvePendingToWallet(amount: Long)

    @Query("UPDATE user_profile SET pendingBalanceUgx = CASE WHEN pendingBalanceUgx >= :amount THEN pendingBalanceUgx - :amount ELSE 0 END WHERE id = 'current_user'")
    suspend fun rejectPendingReward(amount: Long)

    @Query("UPDATE user_profile SET packageName = :packageName, paidCapitalUgx = :paidCapital, currentCycleId = :cycleId, packageActivatedAt = :activatedAt, packageExpiresAt = :expiresAt, isPackageExpired = :isExpired, isPackageActivated = 1 WHERE id = 'current_user'")
    suspend fun updateCycleDetails(
        packageName: String,
        paidCapital: Long,
        cycleId: String,
        activatedAt: Long,
        expiresAt: Long,
        isExpired: Boolean
    )

    @Query("UPDATE user_profile SET isPackageExpired = :isExpired WHERE id = 'current_user'")
    suspend fun updateExpiryState(isExpired: Boolean)

    @Query("UPDATE user_profile SET hasReceivedWelcomeBonus = 1 WHERE id = 'current_user'")
    suspend fun markWelcomeBonusReceived()

    @Query("UPDATE user_profile SET walletBalanceUgx = walletBalanceUgx + :amount WHERE id = 'current_user'")
    suspend fun refundWalletBalance(amount: Long)

    @Query("UPDATE user_profile SET transactionPin = :pin WHERE id = 'current_user'")
    suspend fun updateTransactionPin(pin: String)
}

@Dao
interface ActivationCycleDao {
    @Query("SELECT * FROM activation_cycles WHERE userId = :userId ORDER BY activatedAt DESC")
    fun getAllCycles(userId: String = "current_user"): Flow<List<ActivationCycleEntity>>

    @Query("SELECT * FROM activation_cycles WHERE userId = :userId AND status = 'ACTIVE' LIMIT 1")
    suspend fun getActiveCycle(userId: String = "current_user"): ActivationCycleEntity?

    @Query("SELECT * FROM activation_cycles WHERE id = :cycleId LIMIT 1")
    suspend fun getCycleById(cycleId: String): ActivationCycleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: ActivationCycleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycles(cycles: List<ActivationCycleEntity>)

    @Query("UPDATE activation_cycles SET status = :status WHERE id = :id")
    suspend fun updateCycleStatus(id: String, status: String)
}

@Dao
interface TaskSubmissionDao {
    @Query("SELECT * FROM task_submissions ORDER BY submittedAt DESC")
    fun getAllSubmissions(): Flow<List<TaskSubmissionEntity>>

    @Query("SELECT * FROM task_submissions WHERE taskId = :taskId LIMIT 1")
    suspend fun getSubmissionByTaskId(taskId: String): TaskSubmissionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: TaskSubmissionEntity)

    @Query("UPDATE task_submissions SET status = :status, reviewedAt = :reviewedAt, feedback = :feedback WHERE id = :id")
    suspend fun updateSubmissionStatus(id: String, status: String, reviewedAt: Long, feedback: String?)
}

@Dao
interface WalletTransactionDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<WalletTransactionEntity>>

    @Query("SELECT * FROM wallet_transactions WHERE eventKey = :eventKey LIMIT 1")
    suspend fun getTransactionByEventKey(eventKey: String): WalletTransactionEntity?

    @Query("SELECT * FROM wallet_transactions WHERE eventKey = :eventKey OR title LIKE '%' || :code || '%' LIMIT 1")
    suspend fun getTransactionForGiftCode(eventKey: String, code: String): WalletTransactionEntity?

    @Query("SELECT COUNT(*) FROM wallet_transactions WHERE type = 'WELCOME_BONUS'")
    suspend fun getWelcomeBonusCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<WalletTransactionEntity>)
}

@Dao
interface ReferralDao {
    @Query("SELECT * FROM referrals ORDER BY id DESC")
    fun getAllReferrals(): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE id = :id LIMIT 1")
    suspend fun getReferralById(id: String): ReferralEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: ReferralEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferrals(referrals: List<ReferralEntity>)
}

@Dao
interface P2PTransferDao {
    @Query("SELECT * FROM p2p_transfers ORDER BY timestamp DESC")
    fun getAllTransfers(): Flow<List<P2PTransferEntity>>

    @Query("SELECT * FROM p2p_transfers WHERE id = :id LIMIT 1")
    suspend fun getTransferById(id: String): P2PTransferEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransfer(transfer: P2PTransferEntity)

    @Query("UPDATE p2p_transfers SET status = :status WHERE id = :id")
    suspend fun updateTransferStatus(id: String, status: String)
}

@Dao
interface ActivationFundingTargetDao {
    @Query("SELECT * FROM activation_funding_targets WHERE memberId = :memberId AND isCompleted = 0 LIMIT 1")
    fun getActiveTargetForMember(memberId: String): Flow<ActivationFundingTargetEntity?>

    @Query("SELECT * FROM activation_funding_targets WHERE memberId = :memberId AND isCompleted = 0 LIMIT 1")
    suspend fun getActiveTargetOnce(memberId: String): ActivationFundingTargetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarget(target: ActivationFundingTargetEntity)

    @Query("UPDATE activation_funding_targets SET contributedAmountUgx = contributedAmountUgx + :amount, contributorsCount = contributorsCount + 1, isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :targetId")
    suspend fun addContribution(targetId: String, amount: Long, isCompleted: Boolean, completedAt: Long?)
}

@Dao
interface AgentFacilitationDao {
    @Query("SELECT * FROM agent_facilitations ORDER BY timestamp DESC")
    fun getAllFacilitations(): Flow<List<AgentFacilitationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFacilitation(facilitation: AgentFacilitationEntity)
}

@Dao
interface AgentWorkingFloatDao {
    @Query("SELECT * FROM agent_working_float WHERE agentId = :agentId LIMIT 1")
    fun getWorkingFloat(agentId: String = "AGT-CENTRAL-001"): Flow<AgentWorkingFloatEntity?>

    @Query("SELECT * FROM agent_working_float WHERE agentId = :agentId LIMIT 1")
    suspend fun getWorkingFloatOnce(agentId: String = "AGT-CENTRAL-001"): AgentWorkingFloatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkingFloat(floatEntity: AgentWorkingFloatEntity)

    @Query("UPDATE agent_working_float SET workingFloatUgx = CASE WHEN workingFloatUgx >= :amount THEN workingFloatUgx - :amount ELSE 0 END, totalFacilitatedUgx = totalFacilitatedUgx + :amount WHERE agentId = :agentId")
    suspend fun deductWorkingFloat(agentId: String, amount: Long)

    @Query("UPDATE agent_working_float SET workingFloatUgx = workingFloatUgx + :amount WHERE agentId = :agentId")
    suspend fun addWorkingFloat(agentId: String, amount: Long)

    @Query("SELECT * FROM agent_working_float")
    fun getAllWorkingFloats(): Flow<List<AgentWorkingFloatEntity>>

    @Query("UPDATE agent_working_float SET isAvailable = :isAvailable WHERE agentId = :agentId")
    suspend fun updateAvailability(agentId: String, isAvailable: Boolean)

    @Query("UPDATE agent_working_float SET commissionEarnedUgx = commissionEarnedUgx + :amount WHERE agentId = :agentId")
    suspend fun addCommission(agentId: String, amount: Long)

    @Query("UPDATE agent_working_float SET status = :status WHERE agentId = :agentId")
    suspend fun updateStatus(agentId: String, status: String)

    @Query("SELECT * FROM agent_working_float WHERE agentId = :agentId LIMIT 1")
    suspend fun getAgentById(agentId: String): AgentWorkingFloatEntity?

    @Query("UPDATE agent_working_float SET photoUri = :photoUri WHERE agentId = :agentId")
    suspend fun updatePhoto(agentId: String, photoUri: String)

    @Query("UPDATE agent_working_float SET isRestrictedFromNewRequests = :isRestricted, restrictionReason = :reason WHERE agentId = :agentId")
    suspend fun updateRestriction(agentId: String, isRestricted: Boolean, reason: String)

    @Query("UPDATE agent_working_float SET unresolvedCasesCount = :count WHERE agentId = :agentId")
    suspend fun updateUnresolvedCount(agentId: String, count: Int)

    @Query("UPDATE agent_working_float SET status = :status, isAvailable = :isAvailable WHERE agentId = :agentId")
    suspend fun updateAgentStatus(agentId: String, status: String, isAvailable: Boolean)
}

@Dao
interface WithdrawalRequestDao {
    @Query("SELECT * FROM withdrawal_requests ORDER BY createdAt DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawalRequestEntity>>

    @Query("SELECT * FROM withdrawal_requests WHERE userId = :userId ORDER BY createdAt DESC")
    fun getWithdrawalsForUser(userId: String = "current_user"): Flow<List<WithdrawalRequestEntity>>

    @Query("SELECT * FROM withdrawal_requests WHERE userId = :userId AND status IN ('PENDING', 'PROCESSING')")
    suspend fun getPendingWithdrawalsForUser(userId: String = "current_user"): List<WithdrawalRequestEntity>

    @Query("SELECT * FROM withdrawal_requests WHERE id = :id LIMIT 1")
    suspend fun getWithdrawalById(id: String): WithdrawalRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(request: WithdrawalRequestEntity)

    @Query("UPDATE withdrawal_requests SET status = :status, processedByAdmin = :admin, processedAt = :processedAt, rejectionReason = :rejectionReason, externalRef = :externalRef WHERE id = :id")
    suspend fun updateWithdrawalStatus(
        id: String,
        status: String,
        admin: String?,
        processedAt: Long?,
        rejectionReason: String?,
        externalRef: String?
    )
}

@Dao
interface WithdrawalMethodDao {
    @Query("SELECT * FROM withdrawal_methods ORDER BY isDefault DESC, name ASC")
    fun getAllMethods(): Flow<List<WithdrawalMethodEntity>>

    @Query("SELECT * FROM withdrawal_methods WHERE isActive = 1 ORDER BY isDefault DESC, name ASC")
    fun getActiveMethods(): Flow<List<WithdrawalMethodEntity>>

    @Query("SELECT * FROM withdrawal_methods ORDER BY isDefault DESC, name ASC")
    suspend fun getAllMethodsOnce(): List<WithdrawalMethodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(method: WithdrawalMethodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethods(methods: List<WithdrawalMethodEntity>)

    @Query("UPDATE withdrawal_methods SET isActive = :isActive WHERE id = :id")
    suspend fun updateMethodStatus(id: String, isActive: Boolean)

    @Update
    suspend fun updateMethod(method: WithdrawalMethodEntity)

    @Query("DELETE FROM withdrawal_methods WHERE id = :id")
    suspend fun deleteMethod(id: String)
}

@Dao
interface BeneficiaryDetailsDao {
    @Query("SELECT * FROM beneficiary_details WHERE userId = :userId LIMIT 1")
    fun getBeneficiary(userId: String = "current_user"): Flow<BeneficiaryDetailsEntity?>

    @Query("SELECT * FROM beneficiary_details WHERE userId = :userId LIMIT 1")
    suspend fun getBeneficiaryOnce(userId: String = "current_user"): BeneficiaryDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeneficiary(details: BeneficiaryDetailsEntity)

    @Query("UPDATE beneficiary_details SET accountIdentifier = :account, beneficiaryName = :name, methodId = :methodId, methodName = :methodName, isVerified = :isVerified, verifiedAt = :verifiedAt WHERE userId = :userId")
    suspend fun updateBeneficiary(
        userId: String,
        account: String,
        name: String,
        methodId: String,
        methodName: String,
        isVerified: Boolean,
        verifiedAt: Long
    )
}

@Dao
interface BeneficiaryAuditLogDao {
    @Query("SELECT * FROM beneficiary_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<BeneficiaryAuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: BeneficiaryAuditLogEntity)
}

@Dao
interface AgentApplicationDao {
    @Query("SELECT * FROM agent_applications ORDER BY submittedAt DESC")
    fun getAllApplications(): Flow<List<AgentApplicationEntity>>

    @Query("SELECT * FROM agent_applications WHERE memberId = :memberId ORDER BY submittedAt DESC LIMIT 1")
    fun getApplicationForMember(memberId: String): Flow<AgentApplicationEntity?>

    @Query("SELECT * FROM agent_applications WHERE memberId = :memberId ORDER BY submittedAt DESC LIMIT 1")
    suspend fun getApplicationForMemberOnce(memberId: String): AgentApplicationEntity?

    @Query("SELECT * FROM agent_applications WHERE id = :id LIMIT 1")
    suspend fun getApplicationById(id: String): AgentApplicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: AgentApplicationEntity)

    @Query("UPDATE agent_applications SET status = :status, reviewedByAdmin = :admin, reviewedAt = :reviewedAt, reviewNotes = :notes WHERE id = :id")
    suspend fun updateApplicationStatus(id: String, status: String, admin: String?, reviewedAt: Long?, notes: String?)
}

@Dao
interface AgentCommissionLogDao {
    @Query("SELECT * FROM agent_commission_logs WHERE agentId = :agentId ORDER BY timestamp DESC")
    fun getCommissionLogsForAgent(agentId: String): Flow<List<AgentCommissionLogEntity>>

    @Query("SELECT * FROM agent_commission_logs ORDER BY timestamp DESC")
    fun getAllCommissionLogs(): Flow<List<AgentCommissionLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommissionLog(log: AgentCommissionLogEntity)
}

@Dao
interface AgentActivationRequestDao {
    @Query("SELECT * FROM agent_activation_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<AgentActivationRequestEntity>>

    @Query("SELECT * FROM agent_activation_requests WHERE memberId = :memberId ORDER BY createdAt DESC")
    fun getRequestsForMember(memberId: String): Flow<List<AgentActivationRequestEntity>>

    @Query("SELECT * FROM agent_activation_requests WHERE agentId = :agentId ORDER BY createdAt DESC")
    fun getRequestsForAgent(agentId: String): Flow<List<AgentActivationRequestEntity>>

    @Query("SELECT * FROM agent_activation_requests WHERE id = :id LIMIT 1")
    fun getRequestById(id: String): Flow<AgentActivationRequestEntity?>

    @Query("SELECT * FROM agent_activation_requests WHERE id = :id LIMIT 1")
    suspend fun getRequestByIdOnce(id: String): AgentActivationRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: AgentActivationRequestEntity)

    @Update
    suspend fun updateRequest(request: AgentActivationRequestEntity)

    @Query("UPDATE agent_activation_requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE agent_activation_requests SET status = 'Payment Reported', paymentMethod = :method, paymentReference = :reference, memberNote = :note, paymentProofUri = :proofUri, paymentProofUploadedAt = :uploadedAt, paymentProofUploader = :uploader, paymentReportedAt = :reportedAt, responseDeadline = :deadline WHERE id = :id")
    suspend fun reportPayment(
        id: String,
        method: String,
        reference: String,
        note: String,
        proofUri: String,
        uploadedAt: Long,
        uploader: String,
        reportedAt: Long,
        deadline: Long
    )

    @Query("UPDATE agent_activation_requests SET status = :status, agentStatement = :statement, isDisputed = :isDisputed WHERE id = :id")
    suspend fun updateAgentResponse(id: String, status: String, statement: String, isDisputed: Boolean)

    @Query("UPDATE agent_activation_requests SET status = :status, adminInvestigationNotes = :notes, isResolvedByAdmin = :isResolved, resolvedAt = :resolvedAt, resolutionOutcome = :outcome WHERE id = :id")
    suspend fun updateAdminResolution(id: String, status: String, notes: String, isResolved: Boolean, resolvedAt: Long, outcome: String)

    @Query("UPDATE agent_activation_requests SET status = :status, activationCycleId = :cycleId WHERE id = :id")
    suspend fun completeActivation(id: String, status: String, cycleId: String)

    @Query("SELECT COUNT(*) FROM agent_activation_requests WHERE agentId = :agentId AND status IN ('Payment Reported', 'Awaiting Agent Confirmation', 'Payment Disputed', 'Admin Review', 'Agent Response Overdue')")
    suspend fun getUnresolvedCasesCountForAgent(agentId: String): Int
}

@Dao
interface AgentPaymentProtectionCaseDao {
    @Query("SELECT * FROM agent_payment_protection_cases ORDER BY reportedAt DESC")
    fun getAllCases(): Flow<List<AgentPaymentProtectionCaseEntity>>

    @Query("SELECT * FROM agent_payment_protection_cases WHERE memberId = :memberId ORDER BY reportedAt DESC")
    fun getCasesForMember(memberId: String): Flow<List<AgentPaymentProtectionCaseEntity>>

    @Query("SELECT * FROM agent_payment_protection_cases WHERE agentId = :agentId ORDER BY reportedAt DESC")
    fun getCasesForAgent(agentId: String): Flow<List<AgentPaymentProtectionCaseEntity>>

    @Query("SELECT * FROM agent_payment_protection_cases WHERE id = :id LIMIT 1")
    suspend fun getCaseByIdOnce(id: String): AgentPaymentProtectionCaseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(caseEntity: AgentPaymentProtectionCaseEntity)

    @Query("UPDATE agent_payment_protection_cases SET status = :status, adminNotes = :adminNotes, resolutionSummary = :resolution, updatedAt = :updatedAt WHERE id = :caseId")
    suspend fun updateCaseStatus(caseId: String, status: String, adminNotes: String, resolution: String, updatedAt: Long)
}

@Dao
interface AgentPaymentConfigDao {
    @Query("SELECT * FROM agent_payment_config WHERE id = 'global_config' LIMIT 1")
    fun getConfig(): Flow<AgentPaymentConfigEntity?>

    @Query("SELECT * FROM agent_payment_config WHERE id = 'global_config' LIMIT 1")
    suspend fun getConfigOnce(): AgentPaymentConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: AgentPaymentConfigEntity)

    @Query("UPDATE agent_payment_config SET responseTimeoutMinutes = :timeoutMinutes, maxUnresolvedCasesThreshold = :threshold WHERE id = 'global_config'")
    suspend fun updateConfig(timeoutMinutes: Int, threshold: Int)
}

@Dao
interface AgentPhotoAuditDao {
    @Query("SELECT * FROM agent_photo_audits ORDER BY timestamp DESC")
    fun getAllAudits(): Flow<List<AgentPhotoAuditEntity>>

    @Query("SELECT * FROM agent_photo_audits WHERE agentId = :agentId ORDER BY timestamp DESC")
    fun getAuditsForAgent(agentId: String): Flow<List<AgentPhotoAuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: AgentPhotoAuditEntity)
}

@Dao
interface PaymentProofAccessLogDao {
    @Query("SELECT * FROM payment_proof_access_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<PaymentProofAccessLogEntity>>

    @Query("SELECT * FROM payment_proof_access_logs WHERE caseOrRequestId = :requestId ORDER BY timestamp DESC")
    fun getLogsForRequest(requestId: String): Flow<List<PaymentProofAccessLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PaymentProofAccessLogEntity)
}

@Dao
interface UgandaHolidayDao {
    @Query("SELECT * FROM uganda_holidays ORDER BY dateIso ASC")
    fun getAllHolidays(): Flow<List<UgandaHolidayEntity>>

    @Query("SELECT * FROM uganda_holidays ORDER BY dateIso ASC")
    suspend fun getAllHolidaysOnce(): List<UgandaHolidayEntity>

    @Query("SELECT * FROM uganda_holidays WHERE dateIso = :dateIso AND isOfficialPublicHoliday = 1 LIMIT 1")
    suspend fun getHolidayForDate(dateIso: String): UgandaHolidayEntity?

    @Query("SELECT * FROM uganda_holidays WHERE id = :id LIMIT 1")
    suspend fun getHolidayById(id: String): UgandaHolidayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoliday(holiday: UgandaHolidayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolidays(holidays: List<UgandaHolidayEntity>)

    @Update
    suspend fun updateHoliday(holiday: UgandaHolidayEntity)

    @Query("DELETE FROM uganda_holidays WHERE id = :id")
    suspend fun deleteHoliday(id: String)
}

@Dao
interface UgandaHolidayAuditLogDao {
    @Query("SELECT * FROM holiday_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<UgandaHolidayAuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(log: UgandaHolidayAuditLogEntity)
}

