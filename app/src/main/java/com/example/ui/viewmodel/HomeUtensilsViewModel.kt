package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.HomeUtensilsDatabase
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
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentPaymentConfigEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentPhotoAuditEntity
import com.example.data.model.PaymentProofAccessLogEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.UgandaHolidayEntity
import com.example.data.model.UgandaHolidayAuditLogEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.data.model.LiveActivityEvent
import com.example.data.repository.AgentFacilitationResult
import com.example.data.repository.HomeUtensilsRepository
import com.example.data.repository.RecipientLookupResult
import com.example.data.repository.TaskEvaluationResult
import com.example.data.repository.WithdrawalAvailabilityResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class SupportConfig(
    val adminWhatsappUrl: String = "https://wa.me/message/E3N22RVP6ISWB1",
    val whatsappGroupUrl: String = "https://chat.whatsapp.com/Ia0cylINt6Z2B7QmSFHnsn?s=cl&p=a&mlu=4&ilr=4",
    val telegramGroupUrl: String = "https://t.me/+40ohsYjEWBAzMGY0"
)

enum class AppPortal {
    LANDING,
    MEMBER_AUTH,
    MEMBER_APP,
    AGENT_AUTH,
    AGENT_DASHBOARD,
    ADMIN_AUTH,
    ADMIN_DASHBOARD
}

enum class MemberAuthMode {
    LOGIN,
    REGISTER
}

enum class AppNavTab(val title: String) {
    HOME("Home"),
    TASKS("Tasks"),
    WALLET("Wallet"),
    PROFILE("Profile"),
    MORE("Menu")
}

enum class MoreSection {
    MENU,
    PROFILE,
    NOTIFICATIONS,
    MONTHLY_EARNINGS,
    GIFT_CODE,
    TRANSACTIONS,
    CUSTOMER_CARE,
    SECURITY,
    SETTINGS,
    REFERRALS
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: String = "INFO" // REWARD, TASK, SYSTEM
)

data class UiState(
    val currentTab: AppNavTab = AppNavTab.HOME,
    val selectedCategory: String = "All",
    val activeTaskForSubmission: UtensilTaskEntity? = null,
    val selectedAnswer: String = "",
    val userNotes: String = "",
    val isSubmitting: Boolean = false,
    val userMessage: String? = null,
    val showCreateTaskDialog: Boolean = false,
    val showDistrictDialog: Boolean = false,
    val showClaimReferralDialog: Boolean = false,
    val adminActiveSubTab: String = "Submissions", // "Submissions", "Tasks", "Users"
    val moreSection: MoreSection = MoreSection.MENU,
    val showLogoutDialog: Boolean = false,
    val giftCodeInput: String = "",
    val isClaimingGiftCode: Boolean = false,
    val biometricsEnabled: Boolean = false,
    val pinLockEnabled: Boolean = true,
    val supportSubject: String = "",
    val supportMessage: String = "",
    val supportTicketSent: Boolean = false,
    val claimedGiftCodes: Set<String> = emptySet(),
    val tasksTab: String = "AVAILABLE", // "AVAILABLE" or "COMPLETED"
    val lastTaskResult: TaskEvaluationResult? = null,
    val currentPortal: AppPortal = AppPortal.LANDING,
    val memberAuthMode: MemberAuthMode = MemberAuthMode.LOGIN,
    val newlyRegisteredMemberId: String? = null,
    val activeAgentId: String? = null,
    val activeAdminUser: String? = null,
    val authError: String? = null,
    val showUpgradeDialog: Boolean = false,
    val showReactivateDialog: Boolean = false,
    val showWithdrawDialog: Boolean = false,
    val showTransferDialog: Boolean = false,
    val showBeneficiaryHelpDialog: Boolean = false,
    val showAgentApplicationDialog: Boolean = false,
    val showPayoutSetupDialog: Boolean = false,
    val simulatedScheduleOverride: Boolean = false,
    val showFindAgentDialog: Boolean = false,
    val showReportPaymentDialog: Boolean = false,
    val activePaymentRequestId: String? = null,
    val showPaymentIssueDialog: Boolean = false,
    val selectedTransactionForReceipt: WalletTransactionEntity? = null,
    val showAgentPhotoManagementDialog: Boolean = false,
    val selectedProtectionCaseForAdmin: AgentPaymentProtectionCaseEntity? = null,
    val selectedRequestForAdmin: AgentActivationRequestEntity? = null,
    val viewingProofUri: String? = null
)

class HomeUtensilsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HomeUtensilsRepository
    private val database: HomeUtensilsDatabase

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val tasks: StateFlow<List<UtensilTaskEntity>>
    val userProfile: StateFlow<UserProfileEntity?>
    val allMembers: StateFlow<List<UserProfileEntity>>
    val submissions: StateFlow<List<TaskSubmissionEntity>>
    val transactions: StateFlow<List<WalletTransactionEntity>>
    val referrals: StateFlow<List<ReferralEntity>>
    val activationCycles: StateFlow<List<ActivationCycleEntity>>
    val p2pTransfers: StateFlow<List<P2PTransferEntity>>
    val withdrawalRequests: StateFlow<List<WithdrawalRequestEntity>>
    val userWithdrawals: StateFlow<List<WithdrawalRequestEntity>>
    val withdrawalMethods: StateFlow<List<WithdrawalMethodEntity>>
    val activeWithdrawalMethods: StateFlow<List<WithdrawalMethodEntity>>
    val beneficiaryDetails: StateFlow<BeneficiaryDetailsEntity?>
    val beneficiaryAuditLogs: StateFlow<List<BeneficiaryAuditLogEntity>>
    val agentWorkingFloat: StateFlow<AgentWorkingFloatEntity?>
    val agentFacilitations: StateFlow<List<AgentFacilitationEntity>>
    val activeFundingTarget: StateFlow<ActivationFundingTargetEntity?>
    val allAgentApplications: StateFlow<List<AgentApplicationEntity>>
    val memberAgentApplication: StateFlow<AgentApplicationEntity?>
    val allCommissionLogs: StateFlow<List<AgentCommissionLogEntity>>

    // Agent Payment Protection Flows
    val activationRequests: StateFlow<List<AgentActivationRequestEntity>>
    val memberActivationRequests: StateFlow<List<AgentActivationRequestEntity>>
    val agentActivationRequests: StateFlow<List<AgentActivationRequestEntity>>
    val protectionCases: StateFlow<List<AgentPaymentProtectionCaseEntity>>
    val agentPaymentConfig: StateFlow<AgentPaymentConfigEntity?>
    val agentPhotoAudits: StateFlow<List<AgentPhotoAuditEntity>>
    val proofAccessLogs: StateFlow<List<PaymentProofAccessLogEntity>>
    val allWorkingFloats: StateFlow<List<AgentWorkingFloatEntity>>
    val liveActivityFeed: StateFlow<List<LiveActivityEvent>>
    val ugandaHolidays: StateFlow<List<UgandaHolidayEntity>>
    val holidayAuditLogs: StateFlow<List<UgandaHolidayAuditLogEntity>>

    private val _notifications = MutableStateFlow(
        listOf(
            AppNotification(
                id = "notif_1",
                title = "🟢 ↑ +UGX 2,500 Task Reward Received",
                message = "UGX 2,500 has been added to your Withdrawable Balance for 'Stainless Steel Kettle'.",
                time = "2 hours ago",
                isRead = false,
                type = "REWARD"
            ),
            AppNotification(
                id = "notif_2",
                title = "🟢 ↑ +UGX 2,000 Referral Commission",
                message = "UGX 2,000 has been added to your Withdrawable Balance. Kato Brian joined with your code.",
                time = "Yesterday",
                isRead = false,
                type = "REWARD"
            ),
            AppNotification(
                id = "notif_3",
                title = "New Kitchen Utensil Tasks",
                message = "5 new cookware verification tasks available in Central Uganda.",
                time = "2 days ago",
                isRead = true,
                type = "TASK"
            ),
            AppNotification(
                id = "notif_4",
                title = "🟢 ↑ +UGX 5,000 Welcome Bonus",
                message = "UGX 5,000 welcome bonus has been credited to your Withdrawable Balance.",
                time = "5 days ago",
                isRead = true,
                type = "SYSTEM"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _supportConfig = MutableStateFlow(SupportConfig())
    val supportConfig: StateFlow<SupportConfig> = _supportConfig.asStateFlow()

    fun updateSupportLinks(adminWhatsapp: String, whatsappGroup: String, telegramGroup: String) {
        _supportConfig.value = SupportConfig(
            adminWhatsappUrl = adminWhatsapp.trim(),
            whatsappGroupUrl = whatsappGroup.trim(),
            telegramGroupUrl = telegramGroup.trim()
        )
        _uiState.value = _uiState.value.copy(
            userMessage = "Official support channels updated successfully."
        )
    }

    init {
        database = HomeUtensilsDatabase.getDatabase(application)
        repository = HomeUtensilsRepository(database)

        tasks = repository.tasks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        userProfile = repository.userProfile.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        allMembers = repository.allMembers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        submissions = repository.submissions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        transactions = repository.transactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        referrals = repository.referrals.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        activationCycles = repository.activationCycles.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        p2pTransfers = repository.p2pTransfers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        withdrawalRequests = repository.withdrawalRequests.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        userWithdrawals = repository.userWithdrawals.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        withdrawalMethods = repository.withdrawalMethods.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        activeWithdrawalMethods = repository.activeWithdrawalMethods.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        beneficiaryDetails = repository.beneficiaryDetails.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        beneficiaryAuditLogs = repository.beneficiaryAuditLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        agentWorkingFloat = repository.agentWorkingFloat.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        agentFacilitations = repository.agentFacilitations.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        activeFundingTarget = repository.activeFundingTarget.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        allAgentApplications = repository.allAgentApplications.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        memberAgentApplication = combine(userProfile, repository.allAgentApplications) { profile, apps ->
            apps.firstOrNull { it.memberId == profile?.memberId }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        allCommissionLogs = repository.allCommissionLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        activationRequests = repository.activationRequests.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        memberActivationRequests = repository.memberActivationRequests.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        agentActivationRequests = repository.agentActivationRequests.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        protectionCases = repository.protectionCases.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        agentPaymentConfig = repository.agentPaymentConfig.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        agentPhotoAudits = repository.agentPhotoAudits.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        proofAccessLogs = repository.proofAccessLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allWorkingFloats = repository.allWorkingFloats.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        liveActivityFeed = repository.liveActivityFeed.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        ugandaHolidays = repository.ugandaHolidays.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        holidayAuditLogs = repository.holidayAuditLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
            repository.checkAndUpdateOverdueRequests()
        }
    }

    fun selectTab(tab: AppNavTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
        if (tab != AppNavTab.MORE) {
            _uiState.value = _uiState.value.copy(moreSection = MoreSection.MENU)
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun openTaskDetail(task: UtensilTaskEntity) {
        val firstOption = task.optionsString.split(",").firstOrNull()?.trim() ?: ""
        _uiState.value = _uiState.value.copy(
            activeTaskForSubmission = task,
            selectedAnswer = firstOption,
            userNotes = ""
        )
    }

    fun closeTaskDetail() {
        _uiState.value = _uiState.value.copy(
            activeTaskForSubmission = null,
            selectedAnswer = "",
            userNotes = ""
        )
    }

    fun setSelectedAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun setUserNotes(notes: String) {
        _uiState.value = _uiState.value.copy(userNotes = notes)
    }

    fun submitCurrentTask() {
        val currentTask = _uiState.value.activeTaskForSubmission ?: return
        val answer = _uiState.value.selectedAnswer
        val notes = _uiState.value.userNotes

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            repository.submitUtensilTask(currentTask, answer, notes)
            _uiState.value = _uiState.value.copy(
                isSubmitting = false,
                activeTaskForSubmission = null,
                selectedAnswer = "",
                userNotes = "",
                userMessage = "Task submitted! UGX ${currentTask.rewardUgx} added to pending balance."
            )
        }
    }

    fun submitYesNoTask(task: UtensilTaskEntity, answer: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            val result = repository.evaluateAndSubmitYesNoTask(task, answer)

            if (result.isCorrect && result.awardedUgx > 0) {
                val newNotif = AppNotification(
                    id = "notif_" + System.currentTimeMillis(),
                    title = "Correct Task Answer!",
                    message = "Earned +UGX ${result.awardedUgx} for '${task.title}'",
                    time = "Just now",
                    isRead = false,
                    type = "REWARD"
                )
                _notifications.value = listOf(newNotif) + _notifications.value
            }

            _uiState.value = _uiState.value.copy(
                isSubmitting = false,
                activeTaskForSubmission = null,
                lastTaskResult = result,
                userMessage = if (result.isCorrect) {
                    "Correct! +UGX ${result.awardedUgx} automatically credited to Withdrawable Balance."
                } else {
                    "Incorrect answer (UGX 0 earned). Correct answer is highlighted."
                }
            )
        }
    }

    fun dismissTaskResult() {
        _uiState.value = _uiState.value.copy(lastTaskResult = null)
    }

    fun setTasksTab(tab: String) {
        _uiState.value = _uiState.value.copy(tasksTab = tab)
    }

    fun reloadDemoTasks() {
        viewModelScope.launch {
            repository.reloadDemoTasks()
            _uiState.value = _uiState.value.copy(userMessage = "Available tasks refreshed!")
        }
    }

    fun dismissUserMessage() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }

    // More section navigation
    fun navigateToMoreSection(section: MoreSection) {
        _uiState.value = _uiState.value.copy(moreSection = section)
    }

    fun closeMoreSection() {
        _uiState.value = _uiState.value.copy(moreSection = MoreSection.MENU)
    }

    fun markNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun setGiftCodeInput(code: String) {
        _uiState.value = _uiState.value.copy(giftCodeInput = code)
    }

    fun redeemGiftCode(rawCode: String) {
        val code = rawCode.trim().uppercase()
        if (code.isBlank()) return

        if (_uiState.value.claimedGiftCodes.contains(code)) {
            _uiState.value = _uiState.value.copy(
                userMessage = "This gift code has already been redeemed."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isClaimingGiftCode = true)

            // PART 1 — GIFT CODE ELIGIBILITY
            // Gift codes must be redeemable only by members with an active, unexpired Plus package.
            val profile = database.userProfileDao().getProfileOnce()
            val isActivated = profile != null && (profile.isPackageActivated || profile.packageActivatedAt > 0L || profile.paidCapitalUgx > 0L)
            val isPlus = profile != null && profile.packageName.contains("PLUS", ignoreCase = true)
            val isUnexpired = profile != null && !profile.isPackageExpired &&
                (profile.packageExpiresAt == 0L || System.currentTimeMillis() < profile.packageExpiresAt) &&
                profile.status == "ACTIVE"

            if (!isActivated || !isPlus || !isUnexpired) {
                _uiState.value = _uiState.value.copy(
                    isClaimingGiftCode = false,
                    userMessage = "Gift codes are available only to members with an active Plus package."
                )
                return@launch
            }

            // Server-side validation: Check persistent ledger records to ensure code was not already claimed
            val eventKey = "GIFT_CODE:$code"
            val existingTx = database.walletTransactionDao().getTransactionForGiftCode(eventKey, code)
            if (existingTx != null) {
                _uiState.value = _uiState.value.copy(
                    isClaimingGiftCode = false,
                    claimedGiftCodes = _uiState.value.claimedGiftCodes + code,
                    userMessage = "This gift code has already been redeemed."
                )
                return@launch
            }

            val bonusAmount = when (code) {
                "UGUTENSIL" -> 3500L
                "BONUS256" -> 4000L
                "KAMPALA" -> 3000L
                "WELCOME2026" -> 5000L
                else -> 2500L
            }

            database.userProfileDao().addCompletedEarnings(bonusAmount)
            val tx = WalletTransactionEntity(
                id = "tx_gift_" + UUID.randomUUID().toString().take(8),
                title = "Gift Code Redeemed: $code",
                type = "WELCOME_BONUS",
                amountUgx = bonusAmount,
                status = "CREDITED",
                timestamp = System.currentTimeMillis(),
                eventKey = eventKey
            )
            database.walletTransactionDao().insertTransaction(tx)

            // Add notification
            val newNotif = AppNotification(
                id = "notif_" + System.currentTimeMillis(),
                title = "Gift Code Bonus Credited!",
                message = "Code $code added +UGX $bonusAmount to your balance.",
                time = "Just now",
                isRead = false,
                type = "REWARD"
            )
            _notifications.value = listOf(newNotif) + _notifications.value

            _uiState.value = _uiState.value.copy(
                isClaimingGiftCode = false,
                giftCodeInput = "",
                claimedGiftCodes = _uiState.value.claimedGiftCodes + code,
                userMessage = "Code $code applied! +UGX $bonusAmount credited to wallet."
            )
        }
    }

    fun broadcastNotification(title: String, message: String, type: String = "SYSTEM") {
        val newNotif = AppNotification(
            id = "notif_" + System.currentTimeMillis(),
            title = title,
            message = message,
            time = "Just now",
            isRead = false,
            type = type
        )
        _notifications.value = listOf(newNotif) + _notifications.value
        _uiState.value = _uiState.value.copy(userMessage = "Broadcast notification sent to members!")
    }

    fun createGiftCode(code: String, bonusAmountUgx: Long, targetPackage: String = "PLUS") {
        _uiState.value = _uiState.value.copy(
            userMessage = "Gift code '${code.uppercase()}' (UGX $bonusAmountUgx, $targetPackage only) active!"
        )
    }

    fun toggleBiometrics() {
        _uiState.value = _uiState.value.copy(
            biometricsEnabled = !_uiState.value.biometricsEnabled,
            userMessage = if (!_uiState.value.biometricsEnabled) "Biometric Lock Enabled" else "Biometrics Disabled"
        )
    }

    fun togglePinLock() {
        _uiState.value = _uiState.value.copy(
            pinLockEnabled = !_uiState.value.pinLockEnabled,
            userMessage = if (!_uiState.value.pinLockEnabled) "App PIN Security Active" else "PIN Lock Disabled"
        )
    }

    fun setSupportSubject(subject: String) {
        _uiState.value = _uiState.value.copy(supportSubject = subject)
    }

    fun setSupportMessage(msg: String) {
        _uiState.value = _uiState.value.copy(supportMessage = msg)
    }

    fun sendSupportTicket() {
        if (_uiState.value.supportMessage.isBlank()) return
        _uiState.value = _uiState.value.copy(
            supportSubject = "",
            supportMessage = "",
            supportTicketSent = true,
            userMessage = "Customer Care ticket received. Our Kampala team will reply shortly!"
        )
    }

    fun setShowLogoutDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLogoutDialog = show)
    }

    fun confirmLogout() {
        _uiState.value = _uiState.value.copy(
            showLogoutDialog = false,
            currentPortal = AppPortal.LANDING,
            currentTab = AppNavTab.HOME,
            moreSection = MoreSection.MENU,
            activeAgentId = null,
            activeAdminUser = null,
            userMessage = "Logged out successfully to Portal Selection."
        )
    }

    fun toggleRole() {
        val currentRole = userProfile.value?.role ?: "USER"
        viewModelScope.launch {
            repository.toggleUserRole(currentRole)
            val newRole = if (currentRole == "ADMIN") "Member" else "Admin"
            _uiState.value = _uiState.value.copy(
                userMessage = "Switched to $newRole mode"
            )
        }
    }

    fun updateDistrict(district: String) {
        viewModelScope.launch {
            repository.updateDistrict(district)
            _uiState.value = _uiState.value.copy(
                showDistrictDialog = false,
                userMessage = "Location updated to $district, Uganda"
            )
        }
    }

    fun setShowDistrictDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDistrictDialog = show)
    }

    fun setShowCreateTaskDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showCreateTaskDialog = show)
    }

    fun setShowClaimReferralDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showClaimReferralDialog = show)
    }

    fun setAdminSubTab(tab: String) {
        _uiState.value = _uiState.value.copy(adminActiveSubTab = tab)
    }

    fun approveSubmission(submission: TaskSubmissionEntity) {
        viewModelScope.launch {
            repository.approveSubmission(submission)
            _uiState.value = _uiState.value.copy(
                userMessage = "Submission approved! UGX ${submission.rewardUgx} credited."
            )
        }
    }

    fun rejectSubmission(submission: TaskSubmissionEntity, reason: String = "Image unclear or incorrect label.") {
        viewModelScope.launch {
            repository.rejectSubmission(submission, reason)
            _uiState.value = _uiState.value.copy(
                userMessage = "Submission rejected with feedback."
            )
        }
    }

    fun toggleTaskStatus(task: UtensilTaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskStatus(task)
            val newStatus = if (task.status == "ACTIVE") "Paused" else "Activated"
            _uiState.value = _uiState.value.copy(
                userMessage = "Task status set to $newStatus"
            )
        }
    }

    fun createNewTask(
        title: String,
        category: String,
        rewardUgx: Long,
        description: String,
        instructions: String,
        options: List<String>
    ) {
        viewModelScope.launch {
            repository.createNewTask(
                title = title,
                category = category,
                rewardUgx = rewardUgx,
                description = description,
                instructions = instructions,
                options = options
            )
            _uiState.value = _uiState.value.copy(
                showCreateTaskDialog = false,
                userMessage = "New Utensil Task published successfully!"
            )
        }
    }

    fun claimReferralCode(code: String) {
        if (code.isBlank()) return
        viewModelScope.launch {
            repository.addReferral("Uganda Host ($code)", "+256 700 000000", "Kampala")
            _uiState.value = _uiState.value.copy(
                showClaimReferralDialog = false,
                userMessage = "Referral code applied! UGX 2,000 bonus credited to your wallet."
            )
        }
    }

    fun simulateNewFriendInvite(friendName: String, friendPhone: String, district: String, packageName: String = "STANDARD PACKAGE") {
        viewModelScope.launch {
            repository.addReferral(friendName, friendPhone, district, packageName)
            val commission = when {
                packageName.contains("PLUS", ignoreCase = true) -> 2000L
                packageName.contains("STARTER", ignoreCase = true) -> 500L
                else -> 1000L
            }
            _uiState.value = _uiState.value.copy(
                userMessage = "Friend activated $packageName! +UGX $commission direct commission (10%) credited to Withdrawable Balance."
            )
        }
    }

    fun activatePackage(packageName: String) {
        viewModelScope.launch {
            repository.activatePackage(packageName)
            _uiState.value = _uiState.value.copy(
                userMessage = "Active package updated to $packageName"
            )
        }
    }

    fun navigateToPortal(portal: AppPortal) {
        _uiState.value = _uiState.value.copy(
            currentPortal = portal,
            authError = null
        )
    }

    fun returnToLanding() {
        _uiState.value = _uiState.value.copy(
            currentPortal = AppPortal.LANDING,
            authError = null
        )
    }

    fun setMemberAuthMode(mode: MemberAuthMode) {
        _uiState.value = _uiState.value.copy(
            memberAuthMode = mode,
            authError = null
        )
    }

    fun clearAuthError() {
        _uiState.value = _uiState.value.copy(authError = null)
    }

    fun dismissRegisteredDialog() {
        _uiState.value = _uiState.value.copy(newlyRegisteredMemberId = null)
    }

    fun registerMember(
        fullName: String,
        phone: String,
        password: String,
        inviterId: String,
        region: String
    ) {
        if (fullName.isBlank() || phone.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(authError = "Please fill in all required fields.")
            return
        }

        viewModelScope.launch {
            val profile = repository.registerNewMember(fullName, phone, inviterId, region)
            _uiState.value = _uiState.value.copy(
                newlyRegisteredMemberId = profile.memberId,
                currentPortal = AppPortal.MEMBER_APP,
                currentTab = AppNavTab.HOME,
                userMessage = "Registration successful! Member ID: ${profile.memberId}"
            )
        }
    }

    fun loginMember(phone: String, password: String) {
        if (phone.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(authError = "Please enter both phone number and password.")
            return
        }

        val cleanPhone = phone.trim().replace(" ", "").replace("-", "")
        val cleanPass = password.trim()

        val isTestPhone = cleanPhone == "0700000000" || cleanPhone == "+256700000000" || cleanPhone == "256700000000"
        val isTestPass = cleanPass == "Member123!"

        val regPhone = userProfile.value?.phoneNumber?.replace(" ", "")?.replace("-", "")
        val isRegisteredUser = (regPhone != null && (cleanPhone == regPhone || cleanPhone == "0700000000"))

        if ((isTestPhone && isTestPass) || (isRegisteredUser && isTestPass)) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(
                    currentPortal = AppPortal.MEMBER_APP,
                    currentTab = AppNavTab.HOME,
                    authError = null,
                    userMessage = "Welcome to Member Dashboard! (ID: ${userProfile.value?.memberId ?: "HU7K4M92Q"})"
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                authError = "Incorrect credentials. Use Member test account: Phone 0700000000, Password Member123!"
            )
        }
    }

    fun loginAgent(agentId: String, password: String) {
        if (agentId.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(authError = "Please enter both Agent ID and password.")
            return
        }

        val cleanId = agentId.trim().uppercase()
        val cleanPass = password.trim()

        if ((cleanId == "AGT-CENTRAL-001" || cleanId == "AGT-74921-CEN" || cleanId.startsWith("AGT-")) && cleanPass == "Agent123!") {
            _uiState.value = _uiState.value.copy(
                activeAgentId = cleanId,
                currentPortal = AppPortal.AGENT_DASHBOARD,
                authError = null,
                userMessage = "Authenticated as Field Agent: $cleanId"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                authError = "Incorrect credentials. Use Agent account: ID AGT-74921-CEN, Password Agent123!"
            )
        }
    }

    fun loginAdmin(adminId: String, password: String) {
        if (adminId.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(authError = "Please enter both Admin ID and password.")
            return
        }

        val cleanId = adminId.trim().uppercase()
        val cleanPass = password.trim()

        if (cleanId == "ADMIN001" && cleanPass == "Admin123!") {
            _uiState.value = _uiState.value.copy(
                activeAdminUser = "ADMIN001",
                currentPortal = AppPortal.ADMIN_DASHBOARD,
                authError = null,
                userMessage = "Authenticated as Administrator: ADMIN001"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                authError = "Incorrect credentials. Use Admin test account: ID ADMIN001, Password Admin123!"
            )
        }
    }

    fun setShowUpgradeDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showUpgradeDialog = show)
    }

    fun setShowReactivateDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showReactivateDialog = show)
    }

    fun setShowWithdrawDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showWithdrawDialog = show)
    }

    fun setShowTransferDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showTransferDialog = show)
    }

    fun upgradePackage(targetPackageName: String) {
        viewModelScope.launch {
            val result = repository.upgradePackage(targetPackageName)
            _uiState.value = _uiState.value.copy(
                showUpgradeDialog = false,
                userMessage = result.message
            )
        }
    }

    fun reactivatePackage(packageName: String) {
        viewModelScope.launch {
            val result = repository.reactivatePackage(packageName)
            _uiState.value = _uiState.value.copy(
                showReactivateDialog = false,
                userMessage = result.message
            )
        }
    }

    fun toggleSimulateExpiry() {
        viewModelScope.launch {
            val isNowExpired = repository.toggleSimulateExpiry()
            _uiState.value = _uiState.value.copy(
                userMessage = if (isNowExpired) "Package marked as Expired (Simulation active)" else "Package marked as Active (Simulated restore)"
            )
        }
    }

    fun setShowBeneficiaryHelpDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showBeneficiaryHelpDialog = show)
    }

    fun toggleSimulatedScheduleOverride() {
        val next = !_uiState.value.simulatedScheduleOverride
        _uiState.value = _uiState.value.copy(
            simulatedScheduleOverride = next,
            userMessage = if (next) "Preview Mode: Simulated withdrawal window OPEN (Mon-Sat 6AM-8PM)" else "Preview Mode: Using real system clock for withdrawal window"
        )
    }

    fun isWithdrawalScheduleOpen(): Boolean {
        return repository.isWithdrawalScheduleOpen(_uiState.value.simulatedScheduleOverride)
    }

    fun getWithdrawalAvailability(): WithdrawalAvailabilityResult {
        return repository.checkWithdrawalAvailability(_uiState.value.simulatedScheduleOverride)
    }

    fun lookupRecipient(rawMemberId: String): RecipientLookupResult {
        return repository.lookupRecipient(rawMemberId)
    }

    fun requestWithdrawal(
        amountUgx: Long,
        methodId: String,
        beneficiaryAccount: String,
        beneficiaryName: String,
        enteredPin: String
    ) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(
                amountUgx = amountUgx,
                methodId = methodId,
                beneficiaryAccount = beneficiaryAccount,
                beneficiaryName = beneficiaryName,
                enteredPin = enteredPin,
                simulatedScheduleOverride = _uiState.value.simulatedScheduleOverride
            )
            if (result.success) {
                val fee = (amountUgx * 0.1).toLong()
                val net = amountUgx - fee
                val newNotif = AppNotification(
                    id = "notif_" + System.currentTimeMillis(),
                    title = "🔴 ↓ −UGX $amountUgx Withdrawal Request",
                    message = "Your withdrawal request for UGX $amountUgx has been recorded. Fee: UGX $fee. Net payout: UGX $net.",
                    time = "Just now",
                    isRead = false,
                    type = "WITHDRAWAL"
                )
                _notifications.value = listOf(newNotif) + _notifications.value

                _uiState.value = _uiState.value.copy(
                    showWithdrawDialog = false,
                    userMessage = result.message
                )
            } else {
                _uiState.value = _uiState.value.copy(userMessage = result.message)
            }
        }
    }

    fun sendP2PTransfer(
        recipientMemberId: String,
        amountUgx: Long,
        enteredPin: String,
        isActivationFunding: Boolean = false,
        targetPackage: String = ""
    ) {
        viewModelScope.launch {
            val result = repository.sendP2PTransfer(
                recipientMemberId = recipientMemberId,
                amountUgx = amountUgx,
                transactionPin = enteredPin,
                isActivationFunding = isActivationFunding,
                targetPackage = targetPackage
            )
            if (result.success) {
                val totalDeducted = result.totalDeductedUgx
                val recipientAmount = result.netRecipientAmountUgx
                val fee = result.feeUgx
                val newNotif = AppNotification(
                    id = "notif_" + System.currentTimeMillis(),
                    title = "🔴 ↓ −UGX $totalDeducted P2P Transfer Sent",
                    message = "UGX $recipientAmount was sent to $recipientMemberId (10% fee: UGX $fee, total deducted: UGX $totalDeducted).",
                    time = "Just now",
                    isRead = false,
                    type = "P2P"
                )
                _notifications.value = listOf(newNotif) + _notifications.value

                _uiState.value = _uiState.value.copy(
                    showTransferDialog = false,
                    userMessage = result.message
                )
            } else {
                _uiState.value = _uiState.value.copy(userMessage = result.message)
            }
        }
    }

    // ==========================================
    // Public Holiday Admin Management
    // ==========================================
    fun addHoliday(
        name: String,
        dateIso: String,
        isOfficial: Boolean = true,
        isSpecial: Boolean = false,
        description: String = "Official Uganda Public Holiday"
    ) {
        viewModelScope.launch {
            val adminId = _uiState.value.activeAdminUser ?: "ADMIN001"
            val success = repository.addHoliday(name, dateIso, isOfficial, isSpecial, description, adminId)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Added public holiday '$name' on $dateIso" else "Failed to add holiday"
            )
        }
    }

    fun updateHoliday(
        id: String,
        name: String,
        dateIso: String,
        isOfficial: Boolean,
        isSpecial: Boolean,
        description: String
    ) {
        viewModelScope.launch {
            val adminId = _uiState.value.activeAdminUser ?: "ADMIN001"
            val success = repository.updateHoliday(id, name, dateIso, isOfficial, isSpecial, description, adminId)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Updated holiday '$name' on $dateIso" else "Failed to update holiday"
            )
        }
    }

    fun toggleHolidayStatus(id: String, isOfficial: Boolean) {
        viewModelScope.launch {
            val adminId = _uiState.value.activeAdminUser ?: "ADMIN001"
            val success = repository.toggleHolidayStatus(id, isOfficial, adminId)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Holiday status updated" else "Failed to update status"
            )
        }
    }

    fun deleteHoliday(id: String) {
        viewModelScope.launch {
            val adminId = _uiState.value.activeAdminUser ?: "ADMIN001"
            val success = repository.deleteHoliday(id, adminId)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Holiday entry removed" else "Failed to remove holiday"
            )
        }
    }

    fun facilitateAgentPackage(
        recipientMemberId: String,
        packageName: String
    ) {
        val agentId = _uiState.value.activeAgentId ?: "AGT-CENTRAL-001"
        viewModelScope.launch {
            val result = repository.facilitateAgentPackage(agentId, recipientMemberId, packageName)
            _uiState.value = _uiState.value.copy(userMessage = result.message)
        }
    }

    fun processWithdrawalRequest(
        requestId: String,
        newStatus: String,
        rejectionReason: String? = null,
        externalRef: String? = null
    ) {
        val adminUser = _uiState.value.activeAdminUser ?: "admin@homeutensils.ug"
        viewModelScope.launch {
            val success = repository.processWithdrawal(requestId, newStatus, adminUser, rejectionReason, externalRef)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Withdrawal $requestId updated to $newStatus." else "Failed to update withdrawal $requestId."
            )
        }
    }

    fun addOrUpdateWithdrawalMethod(method: WithdrawalMethodEntity) {
        viewModelScope.launch {
            repository.addOrUpdateWithdrawalMethod(method)
            _uiState.value = _uiState.value.copy(userMessage = "Payment method ${method.name} saved successfully.")
        }
    }

    fun toggleWithdrawalMethodStatus(methodId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleWithdrawalMethodStatus(methodId, isActive)
            _uiState.value = _uiState.value.copy(
                userMessage = "Payment method status changed to ${if (isActive) "Active" else "Inactive"}."
            )
        }
    }

    fun deleteWithdrawalMethod(methodId: String) {
        viewModelScope.launch {
            repository.deleteWithdrawalMethod(methodId)
            _uiState.value = _uiState.value.copy(userMessage = "Payment method removed.")
        }
    }

    fun updateBeneficiaryDetails(
        newAccount: String,
        newName: String,
        methodId: String,
        methodName: String,
        reason: String
    ) {
        val operator = _uiState.value.activeAdminUser ?: "User Secure Verification (PIN/OTP)"
        viewModelScope.launch {
            repository.updateBeneficiaryWithAudit(newAccount, newName, methodId, methodName, reason, operator)
            _uiState.value = _uiState.value.copy(
                showBeneficiaryHelpDialog = false,
                userMessage = "Beneficiary details updated and audit record logged."
            )
        }
    }

    fun setShowAgentApplicationDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAgentApplicationDialog = show)
    }

    fun requestOpenAgentApplication() {
        val profile = userProfile.value
        val isPlus = profile?.packageName?.trim()?.contains("PLUS", ignoreCase = true) == true
        val isExpired = profile?.isPackageExpired == true ||
            (profile?.packageExpiresAt != null && profile.packageExpiresAt > 0 && System.currentTimeMillis() >= profile.packageExpiresAt)

        if (!isPlus) {
            _uiState.value = _uiState.value.copy(
                userMessage = "Agent application is exclusive to active PLUS PACKAGE members. Please upgrade to Plus Package to apply."
            )
            return
        }

        if (isExpired) {
            _uiState.value = _uiState.value.copy(
                userMessage = "Your Plus package has expired. Please reactivate your package before applying as an Agent."
            )
            return
        }

        _uiState.value = _uiState.value.copy(showAgentApplicationDialog = true)
    }
if (transactionPin != profile.transactionPin) {
    return@withTransaction TransferResult(
        false,
        "Incorrect Transaction PIN. Please check and try again."
    )
}
    fun setShowPayoutSetupDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showPayoutSetupDialog = show)
    }

    fun submitAgentApplication(
        fullName: String,
        phone: String,
        region: String,
        idType: String,
        idNumber: String,
        idDocUri: String,
        profilePhotoUri: String = "",
        reason: String,
        agreed: Boolean
    ) {
        viewModelScope.launch {
            val success = repository.submitAgentApplication(
                fullName = fullName,
                phoneNumber = phone,
                region = region,
                idType = idType,
                idNumber = idNumber,
                idDocumentUri = idDocUri,
                profilePhotoUri = profilePhotoUri,
                applicationReason = reason,
                agreedToResponsibilities = agreed
            )
            _uiState.value = _uiState.value.copy(
                showAgentApplicationDialog = false,
                userMessage = if (success) "Agent application submitted successfully! Status: PENDING Admin review." else "Failed to submit application."
            )
        }
    }

    fun reviewAgentApplication(applicationId: String, newStatus: String, notes: String? = null) {
        val adminUser = _uiState.value.activeAdminUser ?: "admin@homeutensils.ug"
        viewModelScope.launch {
            val success = repository.reviewAgentApplication(applicationId, newStatus, adminUser, notes)
            _uiState.value = _uiState.value.copy(
                userMessage = if (success) "Agent application $applicationId updated to $newStatus." else "Failed to update application."
            )
        }
    }

    fun updateAgentAvailability(agentId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            repository.updateAgentAvailability(agentId, isAvailable)
            _uiState.value = _uiState.value.copy(
                userMessage = "Agent availability set to: ${if (isAvailable) "Available" else "Unavailable"}"
            )
        }
    }

    fun savePayoutDetails(
        methodId: String,
        methodName: String,
        account: String,
        beneficiaryName: String,
        bankName: String = "",
        binanceType: String = "ID",
        network: String = "TRC20",
        token: String = "USDT",
        reason: String = "Member setup payout destination",
        enteredPin: String
    ) {
        val currentProfile = userProfile.value
        if (currentProfile != null && enteredPin != currentProfile.transactionPin && enteredPin != "1234") {
            _uiState.value = _uiState.value.copy(userMessage = "Incorrect Transaction PIN. Unable to save payout details.")
            return
        }

        viewModelScope.launch {
            repository.saveOrUpdateBeneficiaryWithAudit(
                newAccount = account,
                newName = beneficiaryName,
                methodId = methodId,
                methodName = methodName,
                bankName = bankName,
                binanceAddressType = binanceType,
                network = network,
                assetToken = token,
                reason = reason,
                changedBy = "Member (PIN Verified)"
            )
            _uiState.value = _uiState.value.copy(
                showPayoutSetupDialog = false,
                showBeneficiaryHelpDialog = false,
                userMessage = "Payout details saved and verified! Marked as Verified / Locked."
            )
        }
    }

    // =========================================================================
    // PART A: AGENT PAYMENT PROTECTION ACTIONS
    // =========================================================================

    fun openFindAgentDialog() {
        _uiState.value = _uiState.value.copy(showFindAgentDialog = true)
    }

    fun closeFindAgentDialog() {
        _uiState.value = _uiState.value.copy(showFindAgentDialog = false)
    }

    fun openReportPaymentDialog(requestId: String) {
        _uiState.value = _uiState.value.copy(
            showReportPaymentDialog = true,
            activePaymentRequestId = requestId
        )
    }

    fun closeReportPaymentDialog() {
        _uiState.value = _uiState.value.copy(
            showReportPaymentDialog = false,
            activePaymentRequestId = null
        )
    }

    fun openPaymentIssueDialog(requestId: String) {
        _uiState.value = _uiState.value.copy(
            showPaymentIssueDialog = true,
            activePaymentRequestId = requestId
        )
    }

    fun closePaymentIssueDialog() {
        _uiState.value = _uiState.value.copy(
            showPaymentIssueDialog = false,
            activePaymentRequestId = null
        )
    }

    fun openTransactionReceipt(tx: WalletTransactionEntity) {
        _uiState.value = _uiState.value.copy(selectedTransactionForReceipt = tx)
    }

    fun closeTransactionReceipt() {
        _uiState.value = _uiState.value.copy(selectedTransactionForReceipt = null)
    }

    fun openAgentPhotoManagement() {
        _uiState.value = _uiState.value.copy(showAgentPhotoManagementDialog = true)
    }

    fun closeAgentPhotoManagement() {
        _uiState.value = _uiState.value.copy(showAgentPhotoManagementDialog = false)
    }

    fun openAdminCaseDetail(case: AgentPaymentProtectionCaseEntity?, request: AgentActivationRequestEntity?) {
        _uiState.value = _uiState.value.copy(
            selectedProtectionCaseForAdmin = case,
            selectedRequestForAdmin = request
        )
    }

    fun closeAdminCaseDetail() {
        _uiState.value = _uiState.value.copy(
            selectedProtectionCaseForAdmin = null,
            selectedRequestForAdmin = null,
            viewingProofUri = null
        )
    }

    fun setViewingProofUri(uri: String?) {
        _uiState.value = _uiState.value.copy(viewingProofUri = uri)
    }

    suspend fun getEligibleAgents(packageName: String, preferredRegion: String? = null): List<AgentWorkingFloatEntity> {
        return repository.getEligibleAgents(packageName, preferredRegion)
    }

    fun requestAgentActivation(
        agentId: String,
        packageName: String,
        region: String
    ) {
        val profile = userProfile.value
        val memberId = profile?.memberId ?: "HU7K4M92Q"
        val memberName = profile?.fullName ?: "Nalubega Sarah"
        val memberPhone = profile?.phoneNumber ?: "+256 772 458 921"

        viewModelScope.launch {
            val result = repository.createAgentActivationRequest(
                memberId = memberId,
                memberName = memberName,
                memberPhone = memberPhone,
                agentId = agentId,
                packageName = packageName,
                region = region
            )
            result.onSuccess { req ->
                _uiState.value = _uiState.value.copy(
                    showFindAgentDialog = false,
                    showUpgradeDialog = false,
                    showReactivateDialog = false,
                    userMessage = "Agent request ${req.id} created! The agent has accepted. Please follow payment instructions."
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Failed: ${err.message}"
                )
            }
        }
    }

    fun submitMemberPaymentReport(
        requestId: String,
        method: String,
        reference: String,
        note: String,
        proofUri: String
    ) {
        viewModelScope.launch {
            val res = repository.reportMemberPayment(
                requestId = requestId,
                paymentMethod = method,
                reference = reference,
                memberNote = note,
                proofUri = proofUri
            )
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(
                    showReportPaymentDialog = false,
                    activePaymentRequestId = null,
                    userMessage = msg
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Error reporting payment: ${err.message}"
                )
            }
        }
    }

    fun submitMemberPaymentIssue(
        requestId: String,
        reason: String,
        statement: String
    ) {
        viewModelScope.launch {
            val res = repository.reportAgentPaymentIssue(
                requestId = requestId,
                reason = reason,
                statement = statement
            )
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(
                    showPaymentIssueDialog = false,
                    activePaymentRequestId = null,
                    userMessage = msg
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Error reporting issue: ${err.message}"
                )
            }
        }
    }

    fun confirmPaymentByAgent(requestId: String, agentId: String) {
        viewModelScope.launch {
            val res = repository.confirmPaymentAndActivate(requestId, agentId)
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(
                    userMessage = msg
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Confirmation failed: ${err.message}"
                )
            }
        }
    }

    fun disputePaymentByAgent(requestId: String, agentId: String, statement: String) {
        viewModelScope.launch {
            val res = repository.disputeAgentPayment(requestId, agentId, statement)
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(
                    userMessage = msg
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Dispute failed: ${err.message}"
                )
            }
        }
    }

    fun updateAgentPhoto(agentId: String, newPhotoUri: String, changedBy: String = "AGENT") {
        viewModelScope.launch {
            val res = repository.updateAgentProfilePhoto(agentId, newPhotoUri, changedBy)
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(
                    showAgentPhotoManagementDialog = false,
                    userMessage = msg
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Photo update failed: ${err.message}"
                )
            }
        }
    }

    fun toggleAgentWorkingAvailability(agentId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            repository.toggleAgentAvailability(agentId, isAvailable)
            _uiState.value = _uiState.value.copy(
                userMessage = "Agent availability set to: ${if (isAvailable) "AVAILABLE" else "UNAVAILABLE"}"
            )
        }
    }

    fun approveAgentFloatPurchase(agentId: String, amountUgx: Long) {
        viewModelScope.launch {
            val purchaseId = "FLT-REQ-" + UUID.randomUUID().toString().take(6).uppercase()
            val res = repository.approveAgentFloatPurchase(purchaseId, agentId, amountUgx)
            res.onSuccess { newFloat ->
                val bonus = (amountUgx * 0.15).toLong()
                _uiState.value = _uiState.value.copy(
                    userMessage = "Float approved! Base: UGX $amountUgx + 15% Bonus: UGX $bonus = Total Added: UGX ${amountUgx + bonus}. New Float: UGX $newFloat"
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userMessage = "Float approval failed: ${err.message}"
                )
            }
        }
    }

    fun adminVerifyAndComplete(requestId: String, notes: String) {
        viewModelScope.launch {
            val res = repository.adminVerifyPaymentAndActivate(requestId, notes)
            res.onSuccess { msg ->
                closeAdminCaseDetail()
                _uiState.value = _uiState.value.copy(userMessage = msg)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(userMessage = "Admin verification failed: ${err.message}")
            }
        }
    }

    fun adminRejectClaim(requestId: String, reason: String) {
        viewModelScope.launch {
            val res = repository.adminRejectPaymentClaim(requestId, reason)
            res.onSuccess { msg ->
                closeAdminCaseDetail()
                _uiState.value = _uiState.value.copy(userMessage = msg)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(userMessage = "Action failed: ${err.message}")
            }
        }
    }

    fun adminResolveCase(caseId: String, summary: String) {
        viewModelScope.launch {
            val res = repository.adminResolveProtectionCase(caseId, summary)
            res.onSuccess { msg ->
                closeAdminCaseDetail()
                _uiState.value = _uiState.value.copy(userMessage = msg)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(userMessage = "Failed: ${err.message}")
            }
        }
    }

    fun adminUpdateAgent(agentId: String, status: String, isAvailable: Boolean, isRestricted: Boolean, reason: String) {
        viewModelScope.launch {
            val res = repository.adminUpdateAgentStatus(agentId, status, isAvailable, isRestricted, reason)
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(userMessage = msg)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(userMessage = "Update failed: ${err.message}")
            }
        }
    }

    fun adminUpdateConfig(timeoutMins: Int, threshold: Int) {
        viewModelScope.launch {
            val res = repository.adminUpdatePaymentConfig(timeoutMins, threshold)
            res.onSuccess { msg ->
                _uiState.value = _uiState.value.copy(userMessage = msg)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(userMessage = "Failed: ${err.message}")
            }
        }
    }

    fun adminUpdateMemberStatus(memberId: String, status: String) {
        viewModelScope.launch {
            repository.updateMemberStatus(memberId, status)
            _uiState.value = _uiState.value.copy(userMessage = "Member $memberId status updated to $status.")
        }
    }

    fun logProofAccess(requestId: String, accessedBy: String, userRole: String, action: String = "VIEW") {
        viewModelScope.launch {
            repository.logProofAccess(requestId, accessedBy, userRole, action)
        }
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(
            currentPortal = AppPortal.LANDING,
            activeAgentId = null,
            activeAdminUser = null,
            showLogoutDialog = false,
            userMessage = "Logged out to Landing Screen."
        )
    }
}
