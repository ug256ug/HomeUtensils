
package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.LiveActivityEvent
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.ui.components.AllLiveActivityDialog
import com.example.ui.components.PackageTierIcon
import com.example.ui.components.PackageVisualTier
import com.example.ui.components.RoyalCrownIcon
import com.example.ui.components.formatUgx
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.PlusCrownGold
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText
import com.example.ui.theme.WarmWhite
import com.example.ui.viewmodel.AppNavTab
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    tasks: List<UtensilTaskEntity>,
    completedTaskCount: Int = 0,
    taskIncomeUgx: Long = userProfile?.walletBalanceUgx ?: 0L,
    activationExpiryDate: String = "Not available",
    daysRemaining: Int = 0,
    onNavigateTab: (AppNavTab) -> Unit,
    onTaskClicked: (UtensilTaskEntity) -> Unit,
    onOpenGiftCode: () -> Unit,
    onOpenReferrals: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onUpgradeClick: () -> Unit = {},
    onReactivateClick: () -> Unit = {},
    onToggleSimulateExpiry: () -> Unit = {},
    unreadNotificationsCount: Int = 0,
    agentApplication: AgentApplicationEntity? = null,
    onApplyAgentClick: () -> Unit = {},
    onOpenAgentPortal: () -> Unit = {},
    activeActivationRequests: List<AgentActivationRequestEntity> = emptyList(),
    onFindAgentClick: () -> Unit = {},
    onReportPaymentClick: (AgentActivationRequestEntity) -> Unit = {},
    onReportIssueClick: (AgentActivationRequestEntity) -> Unit = {},
    liveActivityEvents: List<LiveActivityEvent> = emptyList(),
    modifier: Modifier = Modifier
) {
    var showAllLiveActivityDialog by remember { mutableStateOf(false) }
    var showPackageDetailsDialog by remember { mutableStateOf(false) }
    var showAgentDetailsDialog by remember { mutableStateOf(false) }

    val visualTier = remember(userProfile?.packageName) {
        PackageVisualTier.fromPackageName(userProfile?.packageName)
    }

    var currentTimeMs by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTimeMs = System.currentTimeMillis()
        }
    }

    val expiryTimestamp = userProfile?.packageExpiresAt ?: 0L
    val hasProfile = userProfile != null
    val hasExpiryTimestamp = expiryTimestamp > 0L
    val expiryPassed = hasExpiryTimestamp && currentTimeMs >= expiryTimestamp
    val isExpired = userProfile?.isPackageExpired == true || expiryPassed
    val isPackageActive = hasProfile && hasExpiryTimestamp && !isExpired

    val timeRemainingMs = if (hasExpiryTimestamp && expiryTimestamp > currentTimeMs) {
        expiryTimestamp - currentTimeMs
    } else {
        0L
    }

    val daysLeft = timeRemainingMs / 86_400_000L
    val hoursLeft = (timeRemainingMs / 3_600_000L) % 24
    val minutesLeft = (timeRemainingMs / 60_000L) % 60
    val secondsLeft = (timeRemainingMs / 1_000L) % 60

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val expiryFormatted = remember(expiryTimestamp, activationExpiryDate) {
        if (expiryTimestamp > 0L) {
            dateFormat.format(Date(expiryTimestamp))
        } else {
            activationExpiryDate
        }
    }

    val activationFormatted = remember(expiryTimestamp) {
        if (expiryTimestamp > 0L) {
            val activationMs = expiryTimestamp - 30L * 24L * 60L * 60L * 1000L
            dateFormat.format(Date(activationMs))
        } else {
            "Not available"
        }
    }

    val maskedUserId = remember(userProfile?.memberId) {
        userProfile?.memberId?.takeIf { it.isNotBlank() }?.let { id ->
            if (id.length > 5) "@${id.lowercase()}" else id
        } ?: "Not available"
    }

    val maskedPhone = remember(userProfile?.phoneNumber) {
        val phone = userProfile?.phoneNumber.orEmpty()
        when {
            phone.length >= 7 -> "${phone.take(4)} XXX XXX ${phone.takeLast(3)}"
            phone.isNotBlank() -> "Hidden"
            else -> "Not available"
        }
    }

    val availableCount = tasks.count { it.status == "ACTIVE" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FA))
            .statusBarsPadding()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 12.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("home_top_header"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DeepTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Kitchen,
                                contentDescription = "HomeUtensils logo",
                                tint = Color(0xFFFFD76A),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                "Welcome back,",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                userProfile?.fullName?.takeIf { it.isNotBlank() }
                                    ?: "Member",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier.size(38.dp).testTag("home_notification_bell")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotificationsCount > 0) {
                                        Badge {
                                            Text(
                                                unreadNotificationsCount.toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = DeepTealDark
                                )
                            }
                        }

                        IconButton(
                            onClick = onLogoutClick,
                            modifier = Modifier.size(38.dp).testTag("home_logout_button")
                        ) {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = "Sign out",
                                tint = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("member_identity_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val context = LocalContext.current
                        val avatarResId = remember {
                            val resourceId = context.resources.getIdentifier(
                                "img_sarah_avatar_1791616150174",
                                "drawable",
                                context.packageName
                            )
                            if (resourceId != 0) resourceId
                            else R.drawable.ic_launcher_foreground
                        }

                        Image(
                            painter = painterResource(id = avatarResId),
                            contentDescription = "Profile image",
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer)
                                .border(2.dp, Color(0xFFFFD76A), CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                maskedUserId,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepTealDark
                            )
                            Text(
                                maskedPhone,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                userProfile?.district?.takeIf { it.isNotBlank() }
                                    ?: "Location not available",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DeepTeal)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .testTag("prominent_package_badge")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (visualTier.displayTitle.contains("PLUS", true)) {
                                    RoyalCrownIcon(
                                        modifier = Modifier.size(15.dp),
                                        crownColor = PlusCrownGold,
                                        baseColor = Color.White
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Spacer(Modifier.width(4.dp))

                                Text(
                                    visualTier.displayTitle.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

                        val statusText = when {
                            !hasProfile -> "ACCOUNT UNKNOWN"
                            isExpired -> "EXPIRED"
                            !hasExpiryTimestamp -> "UNCONFIRMED"
                            else -> "ACTIVE"
                        }

                        val statusBackground = when {
                            isExpired -> WarningRedContainer
                            isPackageActive -> StatusGreenContainer
                            else -> Color(0xFFFFF3CD)
                        }

                        val statusTextColor = when {
                            isExpired -> WarningRedText
                            isPackageActive -> StatusGreenText
                            else -> Color(0xFF795500)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(statusBackground)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("package_status_pill")
                        ) {
                            Text(
                                statusText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusTextColor
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("package_expiry_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpired) Color(0xFFFFF5F5) else Color(0xFFE8F6F3)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isExpired) WarningRed.copy(alpha = 0.4f)
                    else Color(0xFFB2DFDB)
                )
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Package dates",
                                tint = DeepTeal,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            Column {
                                Text(
                                    "Package Expiry",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                Text(
                                    "Activated: $activationFormatted",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    "Expires: $expiryFormatted",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(Modifier.width(6.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                when {
                                    isExpired -> "Expired"
                                    isPackageActive -> "Remaining"
                                    else -> "Unconfirmed"
                                },
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            Text(
                                when {
                                    isExpired -> "0 days"
                                    isPackageActive -> "$daysLeft days"
                                    else -> "—"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isExpired) WarningRedText else DeepTealDark
                            )

                            if (isPackageActive && timeRemainingMs > 0L) {
                                Text(
                                    "${hoursLeft}h ${minutesLeft}m ${secondsLeft}s",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    if (isExpired) {
                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = onReactivateClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_reactivate_home"),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Autorenew,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Reactivate Package")
                        }
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Your Financial Summary",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    FinancialSummaryCard(
                        title = "Total Income",
                        amountUgx = userProfile?.totalEarnedUgx,
                        icon = Icons.Default.MonetizationOn,
                        iconBgColor = Color(0xFF1E88E5),
                        containerColor = Color(0xFFF0F7FF),
                        borderColor = Color(0xFFBBDEFB),
                        onClick = { onNavigateTab(AppNavTab.WALLET) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_total_income"
                    )

                    FinancialSummaryCard(
                        title = "Paid Capital",
                        amountUgx = userProfile?.paidCapitalUgx,
                        icon = Icons.Default.Savings,
                        iconBgColor = Color(0xFF8E24AA),
                        containerColor = Color(0xFFFBF4FF),
                        borderColor = Color(0xFFE1BEE7),
                        onClick = { showPackageDetailsDialog = true },
                        modifier = Modifier.weight(1f),
                        testTag = "card_paid_capital"
                    )

                    FinancialSummaryCard(
                        title = "Withdrawable",
                        amountUgx = userProfile?.walletBalanceUgx,
                        icon = Icons.Default.AccountBalanceWallet,
                        iconBgColor = Color(0xFF00897B),
                        containerColor = Color(0xFFE8F6F3),
                        borderColor = Color(0xFFB2DFDB),
                        onClick = { onNavigateTab(AppNavTab.WALLET) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_withdrawable_balance"
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("home_task_summary_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE0B2))
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Kitchen,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Task Summary",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD84315)
                            )
                        }

                        Button(
                            onClick = { onNavigateTab(AppNavTab.TASKS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF4511E)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_start_tasks_home")
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("START TASKS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Available Tasks", fontSize = 11.sp, color = TextSecondary)
                            Text(
                                availableCount.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTealDark
                            )
                        }

                        Box(
                            Modifier.width(1.dp).height(34.dp).background(Color(0xFFFFCC80))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Completed Tasks", fontSize = 11.sp, color = TextSecondary)
                            Text(
                                completedTaskCount.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTealDark
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("home_live_activity_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = DeepTeal
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Live Activity",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                        }

                        Row(
                            modifier = Modifier
                                .clickable { showAllLiveActivityDialog = true }
                                .padding(4.dp)
                                .testTag("btn_view_all_live_activity"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("View All", fontSize = 11.sp, color = DeepTeal)
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View all activity",
                                tint = DeepTeal,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    val displayedEvents = liveActivityEvents.take(5)

                    if (displayedEvents.isEmpty()) {
                        Text(
                            "No recent activity recorded yet.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        displayedEvents.forEachIndexed { index, event ->
                            if (index > 0) {
                                HorizontalDivider(
                                    color = Color(0xFFF1F5F9),
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                            LiveActivityRowItem(event)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickLinkCard(
                    title = "Package Details",
                    icon = Icons.Default.Info,
                    background = Color(0xFFEBF8FF),
                    foreground = Color(0xFF2B6CB0),
                    modifier = Modifier.weight(1f),
                    testTag = "btn_view_package_details",
                    onClick = { showPackageDetailsDialog = true }
                )

                QuickLinkCard(
                    title = "Transactions",
                    icon = Icons.Default.ReceiptLong,
                    background = Color(0xFFFAF5FF),
                    foreground = Color(0xFF6B46C1),
                    modifier = Modifier.weight(1f),
                    testTag = "btn_view_transaction_history",
                    onClick = { onNavigateTab(AppNavTab.WALLET) }
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAgentDetailsDialog = true }
                    .testTag("btn_view_agent_details"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = DeepTeal
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Agent Services & Help",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }
        }
    }

    if (showAllLiveActivityDialog) {
        AllLiveActivityDialog(
            events = liveActivityEvents,
            onDismiss = { showAllLiveActivityDialog = false }
        )
    }

    if (showPackageDetailsDialog) {
        PackageDetailsModalDialog(
            userProfile = userProfile,
            tier = visualTier,
            onDismiss = { showPackageDetailsDialog = false },
            onUpgrade = {
                showPackageDetailsDialog = false
                onUpgradeClick()
            },
            onReactivate = {
                showPackageDetailsDialog = false
                onReactivateClick()
            }
        )
    }

    if (showAgentDetailsDialog) {
        AgentDetailsModalDialog(
            userProfile = userProfile,
            agentApplication = agentApplication,
            activeRequests = activeActivationRequests,
            onDismiss = { showAgentDetailsDialog = false },
            onFindAgent = {
                showAgentDetailsDialog = false
                onFindAgentClick()
            },
            onApplyAgent = {
                showAgentDetailsDialog = false
                onApplyAgentClick()
            },
            onOpenAgentPortal = {
                showAgentDetailsDialog = false
                onOpenAgentPortal()
            }
        )
    }
}

@Composable
private fun FinancialSummaryCard(
    title: String,
    amountUgx: Long?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBgColor: Color,
    containerColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier.clickable { onClick() }.testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                title,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                minLines = 2
            )

            Text(
                text = amountUgx?.let { formatUgx(it) } ?: "Not available",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepTealDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickLinkCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    foreground: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick).testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = androidx.compose.foundation.BorderStroke(1.dp, foreground.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = foreground,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = foreground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = foreground,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
private fun LiveActivityRowItem(event: LiveActivityEvent) {
    var currentTimeMs by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(event.timestamp) {
        while (true) {
            delay(60_000L)
            currentTimeMs = System.currentTimeMillis()
        }
    }

    val diffMs = (currentTimeMs - event.timestamp).coerceAtLeast(0L)
    val minutes = diffMs / 60_000L
    val hours = minutes / 60L
    val days = hours / 24L

    val timeAgo = when {
        minutes < 1L -> "Just now"
        minutes < 60L -> "$minutes min ago"
        hours < 24L -> "$hours hours ago"
        days == 1L -> "1 day ago"
        else -> "$days days ago"
    }

    val iconAndColor = when (event.eventType) {
        "TASK_REWARD" -> Pair(Icons.Default.Kitchen, Color(0xFF00897B))
        "REFERRAL_BONUS" -> Pair(Icons.Default.GroupAdd, Color(0xFF1E88E5))
        "PACKAGE_ACTIVATION" -> Pair(Icons.Default.CheckCircle, Color(0xFF8E24AA))
        "P2P_TRANSFER" -> Pair(Icons.Default.MonetizationOn, Color(0xFFFB8C00))
        "WITHDRAWAL" -> Pair(Icons.Default.AccountBalanceWallet, Color(0xFFE53935))
        else -> Pair(Icons.Default.Check, DeepTeal)
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconAndColor.second.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    iconAndColor.first,
                    contentDescription = null,
                    tint = iconAndColor.second,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    event.detail,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            val amountRegex = remember { Regex("UGX\\s*([0-9,]+)") }
            val match = remember(event.detail) { amountRegex.find(event.detail) }
            val amount = match?.groupValues?.getOrNull(1)
                ?.replace(",", "")
                ?.toLongOrNull()

            if (amount != null && amount > 0L) {
                val isOutgoing = event.eventType == "P2P_TRANSFER" ||
                    event.eventType == "WITHDRAWAL"

                Text(
                    text = (if (isOutgoing) "− " else "+ ") + formatUgx(amount),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isOutgoing) WarningRedText else StatusGreenText
                )
            }

            Text(timeAgo, fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun PackageDetailsModalDialog(
    userProfile: UserProfileEntity?,
    tier: PackageVisualTier,
    onDismiss: () -> Unit,
    onUpgrade: () -> Unit,
    onReactivate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PackageTierIcon(
                    tier = tier,
                    modifier = Modifier.size(24.dp),
                    iconSize = 24.dp
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "${tier.displayTitle} Package",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepTealDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Recorded paid capital: ${
                        userProfile?.paidCapitalUgx?.let { formatUgx(it) } ?: "Not available"
                    }",
                    fontSize = 12.sp
                )
                Text(
                    "Task reward: ${tier.taskRewardDisplay} per eligible utensil review.",
                    fontSize = 12.sp
                )
                Text(
                    "Package duration and expiry depend on the activation recorded for your account.",
                    fontSize = 12.sp
                )
                Text(
                    "Check the Wallet screen for current transaction and withdrawal information.",
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onUpgrade,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text("Upgrade Package", fontSize = 12.sp)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (userProfile?.isPackageExpired == true) {
                    OutlinedButton(onClick = onReactivate) {
                        Text("Reactivate", fontSize = 12.sp)
                    }
                }
                OutlinedButton(onClick = onDismiss) {
                    Text("Close", fontSize = 12.sp)
                }
            }
        }
    )
}

@Composable
private fun AgentDetailsModalDialog(
    userProfile: UserProfileEntity?,
    agentApplication: AgentApplicationEntity?,
    activeRequests: List<AgentActivationRequestEntity>,
    onDismiss: () -> Unit,
    onFindAgent: () -> Unit,
    onApplyAgent: () -> Unit,
    onOpenAgentPortal: () -> Unit
) {
    val isPlus = userProfile?.packageName?.contains("PLUS", ignoreCase = true) == true
    val isApproved = agentApplication?.status == "APPROVED"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.SupportAgent,
                    contentDescription = null,
                    tint = DeepTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Agent Services & Help",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepTealDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Use the agent service to get assistance with package activation or account questions.",
                    fontSize = 12.sp
                )
                Text(
                    "Confirm any agent fees, payment instructions, and protection terms through official platform support before sending money.",
                    fontSize = 12.sp
                )
                Text(
                    "Pending activation requests: ${activeRequests.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (isPlus) {
                    Text(
                        if (isApproved) {
                            "Your agent application is approved."
                        } else {
                            "You may submit an agent application for review."
                        },
                        fontSize = 12.sp,
                        color = DeepTeal
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onFindAgent,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text("Find an Agent", fontSize = 12.sp)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (isPlus) {
                    OutlinedButton(
                        onClick = if (isApproved) onOpenAgentPortal else onApplyAgent
                    ) {
                        Text(
                            if (isApproved) "Agent Center" else "Apply as Agent",
                            fontSize = 12.sp
                        )
                    }
                }
                OutlinedButton(onClick = onDismiss) {
                    Text("Close", fontSize = 12.sp)
                }
            }
        }
    )
}
