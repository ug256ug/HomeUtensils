package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.LinearProgressIndicator
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.ActivationFundingTargetEntity
import com.example.data.model.P2PTransferEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarningBadge
import com.example.ui.components.formatUgx
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldLight
import com.example.ui.theme.SoftGoldContainer
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    userProfile: UserProfileEntity?,
    transactions: List<WalletTransactionEntity>,
    activationCycles: List<ActivationCycleEntity> = emptyList(),
    p2pTransfers: List<P2PTransferEntity> = emptyList(),
    userWithdrawals: List<WithdrawalRequestEntity> = emptyList(),
    activeFundingTarget: ActivationFundingTargetEntity? = null,
    onWithdrawClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onUpgradeClick: () -> Unit = {},
    onReactivateClick: () -> Unit = {},
    onTransactionClick: (WalletTransactionEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Tasks", "Referrals", "P2P Transfers", "Withdrawals", "Agent Activations", "Pending")
    val standardDateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    val filteredTransactions = transactions.filter { tx ->
        when (selectedFilter) {
            "Tasks" -> tx.type == "TASK_REWARD"
            "Referrals" -> tx.type == "REFERRAL_BONUS"
            "P2P Transfers" -> tx.type == "P2P_TRANSFER"
            "Withdrawals" -> tx.type == "WITHDRAWAL"
            "Agent Activations" -> tx.type == "AGENT_FACILITATION"
            "Pending" -> tx.status == "PENDING"
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("wallet_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Balance Card
        item {
            MainBalanceCard(
                walletBalanceUgx = userProfile?.walletBalanceUgx ?: 0L,
                paidCapitalUgx = userProfile?.paidCapitalUgx ?: 10000L,
                totalEarnedUgx = userProfile?.totalEarnedUgx ?: 0L,
                packageName = userProfile?.packageName ?: "STANDARD PACKAGE"
            )
        }

        // Action Buttons Row: Withdraw Funds, P2P Member Transfer, Upgrade/Reactivate
        item {
            val isPkgExpired = userProfile?.isPackageExpired == true ||
                (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0 && System.currentTimeMillis() >= userProfile.packageExpiresAt)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onWithdrawClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_wallet_withdraw"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPkgExpired) Color(0xFFC62828) else DeepTeal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = if (isPkgExpired) Icons.Default.Lock else Icons.Default.Payments,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPkgExpired) "Withdraw (Locked)" else "Withdraw",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onTransferClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_wallet_transfer"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPkgExpired) Color(0xFF757575) else Color(0xFF1976D2)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = if (isPkgExpired) Icons.Default.Lock else Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPkgExpired) "P2P (Locked)" else "P2P Transfer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                OutlinedButton(
                    onClick = if (isPkgExpired) onReactivateClick else onUpgradeClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_wallet_manage_pkg"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepTeal)
                ) {
                    Icon(
                        imageVector = if (isPkgExpired) Icons.Default.Autorenew else Icons.Default.Upgrade,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = DeepTeal
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPkgExpired) "Reactivate" else "Upgrade",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = DeepTealDark
                    )
                }
            }
        }

        // Payout Status & Expiry Gate Notice Card
        item {
            val isPkgExpired = userProfile?.isPackageExpired == true ||
                (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0 && System.currentTimeMillis() >= userProfile.packageExpiresAt)

            if (isPkgExpired) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarningRedContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Withdrawals & P2P Paused (Package Expired)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = WarningRedText
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "As per Rule 6, withdrawals and member transfers are locked while your package is expired. Tap 'Reactivate' to restore full access. You can continue earning via tasks and referrals.",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarningRedText,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onReactivateClick,
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Reactivate Package Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = "Mobile Money",
                                tint = SoftGoldOnContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Uganda Mobile Money Payouts Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Instant cashouts to MTN MoMo and Airtel Money Uganda. Task earnings and referral commissions credit automatically without admin approval.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Rule 2: P2P Activation Funding Progress Section
        if (activeFundingTarget != null) {
            item {
                val progress = if (activeFundingTarget.targetAmountUgx > 0) {
                    (activeFundingTarget.contributedAmountUgx.toFloat() / activeFundingTarget.targetAmountUgx.toFloat()).coerceIn(0f, 1f)
                } else 0f
                val isCompleted = activeFundingTarget.isCompleted

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("activation_funding_target_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCompleted) StatusGreenContainer else WarmCreamBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isCompleted) StatusGreen else SoftGold)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Send,
                                    contentDescription = null,
                                    tint = if (isCompleted) StatusGreenText else DeepTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "P2P Activation Funding Target",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                            }
                            StatusBadge(text = if (isCompleted) "COMPLETED" else "IN PROGRESS")
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Target: ${activeFundingTarget.targetPackageName} (${formatUgx(activeFundingTarget.targetAmountUgx)})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = DeepTeal,
                            trackColor = WarmCreamBorder
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Funded: ${formatUgx(activeFundingTarget.contributedAmountUgx)} (${(progress * 100).toInt()}%)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                            Text(
                                text = "${activeFundingTarget.contributorsCount} contribution(s)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isCompleted)
                                "Target achieved! Package auto-activated and activation cycle initialized."
                            else
                                "Multiple members can contribute via P2P transfers toward this target. When UGX ${formatUgx(activeFundingTarget.targetAmountUgx)} is reached, activation completes automatically.",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Rule 4, 7, 8: User Withdrawal Requests List
        if (userWithdrawals.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_withdrawals_card"),
                    shape = RoundedCornerShape(16.dp),
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
                                Text(
                                    text = "Withdrawal Requests & Payouts",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                            }
                            Text(
                                text = "${userWithdrawals.size} requests",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = WarmCreamBorder, thickness = 0.6.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        userWithdrawals.forEach { w ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${w.payoutMethodName} • ${w.beneficiaryAccount}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${w.id} • ${standardDateFormat.format(Date(w.createdAt))}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Gross: ${formatUgx(w.requestedAmountUgx)} • Fee (10%): -${formatUgx(w.feeUgx)}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    w.rejectionReason?.let { reason ->
                                        Text(
                                            text = "Rejection reason: $reason (Returned to balance)",
                                            fontSize = 9.sp,
                                            color = WarningRedText,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = formatUgx(w.netAmountUgx),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTeal
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (w.status) {
                                                    "PENDING" -> SoftGoldContainer
                                                    "PROCESSING" -> DeepTealContainer
                                                    "COMPLETED" -> StatusGreenContainer
                                                    "REJECTED" -> WarningRedContainer
                                                    else -> SoftGoldContainer
                                                }
                                            )
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = w.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (w.status) {
                                                "PENDING" -> SoftGoldDark
                                                "PROCESSING" -> DeepTealDark
                                                "COMPLETED" -> StatusGreenText
                                                "REJECTED" -> WarningRedText
                                                else -> SoftGoldOnContainer
                                            }
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        // Rule 4 & 7: Activation Attribution History Section
        if (activationCycles.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("activation_history_card"),
                    shape = RoundedCornerShape(16.dp),
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
                                Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Activation & Reactivation Cycles",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                            }
                            Text(
                                text = "${activationCycles.size} cycles",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = WarmCreamBorder, thickness = 0.6.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        val cycleDateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

                        activationCycles.forEach { cycle ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${cycle.packageTier} • ${cycle.cycleType.replace("_", " ")}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${cycle.id} • User: ${cycle.memberId} • ${cycleDateFormat.format(Date(cycle.activatedAt))}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = formatUgx(cycle.amountUgx),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (cycle.status) {
                                                    "ACTIVE" -> StatusGreenContainer
                                                    "EXPIRED" -> WarningRedContainer
                                                    else -> SoftGoldContainer
                                                }
                                            )
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = cycle.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (cycle.status) {
                                                "ACTIVE" -> StatusGreenText
                                                "EXPIRED" -> WarningRedText
                                                else -> SoftGoldOnContainer
                                            }
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        // Transaction History Header & Filter
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaction History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "${filteredTransactions.size} entries",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = WarmWhite,
                                labelColor = TextSecondary,
                                selectedContainerColor = DeepTeal,
                                selectedLabelColor = WarmWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = WarmCreamBorder,
                                selectedBorderColor = DeepTeal
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("filter_tx_$filter")
                        )
                    }
                }
            }
        }

        // Transaction Items
        when (selectedFilter) {
            "P2P Transfers" -> {
                if (p2pTransfers.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                                Text("No P2P transfers recorded yet.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                } else {
                    items(p2pTransfers, key = { it.id }) { transfer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(SoftGoldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, tint = SoftGoldOnContainer, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "To: ${transfer.recipientMemberId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${transfer.id} • ${standardDateFormat.format(Date(transfer.timestamp))}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Gross: ${formatUgx(transfer.amountUgx)} • Fee (10%): -${formatUgx(transfer.feeUgx)}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    if (transfer.isActivationFunding) {
                                        Text(
                                            text = "Activation Contribution (${transfer.targetPackage})",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTeal
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Net: ${formatUgx(transfer.netRecipientAmountUgx)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTealDark
                                    )
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(StatusGreenContainer).padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(transfer.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StatusGreenText)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "Withdrawals" -> {
                if (userWithdrawals.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                                Text("No withdrawal cashouts recorded yet.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                } else {
                    items(userWithdrawals, key = { it.id }) { w ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(
                                        when (w.status) {
                                            "PENDING" -> SoftGoldContainer
                                            "PROCESSING" -> DeepTealContainer
                                            "COMPLETED" -> StatusGreenContainer
                                            "REJECTED" -> WarningRedContainer
                                            else -> SoftGoldContainer
                                        }
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Payments,
                                        contentDescription = null,
                                        tint = when (w.status) {
                                            "PENDING" -> SoftGoldDark
                                            "PROCESSING" -> DeepTealDark
                                            "COMPLETED" -> StatusGreenText
                                            "REJECTED" -> WarningRedText
                                            else -> SoftGoldOnContainer
                                        },
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${w.payoutMethodName} • ${w.beneficiaryAccount}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${w.id} • ${standardDateFormat.format(Date(w.createdAt))}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Gross: ${formatUgx(w.requestedAmountUgx)} • Fee (10%): -${formatUgx(w.feeUgx)}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    w.rejectionReason?.let { r ->
                                        Text(
                                            text = "Reason: $r (Refunded to wallet)",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WarningRedText
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = formatUgx(w.netAmountUgx),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTeal
                                    )
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(
                                            when (w.status) {
                                                "PENDING" -> SoftGoldContainer
                                                "PROCESSING" -> DeepTealContainer
                                                "COMPLETED" -> StatusGreenContainer
                                                "REJECTED" -> WarningRedContainer
                                                else -> SoftGoldContainer
                                            }
                                        ).padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = w.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (w.status) {
                                                "PENDING" -> SoftGoldDark
                                                "PROCESSING" -> DeepTealDark
                                                "COMPLETED" -> StatusGreenText
                                                "REJECTED" -> WarningRedText
                                                else -> SoftGoldOnContainer
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                if (filteredTransactions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No transactions found in this category.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(filteredTransactions, key = { it.id }) { tx ->
                        TransactionItemRow(
                            tx = tx,
                            onClick = { onTransactionClick(tx) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MainBalanceCard(
    walletBalanceUgx: Long,
    paidCapitalUgx: Long,
    totalEarnedUgx: Long,
    packageName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("main_balance_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DeepTealDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SoftGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = DeepTealDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Withdrawable Balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmCreamBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                StatusBadge(text = packageName)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = formatUgx(walletBalanceUgx),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = WarmWhite
            )

            Text(
                text = "Automatically credited task rewards & referral bonuses ready for cash out.",
                style = MaterialTheme.typography.bodySmall,
                color = WarmCreamBackground.copy(alpha = 0.75f),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Financial Breakdown: Clearly distinguish Paid Capital and Total Income Earned
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF00241E))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Paid Capital (Non-withdrawable)",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGoldLight,
                        fontSize = 11.sp
                    )
                    Text(
                        text = formatUgx(paidCapitalUgx),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoftGoldContainer
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Income Earned",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmCreamBackground.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                    Text(
                        text = formatUgx(totalEarnedUgx),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmWhite
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItemRow(
    tx: WalletTransactionEntity,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(tx.timestamp) { dateFormat.format(Date(tx.timestamp)) }

    val direction = when (tx.type) {
        "TASK_REWARD", "REFERRAL_BONUS", "REFERRAL_COMMISSION", "MONTHLY_REFERRAL_BENEFIT",
        "AGENT_COMMISSION", "WELCOME_BONUS", "P2P_RECEIVED", "REFUND" -> "INCOMING"
        "WITHDRAWAL", "P2P_TRANSFER", "P2P_SENT", "PACKAGE_UPGRADE", "PACKAGE_ACTIVATION", "REACTIVATION" -> "OUTGOING"
        "PAID_CAPITAL" -> "NEUTRAL"
        else -> if (tx.title.contains("Received", true) || tx.title.contains("Reward", true)) "INCOMING"
                else if (tx.title.contains("Sent", true) || tx.title.contains("Withdrawal", true)) "OUTGOING"
                else "NEUTRAL"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("tx_row_${tx.id}"),
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
            // Accessible Direction Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        when (direction) {
                            "INCOMING" -> StatusGreenContainer
                            "OUTGOING" -> WarningRedContainer
                            else -> SoftGoldContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        tx.status == "PENDING" -> Icons.Default.HourglassTop
                        direction == "INCOMING" -> Icons.Default.ArrowUpward
                        direction == "OUTGOING" -> Icons.Default.ArrowDownward
                        else -> Icons.Default.Info
                    },
                    contentDescription = "$direction Transaction",
                    tint = when (direction) {
                        "INCOMING" -> StatusGreenText
                        "OUTGOING" -> WarningRedText
                        else -> SoftGoldOnContainer
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${tx.id} • $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                if (tx.type == "P2P_TRANSFER" && tx.feeUgx > 0) {
                    Text(
                        text = "Transfer: ${formatUgx(tx.amountUgx)} • Fee (10%): ${formatUgx(tx.feeUgx)}",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                } else if (tx.type == "WITHDRAWAL" && tx.feeUgx > 0) {
                    val net = if (tx.netAmountUgx > 0) tx.netAmountUgx else (tx.amountUgx - tx.feeUgx)
                    Text(
                        text = "Req: ${formatUgx(tx.amountUgx)} • Fee: -${formatUgx(tx.feeUgx)} • Net: ${formatUgx(net)}",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Amount with direction indicator and status badge
            Column(horizontalAlignment = Alignment.End) {
                val totalDisplayAmount = if (direction == "OUTGOING" && tx.type == "P2P_TRANSFER" && tx.feeUgx > 0) {
                    tx.amountUgx + tx.feeUgx
                } else tx.amountUgx

                Text(
                    text = when (direction) {
                        "INCOMING" -> "🟢 ↑ +${formatUgx(tx.amountUgx)}"
                        "OUTGOING" -> "🔴 ↓ −${formatUgx(totalDisplayAmount)}"
                        else -> "⚪ ${formatUgx(tx.amountUgx)}"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = when (direction) {
                        "INCOMING" -> StatusGreenText
                        "OUTGOING" -> WarningRedText
                        else -> DeepTealDark
                    }
                )

                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when {
                                tx.status == "PENDING" -> SoftGoldContainer
                                tx.status == "CANCELLED" || tx.status == "REJECTED" -> WarningRedContainer
                                direction == "INCOMING" -> StatusGreenContainer
                                direction == "OUTGOING" -> WarningRedContainer.copy(alpha = 0.5f)
                                else -> SoftGoldContainer
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when {
                            tx.status == "PENDING" -> "Pending"
                            tx.status == "CANCELLED" -> "Cancelled"
                            tx.status == "REJECTED" -> "Rejected"
                            direction == "INCOMING" -> "Credited"
                            direction == "OUTGOING" -> "Debited"
                            else -> "Non-withdrawable"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            tx.status == "PENDING" -> SoftGoldOnContainer
                            tx.status == "CANCELLED" || tx.status == "REJECTED" -> WarningRedText
                            direction == "INCOMING" -> StatusGreenText
                            direction == "OUTGOING" -> WarningRedText
                            else -> SoftGoldOnContainer
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
