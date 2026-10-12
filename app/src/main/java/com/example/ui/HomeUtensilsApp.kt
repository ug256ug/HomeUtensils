package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.ActivationFundingTargetEntity
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.model.BeneficiaryAuditLogEntity
import com.example.data.model.BeneficiaryDetailsEntity
import com.example.data.model.P2PTransferEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.ui.components.AgentApplicationDialog
import com.example.ui.components.AgentPhotoManagementDialog
import com.example.ui.components.BeneficiaryHelpDialog
import com.example.ui.components.FindAgentDialog
import com.example.ui.components.HomeUtensilsBottomNav
import com.example.ui.components.HomeUtensilsTopBar
import com.example.ui.components.P2PTransferDialog
import com.example.ui.components.ReactivatePackageDialog
import com.example.ui.components.ReportPaymentDialog
import com.example.ui.components.ReportPaymentIssueDialog
import com.example.ui.components.UpgradePackageDialog
import com.example.ui.components.VisualTransactionReceiptDialog
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.AgentDashboardScreen
import com.example.ui.screens.AgentLoginScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LandingPortalScreen
import com.example.ui.screens.MemberAuthScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReferralsScreen
import com.example.ui.screens.TaskSubmissionDialog
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmWhite
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.AppPortal
import com.example.ui.viewmodel.HomeUtensilsViewModel
import com.example.ui.viewmodel.MoreSection

@Composable
fun HomeUtensilsApp(
    viewModel: HomeUtensilsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val submissions by viewModel.submissions.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val referrals by viewModel.referrals.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val activationCycles by viewModel.activationCycles.collectAsStateWithLifecycle()
    val p2pTransfers by viewModel.p2pTransfers.collectAsStateWithLifecycle()
    val withdrawalRequests by viewModel.withdrawalRequests.collectAsStateWithLifecycle()
    val userWithdrawals by viewModel.userWithdrawals.collectAsStateWithLifecycle()
    val withdrawalMethods by viewModel.withdrawalMethods.collectAsStateWithLifecycle()
    val activeWithdrawalMethods by viewModel.activeWithdrawalMethods.collectAsStateWithLifecycle()
    val beneficiaryDetails by viewModel.beneficiaryDetails.collectAsStateWithLifecycle()
    val beneficiaryAuditLogs by viewModel.beneficiaryAuditLogs.collectAsStateWithLifecycle()
    val agentWorkingFloat by viewModel.agentWorkingFloat.collectAsStateWithLifecycle()
    val agentFacilitations by viewModel.agentFacilitations.collectAsStateWithLifecycle()
    val activeFundingTarget by viewModel.activeFundingTarget.collectAsStateWithLifecycle()

    // Agent Payment Protection State Collections
    val activationRequests by viewModel.activationRequests.collectAsStateWithLifecycle()
    val memberActivationRequests by viewModel.memberActivationRequests.collectAsStateWithLifecycle()
    val agentActivationRequests by viewModel.agentActivationRequests.collectAsStateWithLifecycle()
    val protectionCases by viewModel.protectionCases.collectAsStateWithLifecycle()
    val agentPaymentConfig by viewModel.agentPaymentConfig.collectAsStateWithLifecycle()
    val allWorkingFloats by viewModel.allWorkingFloats.collectAsStateWithLifecycle()
    val liveActivityFeed by viewModel.liveActivityFeed.collectAsStateWithLifecycle()
    val memberAgentApplication by viewModel.memberAgentApplication.collectAsStateWithLifecycle()
    val allMembers by viewModel.allMembers.collectAsStateWithLifecycle()
    val allAgentApplications by viewModel.allAgentApplications.collectAsStateWithLifecycle()
    val allCommissionLogs by viewModel.allCommissionLogs.collectAsStateWithLifecycle()
    val ugandaHolidays by viewModel.ugandaHolidays.collectAsStateWithLifecycle()
    val holidayAuditLogs by viewModel.holidayAuditLogs.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle snackbar messages
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissUserMessage()
        }
    }

    // Overarching Portal Navigation Switcher
    when (uiState.currentPortal) {
        AppPortal.LANDING -> {
            LandingPortalScreen(
                onSelectMemberPortal = { viewModel.navigateToPortal(AppPortal.MEMBER_AUTH) },
                onSelectAgentPortal = { viewModel.navigateToPortal(AppPortal.AGENT_AUTH) },
                onSelectAdminPortal = { viewModel.navigateToPortal(AppPortal.ADMIN_AUTH) }
            )
        }

        AppPortal.MEMBER_AUTH -> {
            MemberAuthScreen(
                authMode = uiState.memberAuthMode,
                errorMessage = uiState.authError,
                onSwitchMode = { viewModel.setMemberAuthMode(it) },
                onLogin = { phone, pass -> viewModel.loginMember(phone, pass) },
                onRegister = { name, phone, pass, inviterId, region ->
                    viewModel.registerMember(name, phone, pass, inviterId, region)
                },
                onBackToLanding = { viewModel.returnToLanding() }
            )
        }

        AppPortal.AGENT_AUTH -> {
            AgentLoginScreen(
                errorMessage = uiState.authError,
                onLogin = { agentId, pass -> viewModel.loginAgent(agentId, pass) },
                onBackToLanding = { viewModel.returnToLanding() }
            )
        }

        AppPortal.AGENT_DASHBOARD -> {
            AgentDashboardScreen(
                agentId = uiState.activeAgentId ?: "AGT-CENTRAL-001",
                workingFloat = agentWorkingFloat,
                facilitations = agentFacilitations,
                agentRequests = agentActivationRequests,
                onLookupRecipient = { viewModel.lookupRecipient(it) },
                onFacilitatePackage = { recId, pkg -> viewModel.facilitateAgentPackage(recId, pkg) },
                onConfirmPayment = { viewModel.confirmPaymentByAgent(it, uiState.activeAgentId ?: "AGT-CENTRAL-001") },
                onDisputePayment = { id, reason -> viewModel.disputePaymentByAgent(id, uiState.activeAgentId ?: "AGT-CENTRAL-001", reason) },
                onManagePhoto = { viewModel.openAgentPhotoManagement() },
                onLogout = { viewModel.returnToLanding() }
            )
        }

        AppPortal.ADMIN_AUTH -> {
            AdminLoginScreen(
                errorMessage = uiState.authError,
                onLogin = { username, pass -> viewModel.loginAdmin(username, pass) },
                onBackToLanding = { viewModel.returnToLanding() }
            )
        }

        AppPortal.ADMIN_DASHBOARD -> {
            AdminDashboardScreen(
                adminUser = uiState.activeAdminUser ?: "admin@homeutensils.ug",
                allMembers = allMembers,
                tasks = tasks,
                submissions = submissions,
                transactions = transactions,
                referrals = referrals,
                withdrawalRequests = withdrawalRequests,
                withdrawalMethods = withdrawalMethods,
                beneficiaryAuditLogs = beneficiaryAuditLogs,
                activationRequests = activationRequests,
                protectionCases = protectionCases,
                workingFloats = allWorkingFloats,
                agentApplications = allAgentApplications,
                commissionLogs = allCommissionLogs,
                paymentConfig = agentPaymentConfig,
                activationCycles = activationCycles,
                ugandaHolidays = ugandaHolidays,
                holidayAuditLogs = holidayAuditLogs,
                notifications = notifications,
                onProcessWithdrawal = { id, st, reason, ref -> viewModel.processWithdrawalRequest(id, st, reason, ref) },
                onAddOrUpdateMethod = { viewModel.addOrUpdateWithdrawalMethod(it) },
                onToggleMethodStatus = { id, act -> viewModel.toggleWithdrawalMethodStatus(id, act) },
                onDeleteMethod = { viewModel.deleteWithdrawalMethod(it) },
                onVerifyAndActivate = { id, notes -> viewModel.adminVerifyAndComplete(id, notes) },
                onRejectPaymentClaim = { id, reason -> viewModel.adminRejectClaim(id, reason) },
                onResolveCase = { id, summary -> viewModel.adminResolveCase(id, summary) },
                onRestrictAgent = { id, restr -> viewModel.adminUpdateAgent(id, "ACTIVE", true, restr, "Admin restriction updated") },
                onSuspendAgent = { id, st -> viewModel.adminUpdateAgent(id, st, false, true, "Admin status updated to $st") },
                onUpdateConfig = { t, th -> viewModel.adminUpdateConfig(t, th) },
                onApproveFloatPurchase = { id, amt -> viewModel.approveAgentFloatPurchase(id, amt) },
                onReviewAgentApplication = { id, st, notes -> viewModel.reviewAgentApplication(id, st, notes) },
                onUpdateMemberStatus = { id, st -> viewModel.adminUpdateMemberStatus(id, st) },
                onApproveSubmission = { viewModel.approveSubmission(it) },
                onRejectSubmission = { sub, reason -> viewModel.rejectSubmission(sub, reason) },
                onToggleTaskStatus = { viewModel.toggleTaskStatus(it) },
                onCreateTask = { title, cat, reward, desc, inst, opts -> viewModel.createNewTask(title, cat, reward, desc, inst, opts) },
                onCreateGiftCode = { code, bonus, targetPkg -> viewModel.createGiftCode(code, bonus, targetPkg) },
                onAddHoliday = { name, date, isOfficial, isSpecial, desc -> viewModel.addHoliday(name, date, isOfficial, isSpecial, desc) },
                onUpdateHoliday = { id, name, date, isOfficial, isSpecial, desc -> viewModel.updateHoliday(id, name, date, isOfficial, isSpecial, desc) },
                onToggleHolidayStatus = { id, isOfficial -> viewModel.toggleHolidayStatus(id, isOfficial) },
                onDeleteHoliday = { id -> viewModel.deleteHoliday(id) },
                onBroadcastNotification = { title, msg, type -> viewModel.broadcastNotification(title, msg, type) },
                onLogout = { viewModel.returnToLanding() }
            )
        }

        AppPortal.MEMBER_APP -> {
            // Full 5-tab Member Application
            MemberPortalAppContent(
                viewModel = viewModel,
                uiState = uiState,
                tasks = tasks,
                userProfile = userProfile,
                submissions = submissions,
                transactions = transactions,
                referrals = referrals,
                notifications = notifications,
                activationCycles = activationCycles,
                p2pTransfers = p2pTransfers,
                userWithdrawals = userWithdrawals,
                activeWithdrawalMethods = activeWithdrawalMethods,
                beneficiaryDetails = beneficiaryDetails,
                activeFundingTarget = activeFundingTarget,
                memberActivationRequests = memberActivationRequests,
                allWorkingFloats = allWorkingFloats,
                liveActivityFeed = liveActivityFeed,
                memberAgentApplication = memberAgentApplication,
                snackbarHostState = snackbarHostState
            )
        }
    }

    if (uiState.showAgentPhotoManagementDialog && agentWorkingFloat != null) {
        AgentPhotoManagementDialog(
            agent = agentWorkingFloat!!,
            onDismiss = { viewModel.closeAgentPhotoManagement() },
            onUpdatePhoto = { newUri ->
                viewModel.updateAgentPhoto(agentWorkingFloat!!.agentId, newUri, "AGENT")
            }
        )
    }
}

/**
 * 5-tab Member Portal Application with Home, Tasks, Referrals, Wallet, and More
 */
@Composable
private fun MemberPortalAppContent(
    viewModel: HomeUtensilsViewModel,
    uiState: com.example.ui.viewmodel.UiState,
    tasks: List<com.example.data.model.UtensilTaskEntity>,
    userProfile: com.example.data.model.UserProfileEntity?,
    submissions: List<com.example.data.model.TaskSubmissionEntity>,
    transactions: List<com.example.data.model.WalletTransactionEntity>,
    referrals: List<com.example.data.model.ReferralEntity>,
    notifications: List<com.example.ui.viewmodel.AppNotification>,
    activationCycles: List<ActivationCycleEntity>,
    p2pTransfers: List<P2PTransferEntity>,
    userWithdrawals: List<WithdrawalRequestEntity>,
    activeWithdrawalMethods: List<WithdrawalMethodEntity>,
    beneficiaryDetails: BeneficiaryDetailsEntity?,
    activeFundingTarget: ActivationFundingTargetEntity?,
    memberActivationRequests: List<AgentActivationRequestEntity>,
    allWorkingFloats: List<AgentWorkingFloatEntity>,
    liveActivityFeed: List<com.example.data.model.LiveActivityEvent> = emptyList(),
    memberAgentApplication: AgentApplicationEntity? = null,
    snackbarHostState: SnackbarHostState
) {
    // Handle back button hierarchically inside Member Portal
    BackHandler(
        enabled = uiState.activeTaskForSubmission != null ||
                (uiState.currentTab == AppNavTab.MORE && uiState.moreSection != MoreSection.MENU) ||
                uiState.currentTab != AppNavTab.HOME
    ) {
        if (uiState.activeTaskForSubmission != null) {
            viewModel.closeTaskDetail()
        } else if (uiState.currentTab == AppNavTab.MORE && uiState.moreSection != MoreSection.MENU) {
            viewModel.closeMoreSection()
        } else if (uiState.currentTab != AppNavTab.HOME) {
            viewModel.selectTab(AppNavTab.HOME)
        }
    }

    Scaffold(
        topBar = {
            if (uiState.currentTab != AppNavTab.HOME) {
                HomeUtensilsTopBar(
                    district = userProfile?.district ?: "Kampala Central",
                    unreadNotificationsCount = notifications.count { !it.isRead },
                    onNotificationClick = {
                        viewModel.selectTab(AppNavTab.MORE)
                        viewModel.navigateToMoreSection(MoreSection.NOTIFICATIONS)
                    }
                )
            }
        },
        bottomBar = {
            HomeUtensilsBottomNav(
                currentTab = uiState.currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppNavTab.HOME -> {
                    val approvedCount = submissions.count { it.status == "APPROVED" }
                    HomeScreen(
                        userProfile = userProfile,
                        tasks = tasks,
                        completedTaskCount = 12 + approvedCount,
                        taskIncomeUgx = userProfile?.walletBalanceUgx ?: 18500L,
                        onNavigateTab = { viewModel.selectTab(it) },
                        onTaskClicked = { viewModel.openTaskDetail(it) },
                        onOpenGiftCode = {
                            viewModel.selectTab(AppNavTab.MORE)
                            viewModel.navigateToMoreSection(MoreSection.GIFT_CODE)
                        },
                        onNotificationClick = {
                            viewModel.selectTab(AppNavTab.MORE)
                            viewModel.navigateToMoreSection(MoreSection.NOTIFICATIONS)
                        },
                        onLogoutClick = { viewModel.logout() },
                        onUpgradeClick = { viewModel.setShowUpgradeDialog(true) },
                        onReactivateClick = { viewModel.setShowReactivateDialog(true) },
                        onToggleSimulateExpiry = { viewModel.toggleSimulateExpiry() },
                        unreadNotificationsCount = notifications.count { !it.isRead },
                        activeActivationRequests = memberActivationRequests,
                        onFindAgentClick = { viewModel.openFindAgentDialog() },
                        onReportPaymentClick = { viewModel.openReportPaymentDialog(it.id) },
                        onReportIssueClick = { viewModel.openPaymentIssueDialog(it.id) },
                        agentApplication = memberAgentApplication,
                        onApplyAgentClick = { viewModel.requestOpenAgentApplication() },
                        onOpenAgentPortal = { viewModel.openAgentPortalFromMemberApp() },
                        liveActivityEvents = liveActivityFeed
                    )
                }

                AppNavTab.TASKS -> {
                    TasksScreen(
                        tasks = tasks,
                        submissions = submissions,
                        userProfile = userProfile,
                        selectedCategory = uiState.selectedCategory,
                        tasksTab = uiState.tasksTab,
                        isSubmitting = uiState.isSubmitting,
                        lastTaskResult = uiState.lastTaskResult,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onTasksTabSelected = { viewModel.setTasksTab(it) },
                        onAnswerSelected = { task, answer -> viewModel.submitYesNoTask(task, answer) },
                        onDismissTaskResult = { viewModel.dismissTaskResult() },
                        onReloadDemoTasks = { viewModel.reloadDemoTasks() }
                    )
                }

                AppNavTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        showDistrictDialog = uiState.showDistrictDialog,
                        onShowDistrictDialog = { viewModel.setShowDistrictDialog(it) },
                        onDistrictSelected = { viewModel.updateDistrict(it) },
                        onToggleRole = { viewModel.toggleRole() }
                    )
                }

                AppNavTab.WALLET -> {
                    WalletScreen(
                        userProfile = userProfile,
                        transactions = transactions,
                        activationCycles = activationCycles,
                        p2pTransfers = p2pTransfers,
                        userWithdrawals = userWithdrawals,
                        activeFundingTarget = activeFundingTarget,
                        onWithdrawClick = { viewModel.setShowWithdrawDialog(true) },
                        onTransferClick = { viewModel.setShowTransferDialog(true) },
                        onUpgradeClick = { viewModel.setShowUpgradeDialog(true) },
                        onReactivateClick = { viewModel.setShowReactivateDialog(true) },
                        onTransactionClick = { viewModel.openTransactionReceipt(it) }
                    )
                }

                AppNavTab.MORE -> {
                    MoreScreen(
                        uiState = uiState,
                        userProfile = userProfile,
                        notifications = notifications,
                        transactions = transactions,
                        tasks = tasks,
                        submissions = submissions,
                        referrals = referrals,
                        onNavigateSection = { viewModel.navigateToMoreSection(it) },
                        onBackToMenu = { viewModel.closeMoreSection() },
                        onMarkNotificationsRead = { viewModel.markNotificationsAsRead() },
                        onRedeemGiftCode = { viewModel.redeemGiftCode(it) },
                        onGiftCodeInputChange = { viewModel.setGiftCodeInput(it) },
                        onToggleBiometrics = { viewModel.toggleBiometrics() },
                        onTogglePinLock = { viewModel.togglePinLock() },
                        onSupportSubjectChange = { viewModel.setSupportSubject(it) },
                        onSupportMessageChange = { viewModel.setSupportMessage(it) },
                        onSendSupportTicket = { viewModel.sendSupportTicket() },
                        onShowLogoutDialog = { viewModel.setShowLogoutDialog(it) },
                        onConfirmLogout = { viewModel.confirmLogout() },
                        onShowDistrictDialog = { viewModel.setShowDistrictDialog(it) },
                        onDistrictSelected = { viewModel.updateDistrict(it) },
                        onToggleRole = { viewModel.toggleRole() },
                        onApproveSubmission = { viewModel.approveSubmission(it) },
                        onRejectSubmission = { viewModel.rejectSubmission(it) },
                        onToggleTaskStatus = { viewModel.toggleTaskStatus(it) },
                        onCreateTask = { title, cat, reward, desc, inst, opts ->
                            viewModel.createNewTask(title, cat, reward, desc, inst, opts)
                        },
                        onShowCreateTaskDialog = { viewModel.setShowCreateTaskDialog(it) },
                        onAdminSubTabChanged = { viewModel.setAdminSubTab(it) },
                        agentApplication = memberAgentApplication,
                        onApplyAgentClick = { viewModel.requestOpenAgentApplication() },
                        onOpenAgentPortal = { viewModel.openAgentPortalFromMemberApp() },
                        onFindAgentClick = { viewModel.openFindAgentDialog() },
                        onOpenUpgrade = { viewModel.setShowUpgradeDialog(true) },
                        onOpenReactivate = { viewModel.setShowReactivateDialog(true) },
                        onOpenP2P = { viewModel.setShowTransferDialog(true) },
                        onOpenWithdraw = { viewModel.setShowWithdrawDialog(true) },
                        onClaimReferralCode = { viewModel.claimReferralCode(it) },
                        onSimulateReferralInvite = { name, phone, district, pkg ->
                            viewModel.simulateNewFriendInvite(name, phone, district, pkg)
                        }
                    )
                }
            }

            // Task Detail / Submission Modal Dialog
            uiState.activeTaskForSubmission?.let { task ->
                TaskSubmissionDialog(
                    task = task,
                    selectedAnswer = uiState.selectedAnswer,
                    userNotes = uiState.userNotes,
                    isSubmitting = uiState.isSubmitting,
                    onAnswerSelected = { viewModel.setSelectedAnswer(it) },
                    onNotesChanged = { viewModel.setUserNotes(it) },
                    onSubmit = { viewModel.submitCurrentTask() },
                    onDismiss = { viewModel.closeTaskDetail() }
                )
            }

            // Newly Registered Member ID Welcome Dialog
            uiState.newlyRegisteredMemberId?.let { memberId ->
                AlertDialog(
                    onDismissRequest = { viewModel.dismissRegisteredDialog() },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(StatusGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusGreenText,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Registration Successful!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                        }
                    },
                    text = {
                        Column {
                            Text(
                                text = "Welcome to HomeUtensils! Your unique non-sequential Member ID has been generated:",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = DeepTealContainer)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "MEMBER ID",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
                                    Text(
                                        text = memberId,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepTealDark
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "A welcome bonus of UGX 5,000 has been credited to your wallet balance. You can now start reviewing household utensils and earn UGX 500 per task.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.dismissRegisteredDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dialog_dismiss_member_id")
                        ) {
                            Text("Start Reviewing Utensils", fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = WarmWhite,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Package Management Dialogs (Upgrade, Reactivate, Withdraw, P2P Transfer)
            if (uiState.showUpgradeDialog) {
                UpgradePackageDialog(
                    currentPackageName = userProfile?.packageName ?: "STANDARD PACKAGE",
                    onDismiss = { viewModel.setShowUpgradeDialog(false) },
                    onUpgrade = { viewModel.upgradePackage(it) }
                )
            }

            if (uiState.showReactivateDialog) {
                ReactivatePackageDialog(
                    currentPackageName = userProfile?.packageName ?: "STANDARD PACKAGE",
                    onDismiss = { viewModel.setShowReactivateDialog(false) },
                    onReactivate = { viewModel.reactivatePackage(it) }
                )
            }

            if (uiState.showWithdrawDialog) {
                WithdrawDialog(
                    isExpired = userProfile?.isPackageExpired == true,
                    availableBalanceUgx = userProfile?.walletBalanceUgx ?: 0L,
                    activeMethods = activeWithdrawalMethods,
                    beneficiaryDetails = beneficiaryDetails,
                    pendingWithdrawal = userWithdrawals.firstOrNull { it.status == "PENDING" || it.status == "PROCESSING" },
                    isScheduleOpen = viewModel.isWithdrawalScheduleOpen(),
                    availabilityMessage = viewModel.getWithdrawalAvailability().message,
                    simulatedScheduleOverride = uiState.simulatedScheduleOverride,
                    onToggleSimulateSchedule = { viewModel.toggleSimulatedScheduleOverride() },
                    onRequestBeneficiaryHelp = { viewModel.setShowBeneficiaryHelpDialog(true) },
                    onDismiss = { viewModel.setShowWithdrawDialog(false) },
                    onWithdraw = { amt, methodId, account, name, pin ->
                        viewModel.requestWithdrawal(amt, methodId, account, name, pin)
                    },
                    onReactivateClick = {
                        viewModel.setShowWithdrawDialog(false)
                        viewModel.setShowReactivateDialog(true)
                    }
                )
            }

            if (uiState.showTransferDialog) {
                P2PTransferDialog(
                    isExpired = userProfile?.isPackageExpired == true,
                    availableBalanceUgx = userProfile?.walletBalanceUgx ?: 0L,
                    onLookupRecipient = { viewModel.lookupRecipient(it) },
                    onDismiss = { viewModel.setShowTransferDialog(false) },
                    onTransfer = { recipientId, amt, pin, isAct, pkg ->
                        viewModel.sendP2PTransfer(recipientId, amt, pin, isAct, pkg)
                    },
                    onReactivateClick = {
                        viewModel.setShowTransferDialog(false)
                        viewModel.setShowReactivateDialog(true)
                    }
                )
            }

            if (uiState.showBeneficiaryHelpDialog) {
                BeneficiaryHelpDialog(
                    currentBeneficiary = beneficiaryDetails,
                    onDismiss = { viewModel.setShowBeneficiaryHelpDialog(false) },
                    onSaveUpdate = { newAccount, newName, reason ->
                        viewModel.updateBeneficiaryDetails(
                            newAccount = newAccount,
                            newName = newName,
                            methodId = beneficiaryDetails?.methodId ?: "METHOD-MOMO",
                            methodName = beneficiaryDetails?.methodName ?: "Mobile Money",
                            reason = reason
                        )
                    }
                )
            }

            // Agent Payment Protection: Find Agent Dialog
            if (uiState.showFindAgentDialog) {
                FindAgentDialog(
                    initialPackageName = userProfile?.packageName ?: "STARTER PACKAGE",
                    availableAgents = allWorkingFloats,
                    onDismiss = { viewModel.closeFindAgentDialog() },
                    onRequestAgent = { agentId, pkg, region ->
                        viewModel.requestAgentActivation(agentId, pkg, region)
                    }
                )
            }

            // Agent Application Dialog for eligible PLUS members
            if (uiState.showAgentApplicationDialog) {
                AgentApplicationDialog(
                    userProfile = userProfile,
                    onDismiss = { viewModel.setShowAgentApplicationDialog(false) },
                    onSubmit = { fullName, phone, region, idType, idNumber, idDocUri, profilePhotoUri, reason, agreed ->
                        viewModel.submitAgentApplication(
                            fullName = fullName,
                            phone = phone,
                            region = region,
                            idType = idType,
                            idNumber = idNumber,
                            idDocUri = idDocUri,
                            profilePhotoUri = profilePhotoUri,
                            reason = reason,
                            agreed = agreed
                        )
                    }
                )
            }

            // Agent Payment Protection: Report Payment Dialog
            val reportingRequest = memberActivationRequests.firstOrNull { it.id == uiState.activePaymentRequestId }
            if (uiState.showReportPaymentDialog && reportingRequest != null) {
                ReportPaymentDialog(
                    request = reportingRequest,
                    onDismiss = { viewModel.closeReportPaymentDialog() },
                    onSubmit = { method, ref, note, proofUri ->
                        viewModel.submitMemberPaymentReport(
                            requestId = reportingRequest.id,
                            method = method,
                            reference = ref,
                            note = note,
                            proofUri = proofUri
                        )
                    }
                )
            }

            // Agent Payment Protection: Report Payment Issue Dialog
            val issueRequest = memberActivationRequests.firstOrNull { it.id == uiState.activePaymentRequestId }
            if (uiState.showPaymentIssueDialog && issueRequest != null) {
                ReportPaymentIssueDialog(
                    request = issueRequest,
                    onDismiss = { viewModel.closePaymentIssueDialog() },
                    onSubmitIssue = { reason, statement ->
                        viewModel.submitMemberPaymentIssue(
                            requestId = issueRequest.id,
                            reason = reason,
                            statement = statement
                        )
                    }
                )
            }

            // Part B: Visual Transaction Receipt Dialog
            if (uiState.selectedTransactionForReceipt != null) {
                VisualTransactionReceiptDialog(
                    transaction = uiState.selectedTransactionForReceipt!!,
                    onDismiss = { viewModel.closeTransactionReceipt() }
                )
            }
        }
    }
}
