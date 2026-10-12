package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.components.MemberPaymentGuidanceCard
import com.example.ui.components.PackageBadgeChip
import com.example.ui.components.PackageTierIcon
import com.example.ui.components.PackageVisualTier
import com.example.ui.components.RewardBadge
import com.example.ui.components.RoyalCrownIcon
import com.example.ui.components.StatusBadge
import com.example.ui.components.UtensilThumbnail
import com.example.ui.components.formatUgx
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.PlusCrownGold
import com.example.ui.theme.PlusPurple
import com.example.ui.theme.PlusPurpleContainer
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldOnContainer
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite
import com.example.ui.viewmodel.AppNavTab
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Clean, simplified, and mobile-friendly Home Screen for HomeUtensils.
 * Designed specifically for ordinary Android phone users:
 * 1. Who am I? (Member Identity Card with photo, name, masked id/phone, package)
 * 2. Which package & is it active? (Prominent badge & clear ACTIVE / EXPIRED pill)
 * 3. When does it expire? (Package Expiry Card with activation date, expiry date, live countdown)
 * 4. Financial Summary (3 distinct cards: Total Income Earned, Paid Capital, Withdrawable Balance)
 * 5. Task Summary (Available tasks, Completed tasks, [START TASKS] button)
 * 6. Live Activity (Compact feed with genuine recorded platform events & View All)
 * 7. Expandable details (Package Details, Transaction History, Agent Details)
 */
@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    tasks: List<UtensilTaskEntity>,
    completedTaskCount: Int = 12,
    taskIncomeUgx: Long = userProfile?.walletBalanceUgx ?: 18500L,
    activationExpiryDate: String = "28 Oct 2026",
    daysRemaining: Int = 29,
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

    val isExpired = userProfile?.isPackageExpired == true ||
        (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0 && System.currentTimeMillis() >= userProfile.packageExpiresAt)

    // Accurate countdown time calculation using actual backend timestamps
    var currentTimeMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTimeMs = System.currentTimeMillis()
        }
    }

    val expiryTimestamp = userProfile?.packageExpiresAt ?: 0L
    val timeRemainingMs = if (expiryTimestamp > currentTimeMs) expiryTimestamp - currentTimeMs else 0L

    val daysLeft = timeRemainingMs / (1000L * 60 * 60 * 24)
    val hoursLeft = (timeRemainingMs / (1000L * 60 * 60)) % 24
    val minutesLeft = (timeRemainingMs / (1000L * 60)) % 60
    val secondsLeft = (timeRemainingMs / 1000L) % 60

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val expiryFormatted = remember(expiryTimestamp) {
        if (expiryTimestamp > 0) dateFormat.format(Date(expiryTimestamp)) else activationExpiryDate
    }

    // Determine activation date from timestamp or 30 days prior to expiry
    val activationFormatted = remember(expiryTimestamp) {
        if (expiryTimestamp > 0) {
            val actMs = expiryTimestamp - (30L * 24 * 60 * 60 * 1000L)
            dateFormat.format(Date(actMs))
        } else {
            "12 Apr 2025"
        }
    }

    // Masked user ID and phone
    val maskedUserId = remember(userProfile?.memberId) {
        val mid = userProfile?.memberId ?: "HU7K4M92Q"
        if (mid.length > 5) "@" + mid.lowercase() else "@" + mid.lowercase()
    }
    val maskedPhone = remember(userProfile?.phoneNumber) {
        val phone = userProfile?.phoneNumber ?: "+256 772 458921"
        if (phone.length >= 10) {
            phone.take(4) + " 7XX XXX " + phone.takeLast(3)
        } else {
            "+256 7XX XXX 123"
        }
    }

    val availableCount = tasks.count { it.status == "ACTIVE" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FA)) // Clean, modern light backdrop
            .statusBarsPadding()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ==========================================
        // A. TOP COMPACT HEADER (Logo, Welcome, Name, Bell)
        // ==========================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_top_header"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
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
                                contentDescription = "HomeUtensils Logo",
                                tint = SoftGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Welcome back,",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = userProfile?.fullName ?: "Sarah N. Okello",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Notification Bell with unread badge
                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("home_notification_bell")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotificationsCount > 0) {
                                        Badge(
                                            containerColor = Color(0xFFE53935),
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = "$unreadNotificationsCount",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = DeepTealDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Logout / Switch button
                        IconButton(
                            onClick = onLogoutClick,
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("home_logout_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sign Out",
                                tint = Color(0xFF718096),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // B. MEMBER IDENTITY CARD
        // Profile photo, name, username/masked ID, contact, package name & status
        // ==========================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("member_identity_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Photo + Info
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Generated Profile Avatar Photo
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer)
                                .border(2.dp, SoftGold.copy(alpha = 0.8f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val context = LocalContext.current
                            val avatarResId = remember {
                                val id = context.resources.getIdentifier(
                                    "img_sarah_avatar_1791616150174",
                                    "drawable",
                                    context.packageName
                                )
                                if (id != 0) id else R.drawable.ic_launcher_foreground
                            }
                            Image(
                                painter = painterResource(id = avatarResId),
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            // Masked User ID / Username
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = maskedUserId,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepTealDark
                                )
                            }

                            // Contact number (masked)
                            Text(
                                text = maskedPhone,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )

                            // Country / Location
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(StatusGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = userProfile?.district ?: "Uganda",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // Right Side: Prominent Package Name & Status Pill
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Prominent Package Badge (e.g. 👑 PLUS, STANDARD, STARTER)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isExpired) WarningRedContainer else Color(0xFF007A65)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("prominent_package_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (visualTier.name.contains("PLUS", ignoreCase = true)) {
                                    RoyalCrownIcon(
                                        modifier = Modifier.size(15.dp),
                                        crownColor = PlusCrownGold,
                                        baseColor = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Text(
                                    text = visualTier.displayTitle.uppercase(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Status Pill (ACTIVE or EXPIRED)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isExpired) WarningRedContainer else StatusGreenContainer
                                )
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                                .testTag("package_status_pill")
                        ) {
                            Text(
                                text = if (isExpired) "EXPIRED" else "ACTIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpired) WarningRedText else StatusGreenText
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // C. PACKAGE EXPIRY CARD
        // Activation Date, Expiry Date, Remaining Days / Countdown, Status Indicator
        // ==========================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("package_expiry_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpired) Color(0xFFFFF5F5) else Color(0xFFE8F6F3)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isExpired) WarningRed.copy(alpha = 0.4f) else Color(0xFFB2DFDB)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Calendar icon + Expiry Dates
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF00796B).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Package Expiry",
                                    tint = DeepTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Package Expiry",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                Text(
                                    text = "Activated: $activationFormatted",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Expires: $expiryFormatted",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }

                        // Right: Remaining Days + Detailed Countdown
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(0.9f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(WarmWhite)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = if (isExpired) WarningRed else DeepTeal,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isExpired) "Expired" else "Remaining",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Text(
                                        text = if (isExpired) "0 Days" else "$daysLeft Days",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isExpired) WarningRedText else DeepTealDark
                                    )
                                    if (!isExpired && timeRemainingMs > 0) {
                                        Text(
                                            text = "${hoursLeft}h ${minutesLeft}m ${secondsLeft}s",
                                            fontSize = 10.sp,
                                            color = TextSecondary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Status Indicator
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isExpired) WarningRedContainer else Color(0xFF00796B)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isExpired) "EXPIRED" else "ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isExpired) WarningRedText else Color.White
                                )
                            }
                        }
                    }

                    // If expired, show clear Reactivate Call to Action
                    if (isExpired) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onReactivateClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_reactivate_home"),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reactivate Package", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onToggleSimulateExpiry,
                                modifier = Modifier.testTag("btn_toggle_expiry"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text("Restore Active", fontSize = 11.sp, color = DeepTeal)
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // D. FINANCIAL SUMMARY (3 Clear Separate Cards)
        // 1. Total Income Earned
        // 2. Paid Capital (Informational, non-withdrawable)
        // 3. Withdrawable Balance (Available to cash out)
        // ==========================================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Your Financial Summary",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Card 1: Total Income Earned (Blue)
                    FinancialSummaryCard(
                        title = "Total Income Earned",
                        amountUgx = userProfile?.totalEarnedUgx ?: 185000L,
                        icon = Icons.Default.MonetizationOn,
                        iconBgColor = Color(0xFF1E88E5),
                        containerColor = Color(0xFFF0F7FF),
                        borderColor = Color(0xFFBBDEFB),
                        onClick = { onNavigateTab(AppNavTab.WALLET) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_total_income"
                    )

                    // Card 2: Paid Capital (Purple)
                    FinancialSummaryCard(
                        title = "Paid Capital",
                        amountUgx = userProfile?.paidCapitalUgx ?: 100000L,
                        icon = Icons.Default.Savings,
                        iconBgColor = Color(0xFF8E24AA),
                        containerColor = Color(0xFFFBF4FF),
                        borderColor = Color(0xFFE1BEE7),
                        onClick = { showPackageDetailsDialog = true },
                        modifier = Modifier.weight(1f),
                        testTag = "card_paid_capital"
                    )

                    // Card 3: Withdrawable Balance (Green)
                    FinancialSummaryCard(
                        title = "Withdrawable Balance",
                        amountUgx = userProfile?.walletBalanceUgx ?: 42500L,
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

        // ==========================================
        // E. TASK SUMMARY CARD
        // Available Tasks, Completed Tasks, [▶ START TASKS] Button
        // ==========================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_task_summary_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7)), // Warm peach/gold
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE0B2)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Title with Clipboard Icon
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE65100).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Kitchen,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Task Summary",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD84315)
                            )
                        }

                        // Prominent START TASKS Button
                        Button(
                            onClick = { onNavigateTab(AppNavTab.TASKS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF4511E)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_start_tasks_home")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "START TASKS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Available Tasks Stat
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Available Tasks",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$availableCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTealDark
                            )
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color(0xFFFFCC80))
                        )

                        // Completed Tasks Stat
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Completed Tasks",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$completedTaskCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTealDark
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // F. LIVE ACTIVITY (Genuine Platform Events)
        // Compact list of recent events with View All link
        // ==========================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_live_activity_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Activity",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showAllLiveActivityDialog = true }
                                .padding(4.dp)
                                .testTag("btn_view_all_live_activity")
                        ) {
                            Text(
                                text = "View All",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View All",
                                tint = DeepTeal,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Genuine Activity Events (Up to 5)
                    val displayedEvents = liveActivityEvents.take(5)
                    if (displayedEvents.isEmpty()) {
                        Text(
                            text = "No recent activity recorded yet.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        displayedEvents.forEachIndexed { index, event ->
                            if (index > 0) {
                                HorizontalDivider(
                                    color = Color(0xFFF1F5F9),
                                    thickness = 0.8.dp,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                            LiveActivityRowItem(event = event)
                        }
                    }
                }
            }
        }

        // ==========================================
        // G. EXPANDABLE DETAILS / QUICK LINKS
        // View Package Details, View Transaction History, Agent Payment Details
        // ==========================================
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Package Details Link Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showPackageDetailsDialog = true }
                        .testTag("btn_view_package_details"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF8FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBEE3F8))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF2B6CB0),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "View Package Details",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B6CB0)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF2B6CB0),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // View Transaction History Link Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTab(AppNavTab.WALLET) }
                        .testTag("btn_view_transaction_history"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D8FD))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = Color(0xFF6B46C1),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "View Transactions",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B46C1)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF6B46C1),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        // Secondary Link: View Agent Payment Details / Find Agent (Clean expandable button)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Agent Payment & Protection Details",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }

    // ==========================================
    // DIALOGS: Full Live Activity, Package Details, Agent Protection Details
    // ==========================================
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

/**
 * Clean Single Financial Card for Income, Capital, and Balance
 */
@Composable
private fun FinancialSummaryCard(
    title: String,
    amountUgx: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBgColor: Color,
    containerColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Details",
                    tint = TextSecondary,
                    modifier = Modifier.size(12.dp)
                )
            }

            Text(
                text = title,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formatUgx(amountUgx),
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepTealDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Single Live Activity Row Item matching user reference layout
 */
@Composable
private fun LiveActivityRowItem(event: LiveActivityEvent) {
    val timeAgo = remember(event.timestamp) {
        val diffMs = System.currentTimeMillis() - event.timestamp
        val minutes = diffMs / (1000 * 60)
        val hours = minutes / 60
        val days = hours / 24
        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "1 day ago"
            else -> "$days days ago"
        }
    }

    val (icon, iconBg) = when (event.eventType) {
        "TASK_REWARD" -> Pair(Icons.Default.Kitchen, Color(0xFF00897B))
        "REFERRAL_BONUS" -> Pair(Icons.Default.GroupAdd, Color(0xFF1E88E5))
        "PACKAGE_ACTIVATION" -> Pair(Icons.Default.CheckCircle, Color(0xFF8E24AA))
        "P2P_TRANSFER" -> Pair(Icons.Default.MonetizationOn, Color(0xFFFB8C00))
        "WITHDRAWAL" -> Pair(Icons.Default.AccountBalanceWallet, Color(0xFFE53935))
        else -> Pair(Icons.Default.Check, DeepTeal)
    }

    val isPositive = event.eventType != "P2P_TRANSFER" && event.eventType != "WITHDRAWAL"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBg.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconBg,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = event.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = event.detail,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            val amountRegex = remember { Regex("UGX\\s*([0-9,]+)") }
            val match = remember(event.detail) { amountRegex.find(event.detail) }
            if (match != null) {
                val amountStr = match.groupValues[1].replace(",", "")
                val amt = amountStr.toLongOrNull() ?: 0L
                if (amt > 0) {
                    Text(
                        text = (if (isPositive) "+ " else "- ") + formatUgx(amt),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isPositive) StatusGreenText else WarningRedText
                    )
                }
            }
            Text(
                text = timeAgo,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}

/**
 * Expandable Modal Dialog for detailed package terms & history
 */
@Composable
private fun PackageDetailsModalDialog(
    userProfile: UserProfileEntity?,
    tier: PackageVisualTier,
    onDismiss: () -> Unit,
    onUpgrade: () -> Unit,
    onReactivate: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PackageTierIcon(tier = tier, modifier = Modifier.size(24.dp), iconSize = 24.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${tier.displayTitle} Package",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepTealDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("• Paid Capital: ${formatUgx(userProfile?.paidCapitalUgx ?: tier.priceUgx)} (Safely recorded working capital)", fontSize = 12.sp)
                Text("• Task Reward: ${tier.taskRewardDisplay} per verified utensil review", fontSize = 12.sp)
                Text("• Package Duration: 30 active days per cycle", fontSize = 12.sp)
                Text("• P2P Transfers: Supported 24/7 with zero waiting time", fontSize = 12.sp)
                Text("• Withdrawals: Monday–Saturday, 6:00 AM–8:00 PM (UGX 10,000 minimum)", fontSize = 12.sp)
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
            OutlinedButton(onClick = onDismiss) {
                Text("Close", fontSize = 12.sp)
            }
        }
    )
}

/**
 * Expandable Modal Dialog for Agent payment protection instructions & links
 */
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

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agent Services & Protection", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepTealDark)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("• 0% fee on all agent-facilitated package activations & reactivations.", fontSize = 12.sp)
                Text("• Full Agent Payment Protection guarantees your funds are safely accounted for.", fontSize = 12.sp)
                Text("• Certified field agents operate across all Uganda districts.", fontSize = 12.sp)
                if (isPlus) {
                    Text("• As a Plus Package member, you are eligible to apply to become an authorized regional Field Agent.", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DeepTeal)
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
            if (isPlus) {
                OutlinedButton(
                    onClick = if (agentApplication?.status == "APPROVED") onOpenAgentPortal else onApplyAgent
                ) {
                    Text(
                        if (agentApplication?.status == "APPROVED") "Agent Center" else "Apply as Agent",
                        fontSize = 12.sp
                    )
                }
            } else {
                OutlinedButton(onClick = onDismiss) {
                    Text("Close", fontSize = 12.sp)
                }
            }
        }
    )
}
