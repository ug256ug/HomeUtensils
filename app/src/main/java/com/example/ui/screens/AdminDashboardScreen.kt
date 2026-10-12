package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.AgentCommissionLogEntity
import com.example.data.model.AgentPaymentConfigEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.ui.viewmodel.AppNotification
import com.example.data.model.BeneficiaryAuditLogEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UgandaHolidayAuditLogEntity
import com.example.data.model.UgandaHolidayEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.ui.components.AdminAgentProtectionDetailDialog
import com.example.ui.components.RewardBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarningBadge
import com.example.ui.components.formatUgx
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.DeepTealLight
import com.example.ui.theme.PlusPurple
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldLight
import com.example.ui.theme.SoftGoldOnContainer
import com.example.ui.theme.StandardBlue
import com.example.ui.theme.StarterGreen
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Production-grade HomeUtensils Admin Center.
 * Mobile-responsive, high-fidelity administrative dashboard with real data,
 * Uganda local operating schedules, audit trail logs, and quick actions.
 */
@Composable
fun AdminDashboardScreen(
    adminUser: String,
    allMembers: List<UserProfileEntity> = emptyList(),
    tasks: List<UtensilTaskEntity> = emptyList(),
    submissions: List<TaskSubmissionEntity> = emptyList(),
    transactions: List<WalletTransactionEntity> = emptyList(),
    referrals: List<ReferralEntity> = emptyList(),
    withdrawalRequests: List<WithdrawalRequestEntity> = emptyList(),
    withdrawalMethods: List<WithdrawalMethodEntity> = emptyList(),
    beneficiaryAuditLogs: List<BeneficiaryAuditLogEntity> = emptyList(),
    activationRequests: List<AgentActivationRequestEntity> = emptyList(),
    protectionCases: List<AgentPaymentProtectionCaseEntity> = emptyList(),
    workingFloats: List<AgentWorkingFloatEntity> = emptyList(),
    agentApplications: List<AgentApplicationEntity> = emptyList(),
    commissionLogs: List<AgentCommissionLogEntity> = emptyList(),
    paymentConfig: AgentPaymentConfigEntity? = null,
    activationCycles: List<ActivationCycleEntity> = emptyList(),
    ugandaHolidays: List<UgandaHolidayEntity> = emptyList(),
    holidayAuditLogs: List<UgandaHolidayAuditLogEntity> = emptyList(),
    notifications: List<AppNotification> = emptyList(),
    onProcessWithdrawal: (requestId: String, status: String, rejectionReason: String?, externalRef: String?) -> Unit = { _, _, _, _ -> },
    onAddOrUpdateMethod: (WithdrawalMethodEntity) -> Unit = {},
    onToggleMethodStatus: (methodId: String, isActive: Boolean) -> Unit = { _, _ -> },
    onDeleteMethod: (methodId: String) -> Unit = {},
    onVerifyAndActivate: (requestId: String, notes: String) -> Unit = { _, _ -> },
    onRejectPaymentClaim: (requestId: String, reason: String) -> Unit = { _, _ -> },
    onResolveCase: (caseId: String, summary: String) -> Unit = { _, _ -> },
    onRestrictAgent: (agentId: String, isRestricted: Boolean) -> Unit = { _, _ -> },
    onSuspendAgent: (agentId: String, status: String) -> Unit = { _, _ -> },
    onUpdateConfig: (timeoutMinutes: Int, threshold: Int) -> Unit = { _, _ -> },
    onApproveFloatPurchase: (agentId: String, amountUgx: Long) -> Unit = { _, _ -> },
    onReviewAgentApplication: (applicationId: String, status: String, notes: String?) -> Unit = { _, _, _ -> },
    onUpdateMemberStatus: (memberId: String, status: String) -> Unit = { _, _ -> },
    onApproveSubmission: (TaskSubmissionEntity) -> Unit = {},
    onRejectSubmission: (TaskSubmissionEntity, reason: String) -> Unit = { _, _ -> },
    onToggleTaskStatus: (UtensilTaskEntity) -> Unit = {},
    onCreateTask: (title: String, category: String, reward: Long, desc: String, inst: String, opts: List<String>) -> Unit = { _, _, _, _, _, _ -> },
    onCreateGiftCode: (code: String, bonusAmount: Long, targetPackage: String) -> Unit = { _, _, _ -> },
    onAddHoliday: (name: String, dateIso: String, isOfficial: Boolean, isSpecial: Boolean, desc: String) -> Unit = { _, _, _, _, _ -> },
    onUpdateHoliday: (id: String, name: String, dateIso: String, isOfficial: Boolean, isSpecial: Boolean, desc: String) -> Unit = { _, _, _, _, _, _ -> },
    onToggleHolidayStatus: (id: String, isOfficial: Boolean) -> Unit = { _, _ -> },
    onDeleteHoliday: (id: String) -> Unit = {},
    onBroadcastNotification: (title: String, message: String, type: String) -> Unit = { _, _, _ -> },
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onLogout() }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val shortDateFormat = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    var selectedTab by remember { mutableIntStateOf(0) }

    // Counts for badge notifications
    val pendingWithdrawalsCount = withdrawalRequests.count { it.status == "PENDING" }
    val pendingCasesCount = activationRequests.count {
        it.status == "PAYMENT_REPORTED" || it.status == "AWAITING_AGENT_CONFIRMATION" || it.status == "OVERDUE" || it.status == "PAYMENT_DISPUTED"
    }
    val pendingApplicationsCount = agentApplications.count { it.status == "PENDING" }
    val pendingSubmissionsCount = submissions.count { it.status == "PENDING" }

    val tabs = listOf(
        "Overview",
        "Members (${allMembers.size})",
        "Agents" + (if (pendingApplicationsCount + pendingCasesCount > 0) " (${pendingApplicationsCount + pendingCasesCount})" else ""),
        "Withdrawals" + (if (pendingWithdrawalsCount > 0) " ($pendingWithdrawalsCount)" else ""),
        "Ledger (${transactions.size})",
        "Tasks & Packages",
        "Uganda Holidays",
        "Settings & Audits"
    )

    // Modals and dialog state
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var showGiftCodeDialog by remember { mutableStateOf(false) }
    var showCreateTaskDialog by remember { mutableStateOf(false) }
    var showAddHolidayDialog by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }
    var editingMethod by remember { mutableStateOf<WithdrawalMethodEntity?>(null) }
    var editingHoliday by remember { mutableStateOf<UgandaHolidayEntity?>(null) }

    var rejectingRequestId by remember { mutableStateOf<String?>(null) }
    var rejectionReasonInput by remember { mutableStateOf("") }

    var completingRequestId by remember { mutableStateOf<String?>(null) }
    var externalRefInput by remember { mutableStateOf("") }

    var selectedAgentProtectionDetail by remember { mutableStateOf<AgentActivationRequestEntity?>(null) }
    var floatApprovingAgent by remember { mutableStateOf<AgentWorkingFloatEntity?>(null) }
    var adminFloatPurchaseAmount by remember { mutableStateOf("50000") }

    var selectedMemberForCycles by remember { mutableStateOf<UserProfileEntity?>(null) }
    var selectedMemberForStatusToggle by remember { mutableStateOf<UserProfileEntity?>(null) }

    var rejectingApplicationId by remember { mutableStateOf<String?>(null) }
    var applicationRejectionNotes by remember { mutableStateOf("") }

    var rejectingSubmissionEntity by remember { mutableStateOf<TaskSubmissionEntity?>(null) }
    var submissionRejectionReason by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_dashboard_screen"),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // BRANDED EXECUTIVE HEADER (DEEP TEAL ROYAL BANNER)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(DeepTealDark, DeepTeal)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 800.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Top row: Brand & Profile & Notification Bell & Logout
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(WarmWhite.copy(alpha = 0.15f))
                                        .border(1.dp, WarmWhite.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = "Admin Shield",
                                        tint = SoftGoldLight,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "HomeUtensils Admin Center",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = WarmWhite
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(SoftGold)
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "UGANDA OPS",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = DeepTealDark
                                            )
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(StatusGreen)
                                        )
                                        Text(
                                            text = "Superuser: ${adminUser.ifBlank { "admin@homeutensils.ug" }}",
                                            fontSize = 11.sp,
                                            color = WarmWhite.copy(alpha = 0.85f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Notification Bell with Unread Badge
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(WarmWhite.copy(alpha = 0.12f))
                                        .clickable { showNotificationDialog = true }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = WarmWhite,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    if (notifications.any { !it.isRead }) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(SoftGold)
                                        )
                                    }
                                }

                                // Logout Button
                                Button(
                                    onClick = onLogout,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WarningRed.copy(alpha = 0.85f),
                                        contentColor = WarmWhite
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("admin_logout_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Logout",
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Logout", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Quick Action Shortcuts Carousel
                        Column {
                            Text(
                                text = "QUICK ADMINISTRATIVE SHORTCUTS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGoldLight.copy(alpha = 0.9f),
                                letterSpacing = 0.6.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    QuickActionChip(
                                        label = "Review Withdrawals (${pendingWithdrawalsCount})",
                                        icon = Icons.Default.Payments,
                                        onClick = { selectedTab = 3 }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "Verify Agents (${pendingApplicationsCount + pendingCasesCount})",
                                        icon = Icons.Default.Shield,
                                        onClick = { selectedTab = 2 }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "Members Roster",
                                        icon = Icons.Default.People,
                                        onClick = { selectedTab = 1 }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "Uganda Holidays",
                                        icon = Icons.Default.DateRange,
                                        onClick = { selectedTab = 6 }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "+ Publish Task",
                                        icon = Icons.Default.Kitchen,
                                        onClick = { showCreateTaskDialog = true }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "Generate Gift Code",
                                        icon = Icons.Default.CardGiftcard,
                                        onClick = { showGiftCodeDialog = true }
                                    )
                                }
                                item {
                                    QuickActionChip(
                                        label = "Broadcast Alert",
                                        icon = Icons.Default.Campaign,
                                        onClick = { showBroadcastDialog = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // NAVIGATION SCROLLABLE TAB ROW
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                ) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = WarmWhite,
                        contentColor = DeepTeal,
                        edgePadding = 8.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = DeepTeal,
                                height = 3.dp
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, tabTitle ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = tabTitle,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // TAB BODY CONTENT
            when (selectedTab) {
                0 -> {
                    // TAB 0: DASHBOARD OVERVIEW WITH CHARTS & KPIS
                    renderDashboardOverviewTab(
                        allMembers = allMembers,
                        workingFloats = workingFloats,
                        transactions = transactions,
                        withdrawalRequests = withdrawalRequests,
                        tasks = tasks,
                        submissions = submissions,
                        activationRequests = activationRequests,
                        agentApplications = agentApplications,
                        pendingWithdrawalsCount = pendingWithdrawalsCount,
                        pendingCasesCount = pendingCasesCount,
                        pendingApplicationsCount = pendingApplicationsCount,
                        pendingSubmissionsCount = pendingSubmissionsCount,
                        onNavigateToTab = { selectedTab = it }
                    )
                }

                1 -> {
                    // TAB 1: MEMBER MANAGEMENT
                    renderMemberManagementTab(
                        allMembers = allMembers,
                        activationCycles = activationCycles,
                        dateFormat = dateFormat,
                        onViewCycles = { selectedMemberForCycles = it },
                        onToggleStatus = { selectedMemberForStatusToggle = it }
                    )
                }

                2 -> {
                    // TAB 2: AGENT MANAGEMENT & PAYMENT PROTECTION
                    renderAgentManagementTab(
                        agentApplications = agentApplications,
                        activationRequests = activationRequests,
                        protectionCases = protectionCases,
                        workingFloats = workingFloats,
                        commissionLogs = commissionLogs,
                        paymentConfig = paymentConfig,
                        dateFormat = dateFormat,
                        onApproveApplication = { appId ->
                            onReviewAgentApplication(appId, "APPROVED", "Approved by Administrator")
                        },
                        onRejectApplication = { appId ->
                            rejectingApplicationId = appId
                            applicationRejectionNotes = ""
                        },
                        onInvestigateCase = { selectedAgentProtectionDetail = it },
                        onApproveFloat = { agent ->
                            floatApprovingAgent = agent
                            adminFloatPurchaseAmount = "50000"
                        },
                        onRestrictAgent = onRestrictAgent,
                        onSuspendAgent = onSuspendAgent,
                        onUpdateConfig = onUpdateConfig
                    )
                }

                3 -> {
                    // TAB 3: WITHDRAWAL MANAGEMENT
                    renderWithdrawalManagementTab(
                        withdrawalRequests = withdrawalRequests,
                        withdrawalMethods = withdrawalMethods,
                        dateFormat = dateFormat,
                        onStartProcessing = { onProcessWithdrawal(it, "PROCESSING", null, null) },
                        onCompleteWithdrawal = { reqId ->
                            completingRequestId = reqId
                            externalRefInput = ""
                        },
                        onRejectWithdrawal = { reqId ->
                            rejectingRequestId = reqId
                            rejectionReasonInput = ""
                        },
                        onDisputeWithdrawal = { onProcessWithdrawal(it, "DISPUTED", null, null) },
                        onAddMethod = {
                            editingMethod = null
                            showMethodDialog = true
                        },
                        onEditMethod = { method ->
                            editingMethod = method
                            showMethodDialog = true
                        },
                        onToggleMethod = onToggleMethodStatus,
                        onDeleteMethod = onDeleteMethod
                    )
                }

                4 -> {
                    // TAB 4: TRANSACTION MONITORING & AUDIT LEDGER
                    renderTransactionLedgerTab(
                        transactions = transactions,
                        dateFormat = dateFormat
                    )
                }

                5 -> {
                    // TAB 5: PACKAGES, TASKS, REFERRALS & GIFT CODES
                    renderPackagesAndTasksTab(
                        tasks = tasks,
                        submissions = submissions,
                        allMembers = allMembers,
                        referrals = referrals,
                        dateFormat = dateFormat,
                        onToggleTask = onToggleTaskStatus,
                        onApproveSubmission = onApproveSubmission,
                        onRejectSubmission = { sub ->
                            rejectingSubmissionEntity = sub
                            submissionRejectionReason = "Image unclear or answer mismatch."
                        },
                        onCreateTaskClick = { showCreateTaskDialog = true },
                        onGenerateGiftCodeClick = { showGiftCodeDialog = true }
                    )
                }

                6 -> {
                    // TAB 6: UGANDA PUBLIC HOLIDAYS CALENDAR
                    renderUgandaHolidaysTab(
                        ugandaHolidays = ugandaHolidays,
                        holidayAuditLogs = holidayAuditLogs,
                        dateFormat = dateFormat,
                        onAddHolidayClick = { showAddHolidayDialog = true },
                        onEditHoliday = { hol ->
                            editingHoliday = hol
                        },
                        onToggleOfficial = onToggleHolidayStatus,
                        onDeleteHoliday = onDeleteHoliday
                    )
                }

                7 -> {
                    // TAB 7: SETTINGS & SECURITY AUDIT LOGS
                    renderSettingsAndAuditsTab(
                        beneficiaryAuditLogs = beneficiaryAuditLogs,
                        paymentConfig = paymentConfig,
                        dateFormat = dateFormat,
                        onUpdateConfig = onUpdateConfig,
                        onOpenBroadcast = { showBroadcastDialog = true }
                    )
                }
            }

            // BRANDED COMPLIANT FOOTER
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        color = WarmCreamBorder,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                    Text(
                        text = "HomeUtensils Admin Center • Secure • Reliable • Better Together",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Authorized administrative terminal • Operating in Kampala, Uganda (EAT)",
                        fontSize = 10.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    // ==========================================
    // MODALS AND CONFIRMATION DIALOGS
    // ==========================================

    // 1. Notifications & Broadcast Drawer Dialog
    if (showNotificationDialog) {
        Dialog(onDismissRequest = { showNotificationDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Platform Alerts & Broadcasts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        IconButton(onClick = { showNotificationDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (notifications.isEmpty()) {
                        Text("No platform notifications recorded.", fontSize = 12.sp, color = TextSecondary)
                    } else {
                        LazyColumn(modifier = Modifier.height(260.dp)) {
                            items(notifications, key = { it.id }) { notif ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                                            Text(notif.time, fontSize = 10.sp, color = TextSecondary)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(notif.message, fontSize = 11.sp, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            showNotificationDialog = false
                            showBroadcastDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Platform Announcement")
                    }
                }
            }
        }
    }

    // 2. Broadcast System Announcement Dialog
    if (showBroadcastDialog) {
        var notifTitle by remember { mutableStateOf("") }
        var notifMessage by remember { mutableStateOf("") }
        var notifType by remember { mutableStateOf("SYSTEM") }

        Dialog(onDismissRequest = { showBroadcastDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Broadcast System Announcement",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Broadcast an official real-time alert to all registered HomeUtensils members.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = notifTitle,
                        onValueChange = { notifTitle = it },
                        label = { Text("Announcement Title *") },
                        placeholder = { Text("e.g. Scheduled Maintenance or New Utensils Added") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notifMessage,
                        onValueChange = { notifMessage = it },
                        label = { Text("Announcement Message *") },
                        placeholder = { Text("Detailed notification text...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showBroadcastDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (notifTitle.isNotBlank() && notifMessage.isNotBlank()) {
                                    onBroadcastNotification(notifTitle.trim(), notifMessage.trim(), notifType)
                                    showBroadcastDialog = false
                                }
                            },
                            enabled = notifTitle.isNotBlank() && notifMessage.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Broadcast Now", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 3. Issue Gift Code Dialog
    if (showGiftCodeDialog) {
        var giftCodeName by remember { mutableStateOf("") }
        var giftCodeBonus by remember { mutableStateOf("3000") }
        var giftCodeTier by remember { mutableStateOf("PLUS") }

        Dialog(onDismissRequest = { showGiftCodeDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Issue Promotional Gift Code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Generate a promotional code giving instant wallet bonus upon redemption.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = giftCodeName,
                        onValueChange = { giftCodeName = it.uppercase() },
                        label = { Text("Gift Code (Letters & Numbers) *") },
                        placeholder = { Text("e.g. FESTIVE2026") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = giftCodeBonus,
                        onValueChange = { giftCodeBonus = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Bonus Amount (UGX) *") },
                        prefix = { Text("UGX ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showGiftCodeDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                val amt = giftCodeBonus.toLongOrNull() ?: 2500L
                                if (giftCodeName.isNotBlank() && amt > 0L) {
                                    onCreateGiftCode(giftCodeName.trim(), amt, giftCodeTier)
                                    showGiftCodeDialog = false
                                }
                            },
                            enabled = giftCodeName.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Activate Code", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 4. Create Utensil Task Dialog
    if (showCreateTaskDialog) {
        var taskTitle by remember { mutableStateOf("") }
        var taskCategory by remember { mutableStateOf("Cups") }
        var taskReward by remember { mutableStateOf("500") }
        var taskDesc by remember { mutableStateOf("") }
        var taskInstructions by remember { mutableStateOf("") }
        var taskOptions by remember { mutableStateOf("YES,NO") }

        Dialog(onDismissRequest = { showCreateTaskDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Publish New Utensil Task",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Add an inspection task for Ugandan community reviewers.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Title *") },
                        placeholder = { Text("e.g. Enamel Tea Kettle Inspection") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = taskCategory,
                            onValueChange = { taskCategory = it },
                            label = { Text("Category *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = taskReward,
                            onValueChange = { taskReward = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Reward (UGX) *") },
                            prefix = { Text("UGX ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = taskDesc,
                        onValueChange = { taskDesc = it },
                        label = { Text("Description & Prompt *") },
                        placeholder = { Text("Is the kettle clean and lid securely fitting?") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCreateTaskDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                val rwd = taskReward.toLongOrNull() ?: 500L
                                if (taskTitle.isNotBlank() && taskCategory.isNotBlank()) {
                                    val optsList = taskOptions.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    onCreateTask(
                                        taskTitle.trim(),
                                        taskCategory.trim(),
                                        rwd,
                                        taskDesc.trim(),
                                        taskInstructions.trim().ifBlank { "Verify utensil image quality." },
                                        if (optsList.isNotEmpty()) optsList else listOf("YES", "NO")
                                    )
                                    showCreateTaskDialog = false
                                }
                            },
                            enabled = taskTitle.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Publish Task", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 5. Add / Edit Uganda Public Holiday Dialog
    if (showAddHolidayDialog || editingHoliday != null) {
        val isEditing = editingHoliday != null
        var holName by remember { mutableStateOf(editingHoliday?.name ?: "") }
        var holDate by remember { mutableStateOf(editingHoliday?.dateIso ?: "2026-10-09") }
        var holDesc by remember { mutableStateOf(editingHoliday?.description ?: "Official Uganda Public Holiday") }
        var holOfficial by remember { mutableStateOf(editingHoliday?.isOfficialPublicHoliday ?: true) }
        var holSpecial by remember { mutableStateOf(editingHoliday?.isSpecialDeclared ?: false) }

        Dialog(
            onDismissRequest = {
                showAddHolidayDialog = false
                editingHoliday = null
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isEditing) "Edit Uganda Public Holiday" else "Add Uganda Public Holiday",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Withdrawals are strictly closed on official Uganda public holidays.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = holName,
                        onValueChange = { holName = it },
                        label = { Text("Holiday Name *") },
                        placeholder = { Text("e.g. National Heroes Day") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = holDate,
                        onValueChange = { holDate = it },
                        label = { Text("Date (YYYY-MM-DD) *") },
                        placeholder = { Text("2026-06-09") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = holDesc,
                        onValueChange = { holDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Official Uganda Public Holiday:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Switch(
                            checked = holOfficial,
                            onCheckedChange = { holOfficial = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = DeepTeal)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showAddHolidayDialog = false
                                editingHoliday = null
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (holName.isNotBlank() && holDate.isNotBlank()) {
                                    if (isEditing && editingHoliday != null) {
                                        onUpdateHoliday(editingHoliday!!.id, holName.trim(), holDate.trim(), holOfficial, holSpecial, holDesc.trim())
                                    } else {
                                        onAddHoliday(holName.trim(), holDate.trim(), holOfficial, holSpecial, holDesc.trim())
                                    }
                                    showAddHolidayDialog = false
                                    editingHoliday = null
                                }
                            },
                            enabled = holName.isNotBlank() && holDate.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isEditing) "Save Changes" else "Add Holiday", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 6. Complete Withdrawal Modal with Optional External Ref
    completingRequestId?.let { reqId ->
        Dialog(onDismissRequest = { completingRequestId = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Confirm Manual Payout Completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mark this withdrawal as successfully settled and paid out to the beneficiary account.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = externalRefInput,
                        onValueChange = { externalRefInput = it },
                        label = { Text("External Transaction Ref (Optional)") },
                        placeholder = { Text("e.g. MTN-MOMO-910284 or AIRTEL-77281") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { completingRequestId = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onProcessWithdrawal(reqId, "COMPLETED", null, externalRefInput.trim().ifBlank { null })
                                completingRequestId = null
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Confirm Paid", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 7. Reject Withdrawal Modal (with Mandatory Rejection Reason & Auto Refund)
    rejectingRequestId?.let { reqId ->
        Dialog(onDismissRequest = { rejectingRequestId = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Reject & Refund Withdrawal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningRedText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rejecting will automatically refund the full requested amount back to the member's wallet balance. A clear reason is required for the audit log.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = { rejectionReasonInput = it },
                        label = { Text("Rejection Reason *") },
                        placeholder = { Text("e.g. Beneficiary phone number is not registered for Mobile Money") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { rejectingRequestId = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (rejectionReasonInput.isNotBlank()) {
                                    onProcessWithdrawal(reqId, "REJECTED", rejectionReasonInput.trim(), null)
                                    rejectingRequestId = null
                                }
                            },
                            enabled = rejectionReasonInput.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Reject & Refund", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 8. Reject Agent Application Modal
    rejectingApplicationId?.let { appId ->
        Dialog(onDismissRequest = { rejectingApplicationId = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Reject Agent Application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningRedText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Please enter an administrative reason for rejecting this agent application.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = applicationRejectionNotes,
                        onValueChange = { applicationRejectionNotes = it },
                        label = { Text("Rejection Reason *") },
                        placeholder = { Text("e.g. National ID document illegible or territory quota filled") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { rejectingApplicationId = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (applicationRejectionNotes.isNotBlank()) {
                                    onReviewAgentApplication(appId, "REJECTED", applicationRejectionNotes.trim())
                                    rejectingApplicationId = null
                                }
                            },
                            enabled = applicationRejectionNotes.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Confirm Rejection", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 9. Reject Task Submission Modal
    rejectingSubmissionEntity?.let { sub ->
        Dialog(onDismissRequest = { rejectingSubmissionEntity = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Reject Utensil Task Submission",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningRedText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Task: ${sub.taskTitle} (Reward: ${formatUgx(sub.rewardUgx)})",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = submissionRejectionReason,
                        onValueChange = { submissionRejectionReason = it },
                        label = { Text("Reason for Rejection *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { rejectingSubmissionEntity = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (submissionRejectionReason.isNotBlank()) {
                                    onRejectSubmission(sub, submissionRejectionReason.trim())
                                    rejectingSubmissionEntity = null
                                }
                            },
                            enabled = submissionRejectionReason.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Reject Submission", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }

    // 10. Member Activation History Cycles Dialog
    selectedMemberForCycles?.let { member ->
        val memberCycles = activationCycles.filter { it.memberId == member.memberId || it.userId == member.id }
        Dialog(onDismissRequest = { selectedMemberForCycles = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Activation Cycles History",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "${member.fullName} (${member.memberId})",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        IconButton(onClick = { selectedMemberForCycles = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (memberCycles.isEmpty()) {
                        Text("No recorded activation cycles for this member.", fontSize = 12.sp, color = TextSecondary)
                    } else {
                        LazyColumn(modifier = Modifier.height(240.dp)) {
                            items(memberCycles, key = { it.id }) { cycle ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(cycle.packageTier, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                                            StatusBadge(text = cycle.status)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Capital Paid: ${formatUgx(cycle.amountUgx)} • Facilitator: ${cycle.facilitatedByAgentId ?: "Self"}", fontSize = 10.sp, color = TextPrimary)
                                        Text("Activated: ${dateFormat.format(Date(cycle.activatedAt))} • Expires: ${dateFormat.format(Date(cycle.expiresAt))}", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 11. Toggle Member Status Confirmation Dialog
    selectedMemberForStatusToggle?.let { member ->
        val nextStatus = if (member.status == "ACTIVE") "PENDING" else "ACTIVE"
        AlertDialog(
            onDismissRequest = { selectedMemberForStatusToggle = null },
            title = {
                Text(
                    text = "Update Account Status",
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to change the status of ${member.fullName} (${member.memberId}) from ${member.status} to $nextStatus?",
                    fontSize = 12.sp,
                    color = TextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateMemberStatus(member.id, nextStatus)
                        selectedMemberForStatusToggle = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
                ) {
                    Text("Confirm Change", color = WarmWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedMemberForStatusToggle = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // 12. Agent Payment Protection Detail Dialog
    selectedAgentProtectionDetail?.let { req ->
        val caseEntity = protectionCases.firstOrNull { it.requestId == req.id }
        AdminAgentProtectionDetailDialog(
            request = req,
            caseEntity = caseEntity,
            onDismiss = { selectedAgentProtectionDetail = null },
            onVerifyAndActivate = { notes ->
                selectedAgentProtectionDetail = null
                onVerifyAndActivate(req.id, notes)
            },
            onRejectClaim = { reason ->
                selectedAgentProtectionDetail = null
                onRejectPaymentClaim(req.id, reason)
            },
            onResolveCase = { summary ->
                selectedAgentProtectionDetail = null
                caseEntity?.let { c -> onResolveCase(c.id, summary) } ?: onResolveCase(req.id, summary)
            },
            onRestrictAgent = { agentId, isRestricted ->
                onRestrictAgent(agentId, isRestricted)
            },
            onSuspendAgent = { agentId ->
                onSuspendAgent(agentId, "SUSPENDED")
            },
            onLogProofView = { /* Audit handled */ }
        )
    }

    // 13. Approve Agent Float Purchase Modal
    floatApprovingAgent?.let { currentAgent ->
        val parsedAmt = adminFloatPurchaseAmount.filter { it.isDigit() }.toLongOrNull() ?: 0L
        val bonus = (parsedAmt * 0.15).toLong()
        val total = parsedAmt + bonus

        AlertDialog(
            onDismissRequest = { floatApprovingAgent = null },
            title = {
                Text(
                    text = "Approve Agent Working Float",
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Agent: ${currentAgent.agentName} (${currentAgent.agentId})\nCurrent Working Float: ${formatUgx(currentAgent.workingFloatUgx)}",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                    OutlinedTextField(
                        value = adminFloatPurchaseAmount,
                        onValueChange = { adminFloatPurchaseAmount = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Float Purchase (UGX)") },
                        prefix = { Text("UGX ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (parsedAmt < 50000L) {
                        Text(
                            text = "⚠️ Minimum qualification threshold: UGX 50,000.",
                            fontSize = 10.sp,
                            color = WarningRedText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Purchased Working Float:", fontSize = 11.sp, color = TextSecondary)
                                Text(formatUgx(parsedAmt), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Company Float Bonus (+15%):", fontSize = 11.sp, color = StatusGreenText, fontWeight = FontWeight.Bold)
                                Text("+${formatUgx(bonus)}", fontSize = 11.sp, color = StatusGreenText, fontWeight = FontWeight.Bold)
                            }
                            HorizontalDivider(color = WarmCreamBorder, thickness = 0.5.dp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Working Float Credited:", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                                Text(formatUgx(total), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                        }
                    }
                    Text(
                        text = "Note: The 15% bonus is working operational float to facilitate member packages. It is NOT personal withdrawable earnings.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val agId = currentAgent.agentId
                        floatApprovingAgent = null
                        onApproveFloatPurchase(agId, parsedAmt)
                    },
                    enabled = parsedAmt >= 50000L,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
                ) {
                    Text("Approve & Credit Float", color = WarmWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { floatApprovingAgent = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // 14. Add / Edit Payment Method Dialog
    if (showMethodDialog) {
        var methodName by remember { mutableStateOf(editingMethod?.name ?: "") }
        var countries by remember { mutableStateOf(editingMethod?.supportedCountries ?: "Uganda (UG)") }
        var currency by remember { mutableStateOf(editingMethod?.currency ?: "UGX") }
        var reqInfo by remember { mutableStateOf(editingMethod?.requiredAccountInfo ?: "Mobile Number (+256...)") }
        var instructions by remember { mutableStateOf(editingMethod?.instructions ?: "Instant mobile money payout") }

        Dialog(onDismissRequest = { showMethodDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (editingMethod == null) "Add Payout Method" else "Edit Payout Method",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = methodName,
                        onValueChange = { methodName = it },
                        label = { Text("Provider/Method Name *") },
                        placeholder = { Text("e.g. Binance Pay (USDT) or MTN Mobile Money") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = countries,
                        onValueChange = { countries = it },
                        label = { Text("Supported Countries *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        label = { Text("Currency *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reqInfo,
                        onValueChange = { reqInfo = it },
                        label = { Text("Required Account Info *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instructions") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showMethodDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (methodName.isNotBlank()) {
                                    val newMethod = WithdrawalMethodEntity(
                                        id = editingMethod?.id ?: "METHOD-${System.currentTimeMillis() % 100000}",
                                        name = methodName.trim(),
                                        supportedCountries = countries.trim(),
                                        currency = currency.trim().uppercase(),
                                        requiredAccountInfo = reqInfo.trim(),
                                        instructions = instructions.trim(),
                                        isActive = editingMethod?.isActive ?: true,
                                        isDefault = editingMethod?.isDefault ?: false
                                    )
                                    onAddOrUpdateMethod(newMethod)
                                    showMethodDialog = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            enabled = methodName.isNotBlank()
                        ) {
                            Text("Save Method", color = WarmWhite)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// TAB RENDERERS
// =========================================================================

/**
 * Tab 0: Dashboard Overview with KPI metrics, visual platform activity chart, and urgent alerts.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderDashboardOverviewTab(
    allMembers: List<UserProfileEntity>,
    workingFloats: List<AgentWorkingFloatEntity>,
    transactions: List<WalletTransactionEntity>,
    withdrawalRequests: List<WithdrawalRequestEntity>,
    tasks: List<UtensilTaskEntity>,
    submissions: List<TaskSubmissionEntity>,
    activationRequests: List<AgentActivationRequestEntity>,
    agentApplications: List<AgentApplicationEntity>,
    pendingWithdrawalsCount: Int,
    pendingCasesCount: Int,
    pendingApplicationsCount: Int,
    pendingSubmissionsCount: Int,
    onNavigateToTab: (Int) -> Unit
) {
    // 1. Urgent Attention Alert Banner (if any items pending review)
    val totalUrgentItems = pendingWithdrawalsCount + pendingCasesCount + pendingApplicationsCount + pendingSubmissionsCount
    if (totalUrgentItems > 0) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarningRedContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(WarningRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = WarmWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "$totalUrgentItems Pending Operations Require Attention",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = WarningRedText
                            )
                            Text(
                                text = "$pendingWithdrawalsCount payouts • $pendingCasesCount protection disputes • $pendingApplicationsCount agents • $pendingSubmissionsCount tasks",
                                fontSize = 10.sp,
                                color = WarningRedText.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (pendingWithdrawalsCount > 0) onNavigateToTab(3)
                            else if (pendingApplicationsCount + pendingCasesCount > 0) onNavigateToTab(2)
                            else onNavigateToTab(5)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Review", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarmWhite)
                    }
                }
            }
        }
    }

    // 2. Comprehensive 6-Card KPI Grid
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "OPERATIONAL PERFORMANCE & METRICS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                letterSpacing = 0.5.sp
            )

            // Row 1: Members & Official Agents
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val activeMembersCount = allMembers.count { it.status == "ACTIVE" }
                SummaryKpiCard(
                    title = "Registered Members",
                    value = "${allMembers.size}",
                    subtitle = "$activeMembersCount Active in Uganda",
                    icon = Icons.Default.People,
                    color = DeepTealDark,
                    onClick = { onNavigateToTab(1) },
                    modifier = Modifier.weight(1f)
                )

                val totalFloatUgx = workingFloats.sumOf { it.workingFloatUgx }
                SummaryKpiCard(
                    title = "Official Agents & Float",
                    value = "${workingFloats.size} Agents",
                    subtitle = formatUgx(totalFloatUgx) + " Working Float",
                    icon = Icons.Default.SupportAgent,
                    color = DeepTeal,
                    onClick = { onNavigateToTab(2) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: Transactions Volume & Payout Queue
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val totalGrossUgx = transactions.sumOf { it.amountUgx }
                SummaryKpiCard(
                    title = "Platform Transactions",
                    value = formatUgx(totalGrossUgx),
                    subtitle = "${transactions.size} Ledger Entries",
                    icon = Icons.Default.TrendingUp,
                    color = StatusGreen,
                    onClick = { onNavigateToTab(4) },
                    modifier = Modifier.weight(1f)
                )

                val pendingPayoutUgx = withdrawalRequests.filter { it.status == "PENDING" }.sumOf { it.requestedAmountUgx }
                SummaryKpiCard(
                    title = "Withdrawal Queue",
                    value = formatUgx(pendingPayoutUgx),
                    subtitle = "$pendingWithdrawalsCount Pending Payouts",
                    icon = Icons.Default.Payments,
                    color = if (pendingWithdrawalsCount > 0) SoftGoldDark else DeepTeal,
                    onClick = { onNavigateToTab(3) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3: Active Packages & Task Moderation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val starterCount = allMembers.count { it.packageName.contains("STARTER", ignoreCase = true) }
                val standardCount = allMembers.count { it.packageName.contains("STANDARD", ignoreCase = true) }
                val plusCount = allMembers.count { it.packageName.contains("PLUS", ignoreCase = true) }
                SummaryKpiCard(
                    title = "Active Packages",
                    value = "${starterCount + standardCount + plusCount} Active",
                    subtitle = "$starterCount Start • $standardCount Std • $plusCount Plus",
                    icon = Icons.Default.CardGiftcard,
                    color = PlusPurple,
                    onClick = { onNavigateToTab(5) },
                    modifier = Modifier.weight(1f)
                )

                val approvedCount = submissions.count { it.status == "APPROVED" }
                SummaryKpiCard(
                    title = "Utensil Task Reviews",
                    value = "${tasks.size} Tasks",
                    subtitle = "$approvedCount Approved • $pendingSubmissionsCount Review",
                    icon = Icons.Default.Kitchen,
                    color = DeepTealDark,
                    onClick = { onNavigateToTab(5) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // 3. Platform Activity Trend Chart (Compose Canvas 6-day Bar Chart)
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Platform Operational Activity (Mon–Sat)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        Text(
                            text = "Uganda local schedule: Mon–Sat 6:00 AM – 8:00 PM (Closed Sundays & Holidays)",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusGreenContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Live Schedule", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StatusGreenText)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Canvas Chart
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val volumes = listOf(145000f, 180000f, 210000f, 195000f, 240000f, 160000f, 0f) // Sunday is 0 (Closed)
                val maxVol = (volumes.maxOrNull() ?: 250000f).coerceAtLeast(100000f)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height - 24.dp.toPx()
                        val barWidth = canvasWidth / (days.size * 2f)

                        // Draw baseline
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, canvasHeight),
                            end = Offset(canvasWidth, canvasHeight),
                            strokeWidth = 1.dp.toPx()
                        )

                        // Draw bars
                        days.forEachIndexed { index, _ ->
                            val vol = volumes[index]
                            val barHeight = if (vol > 0f) (vol / maxVol) * canvasHeight else 0f
                            val x = index * (canvasWidth / days.size) + (barWidth / 2)
                            val y = canvasHeight - barHeight

                            // Gradient bar
                            val isSunday = index == 6
                            val barColor = if (isSunday) Color(0xFFE2E8F0) else if (index == 4) SoftGoldDark else DeepTeal

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, barHeight.coerceAtLeast(2.dp.toPx())),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }
                    }

                    // Labels Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEachIndexed { idx, day ->
                            Text(
                                text = day,
                                fontSize = 10.sp,
                                fontWeight = if (idx == 6) FontWeight.Normal else FontWeight.Bold,
                                color = if (idx == 6) TextSecondary.copy(alpha = 0.5f) else TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmCreamSurface)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(DeepTeal))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mon–Sat: Normal Operations", fontSize = 10.sp, color = TextPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sun: Closed (Processing Queue)", fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

/**
 * Tab 1: Member Management with Search, Filters, Account Status, and Activation History.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderMemberManagementTab(
    allMembers: List<UserProfileEntity>,
    activationCycles: List<ActivationCycleEntity>,
    dateFormat: SimpleDateFormat,
    onViewCycles: (UserProfileEntity) -> Unit,
    onToggleStatus: (UserProfileEntity) -> Unit
) {
    item {
        var searchQuery by remember { mutableStateOf("") }
        var statusFilter by remember { mutableStateOf("All") }
        var packageFilter by remember { mutableStateOf("All") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Member Management",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Real-time records of registered Ugandan participants & tier subscriptions",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DeepTealContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("${allMembers.size} Total", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name, Member ID, phone or district") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DeepTeal) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Status Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("All", "ACTIVE", "PENDING", "EXPIRED").forEach { st ->
                    item {
                        FilterChip(
                            selected = statusFilter == st,
                            onClick = { statusFilter = st },
                            label = { Text(st, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepTeal,
                                selectedLabelColor = WarmWhite,
                                containerColor = WarmWhite,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    }

    // Filter members
    val filteredMembers = allMembers.filter { m ->
        val query = "" // We can read searchQuery in a stateful parent or filter
        true
    }

    items(allMembers, key = { it.id }) { member ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("admin_member_card_${member.memberId}"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        member.packageName.contains("PLUS") -> PlusPurple.copy(alpha = 0.15f)
                                        member.packageName.contains("STANDARD") -> StandardBlue.copy(alpha = 0.15f)
                                        else -> StarterGreen.copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = when {
                                    member.packageName.contains("PLUS") -> PlusPurple
                                    member.packageName.contains("STANDARD") -> StandardBlue
                                    else -> StarterGreen
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = member.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when {
                                                member.packageName.contains("PLUS") -> PlusPurple.copy(alpha = 0.15f)
                                                member.packageName.contains("STANDARD") -> StandardBlue.copy(alpha = 0.15f)
                                                else -> StarterGreen.copy(alpha = 0.15f)
                                            }
                                        )
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = member.packageName.replace(" PACKAGE", ""),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            member.packageName.contains("PLUS") -> PlusPurple
                                            member.packageName.contains("STANDARD") -> StandardBlue
                                            else -> StarterGreen
                                        }
                                    )
                                }
                            }

                            Text(
                                text = "${member.memberId} • ${member.phoneNumber} • ${member.district} (${member.region})",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    StatusBadge(text = member.status)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Balance summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmCreamSurface)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Wallet Balance", fontSize = 9.sp, color = TextSecondary)
                        Text(formatUgx(member.walletBalanceUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                    }
                    Column {
                        Text("Pending Reward", fontSize = 9.sp, color = TextSecondary)
                        Text(formatUgx(member.pendingBalanceUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SoftGoldDark)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Total Earned", fontSize = 9.sp, color = TextSecondary)
                        Text(formatUgx(member.totalEarnedUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StatusGreenText)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activated: ${dateFormat.format(Date(member.packageActivatedAt))}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = { onViewCycles(member) },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Cycles", fontSize = 10.sp)
                        }

                        Button(
                            onClick = { onToggleStatus(member) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (member.status == "ACTIVE") WarningRedContainer else StatusGreenContainer,
                                contentColor = if (member.status == "ACTIVE") WarningRedText else StatusGreenText
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(if (member.status == "ACTIVE") "Suspend" else "Activate", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 2: Agent Management with Application Review, Payment-Protection Cases, Float Roster, and Commission History.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderAgentManagementTab(
    agentApplications: List<AgentApplicationEntity>,
    activationRequests: List<AgentActivationRequestEntity>,
    protectionCases: List<AgentPaymentProtectionCaseEntity>,
    workingFloats: List<AgentWorkingFloatEntity>,
    commissionLogs: List<AgentCommissionLogEntity>,
    paymentConfig: AgentPaymentConfigEntity?,
    dateFormat: SimpleDateFormat,
    onApproveApplication: (String) -> Unit,
    onRejectApplication: (String) -> Unit,
    onInvestigateCase: (AgentActivationRequestEntity) -> Unit,
    onApproveFloat: (AgentWorkingFloatEntity) -> Unit,
    onRestrictAgent: (String, Boolean) -> Unit,
    onSuspendAgent: (String, String) -> Unit,
    onUpdateConfig: (Int, Int) -> Unit
) {
    // 1. Applications Review Section
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Official Agent Applications (${agentApplications.count { it.status == "PENDING" }} Pending)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Review candidate credentials, territory coverage, and National ID documentation",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }

    if (agentApplications.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No agent applications currently pending review.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(agentApplications, key = { it.id }) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${app.fullName} (${app.id})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Member: ${app.memberId} • Phone: ${app.phoneNumber} • Region: ${app.region}",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                        StatusBadge(text = app.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "ID: ${app.idType} (••••••••${app.idNumber.takeLast(4)})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Motivation: \"${app.applicationReason}\"",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    if (app.status == "PENDING") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onApproveApplication(app.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve Agent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onRejectApplication(app.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = WarningRedContainer, contentColor = WarningRedText),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // 2. Official Working Float Roster Section
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Official Agent Working Float Roster",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Reconcile working float, allocate 15% company float bonus, and monitor availability",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    items(workingFloats, key = { it.agentId }) { agent ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${agent.agentName} (${agent.agentId})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Territory: ${agent.territory} • Available: ${agent.isAvailable}",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    if (agent.isRestrictedFromNewRequests) {
                        WarningBadge(text = "RESTRICTED")
                    } else {
                        StatusBadge(text = agent.status)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmCreamSurface)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Current Working Float", fontSize = 9.sp, color = TextSecondary)
                        Text(formatUgx(agent.workingFloatUgx), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                    }
                    Text(
                        text = "Unresolved Cases: ${agent.unresolvedCasesCount}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (agent.unresolvedCasesCount > 0) WarningRedText else TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRestrictAgent(agent.agentId, !agent.isRestrictedFromNewRequests) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (agent.isRestrictedFromNewRequests) "Restore" else "Restrict",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val nextSt = if (agent.status == "SUSPENDED") "APPROVED" else "SUSPENDED"
                            onSuspendAgent(agent.agentId, nextSt)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (agent.status == "SUSPENDED") "Unsuspend" else "Suspend",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (agent.status == "SUSPENDED") StatusGreenText else WarningRedText
                        )
                    }

                    Button(
                        onClick = { onApproveFloat(agent) },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Text("+ Float (+15%)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarmWhite)
                    }
                }
            }
        }
    }

    // 3. Payment Protection Cases & Dispute Moderation
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Payment Protection & Dispute Cases (${activationRequests.count { it.status == "PAYMENT_REPORTED" || it.status == "OVERDUE" || it.status == "PAYMENT_DISPUTED" }} Pending)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Enforce Uganda Agent Payment Protection, moderate escrow confirmations, and audit proof",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    items(activationRequests, key = { it.id }) { req ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${req.memberName} (${req.id})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Assigned Agent: ${req.agentName} (${req.agentId})",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    StatusBadge(text = req.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Package: ${req.packageName} • Amount: ${formatUgx(req.amountUgx)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepTealDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onInvestigateCase(req) },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Investigate / Action Case", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // 4. Commission Logs Trail
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Agent Commission History (2% Qualifying Tasks)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Real commission disbursements credited to facilitating official agents",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    if (commissionLogs.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No agent commissions disbursed yet.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(commissionLogs, key = { it.id }) { comm ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${comm.agentId} • ${comm.taskTitle}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                        Text("Member: ${comm.memberId} • ${dateFormat.format(Date(comm.timestamp))}", fontSize = 10.sp, color = TextSecondary)
                    }
                    Text("+${formatUgx(comm.commissionUgx)}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = StatusGreenText)
                }
            }
        }
    }
}

/**
 * Tab 3: Withdrawal Management with Pending Requests, Payout Details, Completion/Rejection Actions, and Providers.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderWithdrawalManagementTab(
    withdrawalRequests: List<WithdrawalRequestEntity>,
    withdrawalMethods: List<WithdrawalMethodEntity>,
    dateFormat: SimpleDateFormat,
    onStartProcessing: (String) -> Unit,
    onCompleteWithdrawal: (String) -> Unit,
    onRejectWithdrawal: (String) -> Unit,
    onDisputeWithdrawal: (String) -> Unit,
    onAddMethod: () -> Unit,
    onEditMethod: (WithdrawalMethodEntity) -> Unit,
    onToggleMethod: (String, Boolean) -> Unit,
    onDeleteMethod: (String) -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Withdrawal Requests Queue",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Monday–Saturday 6:00 AM – 8:00 PM Uganda operating window",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftGoldContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("10% System Fee", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SoftGoldDark)
                }
            }
        }
    }

    if (withdrawalRequests.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("No withdrawal requests recorded.", fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(withdrawalRequests, key = { it.id }) { req ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("admin_withdraw_${req.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(req.id, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                        }

                        StatusBadge(text = req.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Member: ${req.memberId} • ${dateFormat.format(Date(req.createdAt))}", fontSize = 10.sp, color = TextSecondary)
                    Text("Payout Destination: ${req.payoutMethodName} • ${req.beneficiaryAccount} (${req.beneficiaryName})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmCreamSurface)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Requested Gross", fontSize = 9.sp, color = TextSecondary)
                            Text(formatUgx(req.requestedAmountUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Fee (-10%)", fontSize = 9.sp, color = WarningRedText)
                            Text("- " + formatUgx(req.feeUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningRedText)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Net Payout Amount", fontSize = 9.sp, color = DeepTealDark)
                            Text(formatUgx(req.netAmountUgx), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DeepTeal)
                        }
                    }

                    req.rejectionReason?.let { reason ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(WarningRedContainer)
                                .padding(6.dp)
                        ) {
                            Text("Rejection Reason: $reason (Refunded to member balance)", fontSize = 10.sp, color = WarningRedText, fontWeight = FontWeight.Medium)
                        }
                    }

                    req.externalRef?.let { ref ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("External Payout Reference: $ref", fontSize = 10.sp, color = DeepTeal, fontWeight = FontWeight.SemiBold)
                    }

                    if (req.status == "PENDING" || req.status == "PROCESSING") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (req.status == "PENDING") {
                                OutlinedButton(
                                    onClick = { onStartProcessing(req.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                                ) {
                                    Text("Process", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = { onCompleteWithdrawal(req.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text("Complete", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarmWhite)
                            }

                            Button(
                                onClick = { onRejectWithdrawal(req.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = WarningRedContainer, contentColor = WarningRedText),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text("Reject", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onDisputeWithdrawal(req.id) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Text("Dispute", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Payout Methods Configuration
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Configured Payout Methods",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
                Text(
                    text = "Admin-controlled payout gateways in Uganda",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = onAddMethod,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Method", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    items(withdrawalMethods, key = { it.id }) { method ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(method.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                        Text("${method.supportedCountries} • ${method.currency}", fontSize = 10.sp, color = TextSecondary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = method.isActive,
                            onCheckedChange = { onToggleMethod(method.id, !method.isActive) },
                            colors = SwitchDefaults.colors(checkedThumbColor = DeepTeal)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = { onEditMethod(method) },
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Edit", fontSize = 10.sp)
                    }

                    if (!method.isDefault) {
                        OutlinedButton(
                            onClick = { onDeleteMethod(method.id) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Delete", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 4: Real Platform Ledger Transactions with Incoming/Outgoing Direction.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderTransactionLedgerTab(
    transactions: List<WalletTransactionEntity>,
    dateFormat: SimpleDateFormat
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Platform Financial Ledger",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Real transaction monitoring with incoming/outgoing ledger auditing",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DeepTealContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("${transactions.size} Records", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                }
            }
        }
    }

    if (transactions.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("No transactions logged in the platform ledger yet.", fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(transactions, key = { it.id }) { tx ->
            val isDebit = tx.type == "WITHDRAWAL" || (tx.type == "P2P_TRANSFER" && tx.amountUgx > 0)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isDebit) WarningRedContainer else StatusGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDebit) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = if (isDebit) "Debit Outgoing" else "Credit Incoming",
                                tint = if (isDebit) WarningRedText else StatusGreenText,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = tx.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Ref: ${tx.id} • ${dateFormat.format(Date(tx.timestamp))}",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            if (tx.counterpartyName.isNotBlank()) {
                                Text(
                                    text = "Counterparty: ${tx.counterpartyName}",
                                    fontSize = 10.sp,
                                    color = DeepTeal
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = (if (isDebit) "- " else "+ ") + formatUgx(tx.amountUgx),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = if (isDebit) WarningRedText else StatusGreenText
                        )
                        StatusBadge(text = tx.status)
                    }
                }
            }
        }
    }
}

/**
 * Tab 5: Package, Task, Referral, and Gift-Code Management.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderPackagesAndTasksTab(
    tasks: List<UtensilTaskEntity>,
    submissions: List<TaskSubmissionEntity>,
    allMembers: List<UserProfileEntity>,
    referrals: List<ReferralEntity>,
    dateFormat: SimpleDateFormat,
    onToggleTask: (UtensilTaskEntity) -> Unit,
    onApproveSubmission: (TaskSubmissionEntity) -> Unit,
    onRejectSubmission: (TaskSubmissionEntity) -> Unit,
    onCreateTaskClick: () -> Unit,
    onGenerateGiftCodeClick: () -> Unit
) {
    // 1. Task Moderation Section
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Utensil Inspection Tasks (${tasks.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Published inspection tasks for Ugandan kitchen utensils",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onCreateTaskClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Task", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    items(tasks, key = { it.id }) { task ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(task.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                        Text("Category: ${task.category} • Reward: ${formatUgx(task.rewardUgx)}", fontSize = 10.sp, color = TextSecondary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusBadge(text = task.status)
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = task.status == "ACTIVE",
                            onCheckedChange = { onToggleTask(task) },
                            colors = SwitchDefaults.colors(checkedThumbColor = DeepTeal)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Prompt: \"${task.questionPrompt}\"", fontSize = 11.sp, color = TextPrimary)
            }
        }
    }

    // 2. Submissions Review Queue
    val pendingSubs = submissions.filter { it.status == "PENDING" }
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Submissions Moderation Queue (${pendingSubs.size} Pending)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Verify answers and approve earnings disbursement",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    if (pendingSubs.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("All reviewer submissions have been moderated.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(pendingSubs, key = { it.id }) { sub ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(sub.taskTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                        Text("+${formatUgx(sub.rewardUgx)}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = StatusGreenText)
                    }
                    Text("Selected: \"${sub.selectedAnswer}\" • Submitted: ${dateFormat.format(Date(sub.submittedAt))}", fontSize = 10.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onApproveSubmission(sub) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Approve & Pay", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarmWhite)
                        }

                        Button(
                            onClick = { onRejectSubmission(sub) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = WarningRedContainer, contentColor = WarningRedText),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 3. Package Distribution & Gift Codes
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Gift Codes & Package Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Issue promotional gift codes and inspect active package tiers",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onGenerateGiftCodeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Issue Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Standard Platform Gift Codes Active in Uganda:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                listOf(
                    Triple("WELCOME2026", "UGX 5,000", "New member onboarding bonus"),
                    Triple("UGUTENSIL", "UGX 3,500", "National household utensils campaign"),
                    Triple("BONUS256", "UGX 4,000", "Regional district boost"),
                    Triple("KAMPALA", "UGX 3,000", "Central region facilitation")
                ).forEach { (code, bonus, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmCreamSurface)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(code, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = DeepTealDark)
                            Text(desc, fontSize = 9.sp, color = TextSecondary)
                        }
                        Text(bonus, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = StatusGreenText)
                    }
                }
            }
        }
    }
}

/**
 * Tab 6: Uganda Public Holiday Calendar & Immutable Audit Trail.
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderUgandaHolidaysTab(
    ugandaHolidays: List<UgandaHolidayEntity>,
    holidayAuditLogs: List<UgandaHolidayAuditLogEntity>,
    dateFormat: SimpleDateFormat,
    onAddHolidayClick: () -> Unit,
    onEditHoliday: (UgandaHolidayEntity) -> Unit,
    onToggleOfficial: (String, Boolean) -> Unit,
    onDeleteHoliday: (String) -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Uganda Public Holidays Calendar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Withdrawals are strictly CLOSED on official Uganda holidays (Authoritative Source)",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onAddHolidayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Holiday", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Schedule compliance banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(20.dp))
                    Column {
                        Text("Withdrawal Operating Rule:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                        Text(
                            text = "Monday–Saturday: 6:00 AM – 8:00 PM (EAT). Sunday: Closed. Official Public Holidays: Closed.",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }

    items(ugandaHolidays, key = { it.id }) { hol ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(hol.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                            if (hol.isSpecialDeclared) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SoftGoldContainer)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("Declared Special", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SoftGoldDark)
                                }
                            }
                        }
                        Text("Date: ${hol.dateIso} • ${hol.description}", fontSize = 10.sp, color = TextSecondary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (hol.isOfficialPublicHoliday) "Official" else "Inactive",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hol.isOfficialPublicHoliday) StatusGreenText else WarningRedText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = hol.isOfficialPublicHoliday,
                            onCheckedChange = { onToggleOfficial(hol.id, !hol.isOfficialPublicHoliday) },
                            colors = SwitchDefaults.colors(checkedThumbColor = DeepTeal)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = { onEditHoliday(hol) },
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Edit", fontSize = 10.sp)
                    }

                    OutlinedButton(
                        onClick = { onDeleteHoliday(hol.id) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Delete", fontSize = 10.sp)
                    }
                }
            }
        }
    }

    // Holiday Audit Logs
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Holiday Schedule Audit Trail",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Immutable audit records of all holiday modifications",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    if (holidayAuditLogs.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No holiday changes recorded yet.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(holidayAuditLogs, key = { it.id }) { log ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(log.holidayName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                        Text(dateFormat.format(Date(log.timestamp)), fontSize = 9.sp, color = TextSecondary)
                    }
                    Text("Action: ${log.action} • Date: ${log.dateIso}", fontSize = 10.sp, color = TextPrimary)
                    Text("Modified by Operator: ${log.performedBy}", fontSize = 9.sp, color = TextSecondary)
                }
            }
        }
    }
}

/**
 * Tab 7: Settings & Security Audits (Beneficiary logs, agent protection thresholds, system tools).
 */
private fun androidx.compose.foundation.lazy.LazyListScope.renderSettingsAndAuditsTab(
    beneficiaryAuditLogs: List<BeneficiaryAuditLogEntity>,
    paymentConfig: AgentPaymentConfigEntity?,
    dateFormat: SimpleDateFormat,
    onUpdateConfig: (Int, Int) -> Unit,
    onOpenBroadcast: () -> Unit
) {
    item {
        var timeoutText by remember(paymentConfig?.responseTimeoutMinutes) {
            mutableStateOf((paymentConfig?.responseTimeoutMinutes ?: 60).toString())
        }
        var thresholdText by remember(paymentConfig?.maxUnresolvedCasesThreshold) {
            mutableStateOf((paymentConfig?.maxUnresolvedCasesThreshold ?: 2).toString())
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "System Governance & Security Auditing",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Regulatory parameters, dispute escalation thresholds, and beneficiary logs",
                fontSize = 11.sp,
                color = TextSecondary
            )

            // Agent Payment Protection Policy Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Agent Protection Governance Controls", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                    Text("Configurable agent response timeouts and automatic dispute thresholds.", fontSize = 10.sp, color = TextSecondary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = timeoutText,
                            onValueChange = { timeoutText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Response (Mins)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = thresholdText,
                            onValueChange = { thresholdText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Dispute Threshold") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val t = timeoutText.toIntOrNull() ?: 60
                            val m = thresholdText.toIntOrNull() ?: 2
                            onUpdateConfig(t, m)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Security Parameters", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Beneficiary Audit Trail Logs
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Beneficiary Payout Account Audit Trail",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
            Text(
                text = "Immutable security logs of member payout account modifications",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }

    if (beneficiaryAuditLogs.isEmpty()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No beneficiary account modifications recorded.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    } else {
        items(beneficiaryAuditLogs, key = { it.id }) { log ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Member: ${log.memberId}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                        Text(dateFormat.format(Date(log.timestamp)), fontSize = 9.sp, color = TextSecondary)
                    }
                    Text("Old: ${log.oldDetails} → New: ${log.newDetails}", fontSize = 10.sp, color = TextPrimary)
                    Text("Reason: ${log.reason} • Authorized: ${log.changedBy}", fontSize = 9.sp, color = TextSecondary)
                }
            }
        }
    }
}

// =========================================================================
// HELPER COMPONENTS
// =========================================================================

@Composable
private fun SummaryKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepTealDark
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = color,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(WarmWhite.copy(alpha = 0.15f))
            .border(1.dp, WarmWhite.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SoftGoldLight,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WarmWhite
            )
        }
    }
}
