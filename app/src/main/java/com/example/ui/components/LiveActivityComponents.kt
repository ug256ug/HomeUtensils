package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveActivityEvent
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite
import kotlinx.coroutines.delay

fun formatEventTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000L -> "Just now"
        diff < 3600_000L -> "${diff / 60_000L}m ago"
        diff < 86400_000L -> "${diff / 3600_000L}h ago"
        diff < 172800_000L -> "Yesterday"
        else -> "${diff / 86400_000L}d ago"
    }
}

fun getEventIcon(iconType: String): ImageVector {
    return when (iconType) {
        "TASK" -> Icons.Default.CheckCircle
        "UPGRADE" -> Icons.Default.Upgrade
        "REACTIVATION" -> Icons.Default.Autorenew
        "PACKAGE" -> Icons.Default.Verified
        "P2P" -> Icons.Default.SwapHoriz
        "COMMISSION" -> Icons.Default.MonetizationOn
        "AGENT" -> Icons.Default.SupportAgent
        "WITHDRAWAL" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Verified
    }
}

fun getEventColors(iconType: String): Pair<Color, Color> {
    return when (iconType) {
        "TASK" -> Pair(StatusGreenContainer, StatusGreenText)
        "UPGRADE" -> Pair(SoftGoldContainer, DeepTealDark)
        "REACTIVATION" -> Pair(DeepTealContainer, DeepTeal)
        "PACKAGE" -> Pair(DeepTealContainer, DeepTealDark)
        "P2P" -> Pair(Color(0xFFE0F2F1), Color(0xFF00695C))
        "COMMISSION" -> Pair(SoftGoldContainer, SoftGoldDark)
        "AGENT" -> Pair(DeepTealContainer, DeepTealDark)
        "WITHDRAWAL" -> Pair(Color(0xFFEDE7F6), Color(0xFF512DA8))
        else -> Pair(DeepTealContainer, DeepTeal)
    }
}

@Composable
fun LiveActivityTickerCard(
    events: List<LiveActivityEvent>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag("home_live_activity_ticker"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StatusGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StatusGreenContainer)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE ACTIVITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StatusGreenText
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-Time Platform Feed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Listening for verified member package activations and task events...",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }

    // Auto-rotation timer: cycles through events every 3.5 seconds
    LaunchedEffect(events.size, isPaused) {
        if (!isPaused && events.size > 1) {
            while (true) {
                delay(3500)
                currentIndex = (currentIndex + 1) % events.size
            }
        }
    }

    val currentEvent = events.getOrNull(currentIndex % events.size) ?: events.first()
    val (bgColor, iconColor) = getEventColors(currentEvent.iconType)
    val eventIcon = getEventIcon(currentEvent.iconType)
    val relativeTime = formatEventTimeAgo(currentEvent.timestamp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("home_live_activity_ticker"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Pulsing Live Indicator, Title, and "View All" affordance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pulsing Green Live Dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StatusGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StatusGreenContainer)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE ACTIVITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StatusGreenText
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-Time Platform Feed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onViewAllClick() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .testTag("btn_view_live_activity_feed")
                ) {
                    Text(
                        text = "View Feed (${events.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Expand Live Activity",
                        tint = DeepTeal,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Event Row
            AnimatedContent(
                targetState = currentEvent,
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut()
                    )
                },
                label = "live_activity_event_transition"
            ) { event ->
                val (itemBg, itemColor) = getEventColors(event.iconType)
                val itemIcon = getEventIcon(event.iconType)
                val itemTime = formatEventTimeAgo(event.timestamp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmCreamBackground)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(itemBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = itemIcon,
                            contentDescription = event.eventType,
                            tint = itemColor,
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(DeepTealContainer)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = event.maskedMember,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepTealDark
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = event.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                            }

                            Text(
                                text = itemTime,
                                fontSize = 10.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = event.detail,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Footer: Verification guarantee & navigation dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Event",
                        tint = StatusGreenText,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "100% Genuine Recorded Platform Events • Private & Masked",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }

                // Progress dots (up to 5 dots)
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    val dotCount = minOf(events.size, 5)
                    repeat(dotCount) { dotIndex ->
                        val isSelected = (currentIndex % dotCount) == dotIndex
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 6.dp else 4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) DeepTeal else WarmCreamBorder)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AllLiveActivityDialog(
    events: List<LiveActivityEvent>,
    onDismiss: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredEvents = remember(events, selectedFilter) {
        when (selectedFilter) {
            "PACKAGES" -> events.filter { it.eventType.startsWith("PACKAGE") }
            "TASKS" -> events.filter { it.eventType == "TASK_REWARD" }
            "P2P" -> events.filter { it.eventType == "P2P_TRANSFER" }
            "AGENTS" -> events.filter { it.eventType == "AGENT_FACILITATION" || it.eventType == "FLOAT_BONUS" }
            "PAYOUTS" -> events.filter { it.eventType == "WITHDRAWAL" }
            else -> events
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("dialog_all_live_activity"),
        title = {
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
                            .background(StatusGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Live Platform Activity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DeepTealDark
                        )
                        Text(
                            text = "Real recent events recorded on HomeUtensils",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Filter Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filterOptions = listOf(
                        "ALL" to "All Events",
                        "PACKAGES" to "Packages",
                        "TASKS" to "Tasks",
                        "P2P" to "P2P",
                        "AGENTS" to "Agents",
                        "PAYOUTS" to "Payouts"
                    )
                    items(filterOptions) { (key, label) ->
                        val isSelected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) DeepTeal else WarmCreamSurface)
                                .border(1.dp, if (isSelected) DeepTeal else WarmCreamBorder, RoundedCornerShape(14.dp))
                                .clickable { selectedFilter = key }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) WarmWhite else DeepTealDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = WarmCreamBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                if (filteredEvents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No events recorded in this category yet.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredEvents, key = { it.id }) { event ->
                            val (bgColor, iconColor) = getEventColors(event.iconType)
                            val icon = getEventIcon(event.iconType)
                            val timeAgo = formatEventTimeAgo(event.timestamp)

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, WarmCreamBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bgColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = iconColor,
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
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(DeepTealContainer)
                                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = event.maskedMember,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = DeepTealDark
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = event.title,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }

                                            Text(
                                                text = timeAgo,
                                                fontSize = 10.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = event.detail,
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = StatusGreenText, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Authoritative backend records only • No simulated activity",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("Close Feed", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
}
