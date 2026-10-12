package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.model.WalletTransactionEntity
import com.example.ui.components.LocationBadge
import com.example.ui.components.RewardBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarningBadge
import com.example.ui.components.PackageBadgeChip
import com.example.ui.components.PackageVisualTier
import com.example.ui.components.formatUgx
import com.example.ui.theme.PlusPurple
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldLight
import com.example.ui.theme.SoftGoldOnContainer
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
import com.example.ui.viewmodel.AppNotification
import com.example.ui.viewmodel.MoreSection
import com.example.ui.viewmodel.UiState

@Composable
fun MoreScreen(
    uiState: UiState,
    userProfile: UserProfileEntity?,
    notifications: List<AppNotification>,
    transactions: List<WalletTransactionEntity>,
    tasks: List<UtensilTaskEntity>,
    submissions: List<TaskSubmissionEntity>,
    referrals: List<ReferralEntity>,
    onNavigateSection: (MoreSection) -> Unit,
    onBackToMenu: () -> Unit,
    onMarkNotificationsRead: () -> Unit,
    onRedeemGiftCode: (String) -> Unit,
    onGiftCodeInputChange: (String) -> Unit,
    onToggleBiometrics: () -> Unit,
    onTogglePinLock: () -> Unit,
    onSupportSubjectChange: (String) -> Unit,
    onSupportMessageChange: (String) -> Unit,
    onSendSupportTicket: () -> Unit,
    onShowLogoutDialog: (Boolean) -> Unit,
    onConfirmLogout: () -> Unit,
    onShowDistrictDialog: (Boolean) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onToggleRole: () -> Unit,
    // Admin delegate actions
    onApproveSubmission: (TaskSubmissionEntity) -> Unit,
    onRejectSubmission: (TaskSubmissionEntity) -> Unit,
    onToggleTaskStatus: (UtensilTaskEntity) -> Unit,
    onCreateTask: (String, String, Long, String, String, List<String>) -> Unit,
    onShowCreateTaskDialog: (Boolean) -> Unit,
    onAdminSubTabChanged: (String) -> Unit,
    agentApplication: AgentApplicationEntity? = null,
    onApplyAgentClick: () -> Unit = {},
    onOpenAgentPortal: () -> Unit = {},
    onFindAgentClick: () -> Unit = {},
    onOpenUpgrade: () -> Unit = {},
    onOpenReactivate: () -> Unit = {},
    onOpenP2P: () -> Unit = {},
    onOpenWithdraw: () -> Unit = {},
    onClaimReferralCode: (String) -> Unit = {},
    onSimulateReferralInvite: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        when (uiState.moreSection) {
            MoreSection.MENU -> {
                MoreMenuHub(
                    userProfile = userProfile,
                    unreadNotifCount = notifications.count { !it.isRead },
                    agentApplication = agentApplication,
                    onNavigateSection = onNavigateSection,
                    onShowLogoutDialog = onShowLogoutDialog,
                    onApplyAgentClick = onApplyAgentClick,
                    onOpenAgentPortal = onOpenAgentPortal,
                    onOpenUpgrade = onOpenUpgrade,
                    onOpenReactivate = onOpenReactivate,
                    onOpenP2P = onOpenP2P,
                    onOpenWithdraw = onOpenWithdraw
                )
            }

            MoreSection.PROFILE -> {
                MoreProfileSubScreen(
                    userProfile = userProfile,
                    showDistrictDialog = uiState.showDistrictDialog,
                    onBack = onBackToMenu,
                    onShowDistrictDialog = onShowDistrictDialog,
                    onDistrictSelected = onDistrictSelected,
                    onToggleRole = onToggleRole
                )
            }

            MoreSection.SETTINGS -> {
                MoreSettingsSubScreen(
                    userProfile = userProfile,
                    biometricsEnabled = uiState.biometricsEnabled,
                    pinLockEnabled = uiState.pinLockEnabled,
                    showDistrictDialog = uiState.showDistrictDialog,
                    onToggleBiometrics = onToggleBiometrics,
                    onTogglePinLock = onTogglePinLock,
                    onShowDistrictDialog = onShowDistrictDialog,
                    onDistrictSelected = onDistrictSelected,
                    onBack = onBackToMenu
                )
            }

            MoreSection.REFERRALS -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WarmCreamBackground)
                ) {
                    SubScreenHeader(title = "Referral & Commissions", onBack = onBackToMenu)
                    ReferralsScreen(
                        userProfile = userProfile,
                        referrals = referrals,
                        onClaimCode = onClaimReferralCode,
                        onSimulateInvite = onSimulateReferralInvite,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            MoreSection.NOTIFICATIONS -> {
                MoreNotificationsSubScreen(
                    notifications = notifications,
                    onBack = onBackToMenu,
                    onMarkAllRead = onMarkNotificationsRead
                )
            }

            MoreSection.MONTHLY_EARNINGS -> {
                MoreMonthlyEarningsSubScreen(
                    userProfile = userProfile,
                    transactions = transactions,
                    onBack = onBackToMenu
                )
            }

            MoreSection.GIFT_CODE -> {
                MoreGiftCodeSubScreen(
                    giftCodeInput = uiState.giftCodeInput,
                    isClaiming = uiState.isClaimingGiftCode,
                    claimedCodes = uiState.claimedGiftCodes,
                    onCodeChange = onGiftCodeInputChange,
                    onClaim = onRedeemGiftCode,
                    onBack = onBackToMenu
                )
            }

            MoreSection.TRANSACTIONS -> {
                MoreTransactionsSubScreen(
                    transactions = transactions,
                    onBack = onBackToMenu
                )
            }

            MoreSection.CUSTOMER_CARE -> {
                MoreCustomerCareSubScreen(
                    subject = uiState.supportSubject,
                    message = uiState.supportMessage,
                    isSent = uiState.supportTicketSent,
                    onSubjectChange = onSupportSubjectChange,
                    onMessageChange = onSupportMessageChange,
                    onSend = onSendSupportTicket,
                    onBack = onBackToMenu
                )
            }

            MoreSection.SECURITY -> {
                MoreSecuritySubScreen(
                    biometricsEnabled = uiState.biometricsEnabled,
                    pinLockEnabled = uiState.pinLockEnabled,
                    onToggleBiometrics = onToggleBiometrics,
                    onTogglePinLock = onTogglePinLock,
                    onBack = onBackToMenu
                )
            }
        }

        // Logout Confirmation Dialog
        if (uiState.showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { onShowLogoutDialog(false) },
                title = {
                    Text(
                        text = "Sign Out of HomeUtensils?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out from this device in Uganda? Your task earnings will remain safely stored.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onConfirmLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Log Out")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onShowLogoutDialog(false) }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = WarmWhite,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// ----------------- SUB-COMPONENTS -----------------

@Composable
fun MoreMenuHub(
    userProfile: UserProfileEntity?,
    unreadNotifCount: Int,
    agentApplication: AgentApplicationEntity? = null,
    onNavigateSection: (MoreSection) -> Unit,
    onShowLogoutDialog: (Boolean) -> Unit,
    onApplyAgentClick: () -> Unit = {},
    onOpenAgentPortal: () -> Unit = {},
    onOpenUpgrade: () -> Unit = {},
    onOpenReactivate: () -> Unit = {},
    onOpenP2P: () -> Unit = {},
    onOpenWithdraw: () -> Unit = {}
) {
    val isPlusPackage = userProfile?.packageName?.trim()?.contains("PLUS", ignoreCase = true) == true
    val isCurrentPackageActive = userProfile?.isPackageExpired != true && System.currentTimeMillis() < (userProfile?.packageExpiresAt ?: 0L)
    val isEligiblePlusMember = isPlusPackage && isCurrentPackageActive
    val isExpired = userProfile?.isPackageExpired == true ||
        (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0 && System.currentTimeMillis() >= userProfile.packageExpiresAt)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("more_menu_hub"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // User Brief Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateSection(MoreSection.PROFILE) }
                    .testTag("more_profile_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(DeepTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = WarmWhite,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile?.fullName ?: "Nalubega Sarah",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            StatusBadge(text = if (isExpired) "Expired" else "Active")
                            Spacer(modifier = Modifier.width(4.dp))
                            PackageBadgeChip(tier = PackageVisualTier.fromPackageName(userProfile?.packageName), isLarge = false)
                        }
                        Text(
                            text = "${userProfile?.phoneNumber ?: "+256 772 458921"} • ${userProfile?.district ?: "Kampala"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Profile",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Section: Package Operations
        item {
            Text(
                text = "Package Operations",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    MoreMenuItem(
                        icon = Icons.Default.ArrowUpward,
                        title = "Upgrade Package",
                        subtitle = "Move to Standard or Plus tier for higher daily rewards",
                        badge = "Tier Upgrade",
                        badgeColor = PlusPurple,
                        onClick = onOpenUpgrade
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Refresh,
                        title = "Reactivate Package",
                        subtitle = if (isExpired) "Package expired — renew 30-day earning cycle" else "Renew subscription or extend active cycle",
                        badge = if (isExpired) "Action Required" else "Renewal",
                        badgeColor = if (isExpired) WarningRed else DeepTeal,
                        onClick = onOpenReactivate
                    )
                }
            }
        }

        // Section: Financial & Transfers
        item {
            Text(
                text = "Transfers & Financial Services",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    MoreMenuItem(
                        icon = Icons.Default.SwapHoriz,
                        title = "P2P Transfer",
                        subtitle = "Send balance or activate friend's package instantly",
                        badge = "P2P",
                        badgeColor = DeepTeal,
                        onClick = onOpenP2P
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Payments,
                        title = "Withdraw",
                        subtitle = "MTN MoMo, Airtel Money, Bank & Agent cashouts",
                        badge = "Cashout",
                        badgeColor = StatusGreen,
                        onClick = onOpenWithdraw
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.ReceiptLong,
                        title = "Transaction History",
                        subtitle = "Complete ledger of tasks, rewards & transfers",
                        onClick = { onNavigateSection(MoreSection.TRANSACTIONS) }
                    )
                }
            }
        }

        // Section: Agent Program
        item {
            Text(
                text = "Agent System",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    if (agentApplication?.status == "APPROVED") {
                        MoreMenuItem(
                            icon = Icons.Default.SupportAgent,
                            title = "Agent Center",
                            subtitle = "Float: Min UGX 50K (+15% bonus) • 2% Commission",
                            badge = "Approved Agent",
                            badgeColor = StatusGreen,
                            onClick = onOpenAgentPortal
                        )
                    } else {
                        MoreMenuItem(
                            icon = Icons.Default.SupportAgent,
                            title = "Apply as Agent",
                            subtitle = if (isEligiblePlusMember) "UGX 50K Min Float • +15% Company Bonus • 2% Commission" else "Requires Active PLUS Package Membership",
                            badge = if (isEligiblePlusMember) "Eligible" else "Plus Exclusive",
                            badgeColor = if (isEligiblePlusMember) PlusPurple else DeepTeal,
                            onClick = onApplyAgentClick
                        )
                    }
                }
            }
        }

        // Section: Rewards & Growth
        item {
            Text(
                text = "Rewards & Community",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    MoreMenuItem(
                        icon = Icons.Default.Share,
                        title = "Referral & Commissions",
                        subtitle = "Direct bonuses & monthly recurring member rewards",
                        badge = "Commissions",
                        badgeColor = SoftGold,
                        onClick = { onNavigateSection(MoreSection.REFERRALS) }
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.CardGiftcard,
                        title = "Gift Code",
                        subtitle = "Redeem promo codes for instant wallet bonus",
                        badge = "Promo",
                        badgeColor = StatusGreen,
                        onClick = { onNavigateSection(MoreSection.GIFT_CODE) }
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Task approvals & system announcements",
                        badge = if (unreadNotifCount > 0) "$unreadNotifCount new" else null,
                        badgeColor = SoftGold,
                        onClick = { onNavigateSection(MoreSection.NOTIFICATIONS) }
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Insights,
                        title = "Monthly Earnings",
                        subtitle = "Review UGX performance & historical targets",
                        onClick = { onNavigateSection(MoreSection.MONTHLY_EARNINGS) }
                    )
                }
            }
        }

        // Section: Security & Settings
        item {
            Text(
                text = "Account & Security",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    MoreMenuItem(
                        icon = Icons.Default.Security,
                        title = "Security & Transaction PIN",
                        subtitle = "App PIN, biometric unlock & session management",
                        onClick = { onNavigateSection(MoreSection.SECURITY) }
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        subtitle = "Account details, district preferences & privacy",
                        onClick = { onNavigateSection(MoreSection.SETTINGS) }
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)

                    MoreMenuItem(
                        icon = Icons.Default.Headphones,
                        title = "Support / Help",
                        subtitle = "Uganda helpdesk, WhatsApp & FAQs",
                        onClick = { onNavigateSection(MoreSection.CUSTOMER_CARE) }
                    )
                }
            }
        }

        // Section: Logout
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowLogoutDialog(true) }
                    .testTag("logout_button"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarningRedContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = WarningRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarningRedText
                        )
                        Text(
                            text = "Securely sign out and return to Login screen",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Log Out",
                        tint = WarningRed
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    badgeColor: Color = SoftGold,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DeepTealContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DeepTeal,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(badgeColor.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ----------------- SUB SCREEN: NOTIFICATIONS -----------------

@Composable
fun MoreNotificationsSubScreen(
    notifications: List<AppNotification>,
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Notifications", onBack = onBack, actionText = "Mark All Read", onAction = onMarkAllRead)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (notifications.isEmpty()) {
                item {
                    Text(
                        text = "No notifications yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(notifications, key = { it.id }) { notif ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!notif.isRead) WarmCreamSurface else WarmWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (!notif.isRead) SoftGold.copy(alpha = 0.5f) else WarmCreamBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (notif.type) {
                                            "REWARD" -> SoftGoldContainer
                                            "TASK" -> DeepTealContainer
                                            else -> StatusGreenContainer
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "REWARD" -> Icons.Default.CardGiftcard
                                        "TASK" -> Icons.Default.Kitchen
                                        else -> Icons.Default.Notifications
                                    },
                                    contentDescription = notif.title,
                                    tint = when (notif.type) {
                                        "REWARD" -> SoftGoldOnContainer
                                        "TASK" -> DeepTeal
                                        else -> StatusGreenText
                                    },
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = notif.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = notif.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = notif.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------- SUB SCREEN: MONTHLY EARNINGS -----------------

@Composable
fun MoreMonthlyEarningsSubScreen(
    userProfile: UserProfileEntity?,
    transactions: List<WalletTransactionEntity>,
    onBack: () -> Unit
) {
    val totalEarned = userProfile?.totalEarnedUgx ?: 32000L
    val targetUgx = 60000L
    val progress = (totalEarned.toFloat() / targetUgx.toFloat()).coerceIn(0f, 1f)

    val taskEarnings = transactions.filter { it.type == "TASK_REWARD" && it.status == "CREDITED" }.sumOf { it.amountUgx }
    val referralEarnings = transactions.filter { it.type == "REFERRAL_BONUS" && it.status == "CREDITED" }.sumOf { it.amountUgx }
    val bonusEarnings = transactions.filter { it.type == "WELCOME_BONUS" && it.status == "CREDITED" }.sumOf { it.amountUgx }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Monthly Earnings", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Target Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepTealDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Month (September 2026)",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGoldLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatUgx(totalEarned),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = WarmWhite
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Monthly Goal: ${formatUgx(targetUgx)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmCreamBackground.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${(progress * 100).toInt()}% Achieved",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SoftGold,
                        trackColor = Color(0xFF00382E)
                    )
                }
            }

            // Breakdown
            Text(
                text = "Earnings Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    EarningsBreakdownRow(label = "Household Utensil Tasks", amount = taskEarnings, color = DeepTeal)
                    HorizontalDivider(color = WarmCreamBorder)
                    EarningsBreakdownRow(label = "Referral Friends Bonuses", amount = referralEarnings, color = StatusGreen)
                    HorizontalDivider(color = WarmCreamBorder)
                    EarningsBreakdownRow(label = "Gift & Welcome Bonuses", amount = bonusEarnings, color = SoftGoldDark)
                }
            }

            // Daily Activity Insight
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Uganda Review Performance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You are in the top 15% of active household utensil reviewers in Kampala & Wakiso this month! Keep verifying kitchen items to unlock higher-tier tasks.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EarningsBreakdownRow(label: String, amount: Long, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
        Text(
            text = formatUgx(amount),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// ----------------- SUB SCREEN: GIFT / BONUS CODE -----------------

@Composable
fun MoreGiftCodeSubScreen(
    giftCodeInput: String,
    isClaiming: Boolean,
    claimedCodes: Set<String>,
    onCodeChange: (String) -> Unit,
    onClaim: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Gift / Bonus Code", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Redeem Utensil Bonus Voucher",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enter promotional gift codes received from HomeUtensils campaigns or events in Uganda to claim instant wallet credits.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = giftCodeInput,
                        onValueChange = onCodeChange,
                        placeholder = { Text("e.g., UGUTENSIL, BONUS256") },
                        label = { Text("Bonus Voucher Code") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gift_code_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = WarmCreamBorder
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onClaim(giftCodeInput) },
                        enabled = giftCodeInput.isNotBlank() && !isClaiming,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("redeem_gift_code_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepTeal,
                            contentColor = WarmWhite
                        )
                    ) {
                        if (isClaiming) {
                            CircularProgressIndicator(color = WarmWhite, modifier = Modifier.size(18.dp))
                        } else {
                            Text("Redeem Voucher", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Available Demo Codes
            Text(
                text = "Available Community Codes (Tap to try)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )

            val sampleCodes = listOf(
                Pair("UGUTENSIL", "Community launch bonus (+UGX 3,500)"),
                Pair("BONUS256", "Uganda national reviewer reward (+UGX 4,000)"),
                Pair("KAMPALA", "Central district utensil incentive (+UGX 3,000)")
            )

            sampleCodes.forEach { (code, desc) ->
                val alreadyUsed = claimedCodes.contains(code)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !alreadyUsed) { onCodeChange(code) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (alreadyUsed) WarmCreamSurface else WarmWhite
                    ),
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
                            Text(
                                text = code,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (alreadyUsed) TextSecondary else DeepTeal
                            )
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        if (alreadyUsed) {
                            StatusBadge(text = "Claimed")
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SoftGoldContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Use Code",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGoldOnContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------- SUB SCREEN: TRANSACTIONS -----------------

@Composable
fun MoreTransactionsSubScreen(
    transactions: List<WalletTransactionEntity>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Transactions", onBack = onBack)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "All Wallet Activities (${transactions.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
            }

            items(transactions, key = { it.id }) { tx ->
                TransactionItemRow(tx = tx)
            }
        }
    }
}

// ----------------- SUB SCREEN: CUSTOMER CARE -----------------

@Composable
fun MoreCustomerCareSubScreen(
    subject: String,
    message: String,
    isSent: Boolean,
    onSubjectChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Customer Care", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Quick contact cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+256701000111"))
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Uganda Helpline", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text("+256 701 000111", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = DeepTeal)
                        Text("Mon - Sat (8am - 6pm)", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Office Location", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text("Kampala, Uganda", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = DeepTeal)
                        Text("Plot 14 Nakasero Rd", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }

            // Ticket Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Send Message to Support",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = subject,
                        onValueChange = onSubjectChange,
                        label = { Text("Subject") },
                        placeholder = { Text("e.g., Question about kettle task rating") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = onMessageChange,
                        label = { Text("Description") },
                        placeholder = { Text("Describe your query or feedback...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onSend,
                        enabled = message.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("send_support_ticket_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send to Kampala Desk", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // FAQs Accordion / List
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )

            FaqCard(
                question = "When will Mobile Money payouts be live?",
                answer = "MTN MoMo and Airtel Money Uganda payouts are being integrated in Phase 2. Your UGX rewards accumulate safely in your balance until activation."
            )

            FaqCard(
                question = "What if a household utensil photo is unclear?",
                answer = "You can leave an observation note or select 'Scratched / Damaged' in the inspection form to help moderation."
            )
        }
    }
}

@Composable
fun FaqCard(question: String, answer: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = answer, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 16.sp)
        }
    }
}

// ----------------- SUB SCREEN: SECURITY -----------------

@Composable
fun MoreSecuritySubScreen(
    biometricsEnabled: Boolean,
    pinLockEnabled: Boolean,
    onToggleBiometrics: () -> Unit,
    onTogglePinLock: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Security & Privacy", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = "PIN", tint = DeepTeal, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Require App PIN Lock", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Protects wallet balance upon launch", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = pinLockEnabled,
                            onCheckedChange = { onTogglePinLock() },
                            colors = SwitchDefaults.colors(checkedThumbColor = WarmWhite, checkedTrackColor = DeepTeal)
                        )
                    }

                    HorizontalDivider(color = WarmCreamBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = "Biometric", tint = DeepTeal, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Biometric Authentication", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Fingerprint or Face Unlock on Android", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = biometricsEnabled,
                            onCheckedChange = { onToggleBiometrics() },
                            colors = SwitchDefaults.colors(checkedThumbColor = WarmWhite, checkedTrackColor = DeepTeal)
                        )
                    }
                }
            }

            // Active Sessions
            Text(
                text = "Active Device Sessions in Uganda",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Current Device (Android)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Kampala, Uganda • Active Now", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                    }
                    StatusBadge(text = "This Device")
                }
            }
        }
    }
}

// ----------------- SUB SCREEN: PROFILE -----------------

@Composable
fun MoreProfileSubScreen(
    userProfile: UserProfileEntity?,
    showDistrictDialog: Boolean,
    onBack: () -> Unit,
    onShowDistrictDialog: (Boolean) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onToggleRole: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(title = "Profile & Account", onBack = onBack)

        ProfileScreen(
            userProfile = userProfile,
            showDistrictDialog = showDistrictDialog,
            onShowDistrictDialog = onShowDistrictDialog,
            onDistrictSelected = onDistrictSelected,
            onToggleRole = onToggleRole,
            modifier = Modifier.weight(1f)
        )
    }
}

// ----------------- SUB SCREEN: SETTINGS -----------------

@Composable
fun MoreSettingsSubScreen(
    userProfile: UserProfileEntity?,
    biometricsEnabled: Boolean,
    pinLockEnabled: Boolean,
    showDistrictDialog: Boolean,
    onToggleBiometrics: () -> Unit,
    onTogglePinLock: () -> Unit,
    onShowDistrictDialog: (Boolean) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("more_settings_subscreen"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SubScreenHeader(title = "Account Settings", onBack = onBack)
        }

        // Account / Profile Card
        item {
            Text(
                text = "Profile & Account Information",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile?.fullName ?: "Nalubega Sarah",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Member ID: ${userProfile?.memberId ?: "HU7K4M92Q"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Phone: ${userProfile?.phoneNumber ?: "+256 772 458921"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    HorizontalDivider(color = WarmCreamBorder, modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onShowDistrictDialog(true) }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EditLocation, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Operating District", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("${userProfile?.district ?: "Kampala Central"}, Uganda (Tap to change)", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        }

        // Security Settings Card
        item {
            Text(
                text = "Security & Transaction PIN",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = "PIN", tint = DeepTeal, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("App PIN Lock Protection", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Protects wallet balance & P2P transfers", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = pinLockEnabled,
                            onCheckedChange = { onTogglePinLock() },
                            colors = SwitchDefaults.colors(checkedThumbColor = WarmWhite, checkedTrackColor = DeepTeal)
                        )
                    }

                    HorizontalDivider(color = WarmCreamBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = "Biometric", tint = DeepTeal, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Biometric Authentication", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Fingerprint or Face Unlock on Android", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = biometricsEnabled,
                            onCheckedChange = { onToggleBiometrics() },
                            colors = SwitchDefaults.colors(checkedThumbColor = WarmWhite, checkedTrackColor = DeepTeal)
                        )
                    }
                }
            }
        }

        // Notification Preferences
        item {
            Text(
                text = "Notification Preferences",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Task Review & Reward Alerts", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text("Instant push notices when daily tasks are approved", fontSize = 11.sp, color = TextSecondary)
                        }
                        StatusBadge(text = "Enabled")
                    }

                    HorizontalDivider(color = WarmCreamBorder, modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("P2P & Withdrawal Updates", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text("Real-time notifications for money sent or received", fontSize = 11.sp, color = TextSecondary)
                        }
                        StatusBadge(text = "Enabled")
                    }
                }
            }
        }

        // App & Privacy
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("HomeUtensils Uganda • Version 1.0.0", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Encrypted local session with strict role-based access control. No private credentials or identity documents are exposed on this device.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ----------------- COMMON HEADER -----------------

@Composable
fun SubScreenHeader(
    title: String,
    onBack: () -> Unit,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Surface(
        color = WarmWhite,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("subscreen_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepTeal
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
            }

            if (actionText != null && onAction != null) {
                TextButton(onClick = onAction) {
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                }
            }
        }
    }
}
