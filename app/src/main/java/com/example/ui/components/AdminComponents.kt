package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.WithdrawalRequestEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Vibrant Royal Blue theme colors matching mockup
val AdminPrimaryBlue = Color(0xFF0A60D4)
val AdminPrimaryBlueDark = Color(0xFF0748A4)
val AdminPrimaryBlueLight = Color(0xFFE0EDFE)

val AdminMetricBlue = Color(0xFF2563EB)
val AdminMetricGreen = Color(0xFF10B981)
val AdminMetricPurple = Color(0xFF7C3AED)
val AdminMetricOrange = Color(0xFFF59E0B)
val AdminMetricRed = Color(0xFFEF4444)
val AdminMetricTeal = Color(0xFF0D9488)

val AdminStatusYellowBg = Color(0xFFFEF3C7)
val AdminStatusYellowText = Color(0xFFD97706)
val AdminStatusGreenBg = Color(0xFFD1FAE5)
val AdminStatusGreenText = Color(0xFF059669)
val AdminStatusRedBg = Color(0xFFFEE2E2)
val AdminStatusRedText = Color(0xFFDC2626)

/**
 * Top Branded Header for HomeUtensils Admin Center
 */
@Composable
fun AdminTopHeader(
    adminUser: String,
    unreadNotificationsCount: Int,
    onNotificationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onOpenHolidayCalendar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var profileMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AdminPrimaryBlue)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("admin_top_header")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Logo & Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Kitchen,
                        contentDescription = "HomeUtensils Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "HomeUtensils",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Admin Center",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Right: Notification Bell & Admin Profile Dropdown
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notification Bell with Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onNotificationsClick() }
                        .testTag("btn_admin_notifications"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )

                    if (unreadNotificationsCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unreadNotificationsCount.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Administrator Profile Pill
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { profileMenuExpanded = true }
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("btn_admin_profile_pill"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AdminPrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Admin",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "System Administrator",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = profileMenuExpanded,
                        onDismissRequest = { profileMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Signed in as: ${adminUser.ifBlank { "admin@homeutensils.ug" }}", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            onClick = { }
                        )
                        DropdownMenuItem(
                            text = { Text("Role: Super Administrator", fontSize = 12.sp) },
                            onClick = { }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Uganda Holiday Calendar", fontSize = 12.sp) },
                            onClick = {
                                profileMenuExpanded = false
                                onOpenHolidayCalendar()
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Logout & Exit Admin", fontSize = 12.sp, color = AdminMetricRed, fontWeight = FontWeight.Bold) },
                            onClick = {
                                profileMenuExpanded = false
                                onLogoutClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Welcome Banner Card matching mockup
 */
@Composable
fun AdminDashboardWelcomeCard(
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val now = remember { Date() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_welcome_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AdminPrimaryBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome, Admin!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Here's what's happening with HomeUtensils today.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = dateFormat.format(now),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = timeFormat.format(now),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Metric Summary Card
 */
@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    trendText: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("metric_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = trendText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdminMetricGreen
                )
            }
        }
    }
}

/**
 * Pending Withdrawal Requests Table Card matching mockup
 */
@Composable
fun AdminPendingWithdrawalsTable(
    withdrawals: List<WithdrawalRequestEntity>,
    onViewAll: () -> Unit,
    onRowClick: (WithdrawalRequestEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pendingList = withdrawals.filter { it.status == "PENDING" || it.status == "PROCESSING" }.take(4)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("table_pending_withdrawals"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AdminMetricPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Pending Withdrawal Requests",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                TextButton(
                    onClick = onViewAll,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("View All", fontSize = 11.sp, color = AdminPrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Member", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
                Text("Amount", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
                Text("Method", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
                Text("Status", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (pendingList.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No pending withdrawal requests", color = Color(0xFF94A3B8), fontSize = 11.sp)
                }
            } else {
                pendingList.forEach { req ->
                    val maskedContact = if (req.beneficiaryAccount.length >= 7) {
                        req.beneficiaryAccount.take(4) + "***" + req.beneficiaryAccount.takeLast(3)
                    } else req.beneficiaryAccount

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRowClick(req) }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = maskedContact,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = "UGX ${formatUgx(req.requestedAmountUgx)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = req.payoutMethodName.replace(" Uganda", ""),
                            fontSize = 11.sp,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(1.2f)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AdminStatusYellowBg)
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pending",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AdminStatusYellowText
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 0.8.dp)
                }
            }
        }
    }
}

/**
 * Agent Applications Table Card matching mockup
 */
@Composable
fun AdminAgentApplicationsTable(
    applications: List<AgentApplicationEntity>,
    onViewAll: () -> Unit,
    onRowClick: (AgentApplicationEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val displayList = applications.take(4)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("table_agent_applications"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AdminMetricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Agent Applications",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                TextButton(
                    onClick = onViewAll,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("View All", fontSize = 11.sp, color = AdminPrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Applicant", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.4f))
                Text("Type", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text("Status", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (displayList.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No agent applications received", color = Color(0xFF94A3B8), fontSize = 11.sp)
                }
            } else {
                displayList.forEach { app ->
                    val maskedContact = if (app.phoneNumber.length >= 7) {
                        app.phoneNumber.take(4) + "***" + app.phoneNumber.takeLast(3)
                    } else app.phoneNumber

                    val (bgCol, txtCol) = when (app.status.uppercase()) {
                        "APPROVED" -> Pair(AdminStatusGreenBg, AdminStatusGreenText)
                        "REJECTED" -> Pair(AdminStatusRedBg, AdminStatusRedText)
                        else -> Pair(AdminStatusYellowBg, AdminStatusYellowText)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRowClick(app) }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = maskedContact,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Agent",
                            fontSize = 11.sp,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgCol)
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = app.status.lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = txtCol
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 0.8.dp)
                }
            }
        }
    }
}

/**
 * Platform Activity (Last 7 Days) Compose Canvas Line Chart matching mockup
 */
@Composable
fun AdminPlatformActivityChart(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_platform_activity_chart"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header & Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Platform Activity (Last 7 Days)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChartLegendItem(color = AdminMetricBlue, label = "New Members")
                    ChartLegendItem(color = AdminMetricGreen, label = "Transactions")
                    ChartLegendItem(color = AdminMetricOrange, label = "Withdrawals")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Canvas Line Chart
            val dates = listOf("Apr 3", "Apr 4", "Apr 5", "Apr 6", "Apr 7", "Apr 8", "Apr 9")
            val seriesMembers = listOf(520f, 720f, 660f, 910f, 820f, 990f, 1200f)
            val seriesTx = listOf(280f, 410f, 420f, 570f, 450f, 630f, 780f)
            val seriesWth = listOf(120f, 180f, 210f, 290f, 200f, 340f, 410f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val paddingLeft = 40f
                    val paddingBottom = 30f
                    val paddingTop = 10f
                    val chartWidth = width - paddingLeft
                    val chartHeight = height - paddingBottom - paddingTop

                    val maxY = 1500f

                    // Draw subtle grid lines & Y labels
                    val yLevels = listOf(0f, 500f, 1000f, 1500f)
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#94A3B8")
                        textSize = 24f
                        isAntiAlias = true
                    }

                    yLevels.forEach { lvl ->
                        val y = paddingTop + chartHeight - (lvl / maxY) * chartHeight
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(paddingLeft, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                        val lbl = if (lvl == 0f) "0" else String.format("%,d", lvl.toInt())
                        drawContext.canvas.nativeCanvas.drawText(
                            lbl,
                            5f,
                            y + 8f,
                            textPaint
                        )
                    }

                    // Draw X-axis labels
                    val stepX = chartWidth / (dates.size - 1)
                    dates.forEachIndexed { i, d ->
                        val x = paddingLeft + i * stepX
                        val textWidth = textPaint.measureText(d)
                        drawContext.canvas.nativeCanvas.drawText(
                            d,
                            x - textWidth / 2f,
                            height - 5f,
                            textPaint
                        )
                    }

                    // Helper to draw series
                    fun drawSeries(values: List<Float>, strokeCol: Color) {
                        val path = Path()
                        val points = mutableListOf<Offset>()

                        values.forEachIndexed { i, v ->
                            val x = paddingLeft + i * stepX
                            val y = paddingTop + chartHeight - (v / maxY) * chartHeight
                            points.add(Offset(x, y))
                            if (i == 0) {
                                path.moveTo(x, y)
                            } else {
                                val prev = points[i - 1]
                                val cx = (prev.x + x) / 2f
                                path.cubicTo(cx, prev.y, cx, y, x, y)
                            }
                        }

                        // Stroke
                        drawPath(
                            path = path,
                            color = strokeCol,
                            style = Stroke(width = 4f)
                        )

                        // Points
                        points.forEach { pt ->
                            drawCircle(
                                color = strokeCol,
                                radius = 4f,
                                center = pt
                            )
                        }
                    }

                    // Draw 3 series matching mockup
                    drawSeries(seriesMembers, AdminMetricBlue)
                    drawSeries(seriesTx, AdminMetricGreen)
                    drawSeries(seriesWth, AdminMetricOrange)
                }
            }
        }
    }
}

@Composable
private fun ChartLegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Quick Actions Card matching mockup
 */
@Composable
fun AdminQuickActionsCard(
    onViewMembersClick: () -> Unit,
    onApproveAgentsClick: () -> Unit,
    onProcessWithdrawalsClick: () -> Unit,
    onManageGiftCodesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_quick_actions_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AdminMetricGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "Quick Actions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Manage the platform quickly",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Big Action Buttons (2x2 or 4 across)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    title = "View Members",
                    icon = Icons.Default.People,
                    containerColor = AdminMetricBlue,
                    onClick = onViewMembersClick,
                    modifier = Modifier.weight(1f)
                )

                QuickActionButton(
                    title = "Approve Agents",
                    icon = Icons.Default.PersonAdd,
                    containerColor = AdminMetricGreen,
                    onClick = onApproveAgentsClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    title = "Process Withdrawals",
                    icon = Icons.Default.AccountBalanceWallet,
                    containerColor = AdminMetricPurple,
                    onClick = onProcessWithdrawalsClick,
                    modifier = Modifier.weight(1f)
                )

                QuickActionButton(
                    title = "Manage Gift Codes",
                    icon = Icons.Default.CardGiftcard,
                    containerColor = Color(0xFFF97316),
                    onClick = onManageGiftCodesClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .testTag("quick_action_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}
