package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.BeneficiaryDetailsEntity
import com.example.data.model.HomeUtensilsPackage
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.data.model.maskBeneficiaryName
import com.example.data.model.maskDestination
import com.example.data.repository.RecipientLookupResult
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
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

/**
 * Rule 5: Upgrades
 * - No downgrade allowed.
 * - Allowed upgrades: Starter -> Standard (5,000 UGX), Standard -> Plus (10,000 UGX), Starter -> Plus (15,000 UGX)
 * - Difference only: Do not charge the full new package amount again.
 * - No welcome bonus for upgrade.
 * - Direct inviter commission: 10% of upgrade top-up only.
 */
@Composable
fun UpgradePackageDialog(
    currentPackageName: String,
    onDismiss: () -> Unit,
    onUpgrade: (targetPackage: String) -> Unit
) {
    val currentPkg = HomeUtensilsPackage.fromName(currentPackageName)
    val eligiblePackages = when (currentPkg) {
        HomeUtensilsPackage.STARTER -> listOf(HomeUtensilsPackage.STANDARD, HomeUtensilsPackage.PLUS)
        HomeUtensilsPackage.STANDARD -> listOf(HomeUtensilsPackage.PLUS)
        HomeUtensilsPackage.PLUS -> emptyList()
    }

    var selectedPkg by remember { mutableStateOf(eligiblePackages.firstOrNull()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("upgrade_package_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upgrade,
                                contentDescription = "Upgrade",
                                tint = DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Package Upgrade",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Current: ${currentPkg.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(14.dp))

                if (eligiblePackages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepTealContainer)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Highest Package Tier Active",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "You are already enjoying the PLUS PACKAGE (Top tier). No further upgrades available.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Select Upgrade Option (Top-up difference only):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    eligiblePackages.forEach { pkg ->
                        val diff = when (currentPkg to pkg) {
                            HomeUtensilsPackage.STARTER to HomeUtensilsPackage.STANDARD -> 5000L
                            HomeUtensilsPackage.STANDARD to HomeUtensilsPackage.PLUS -> 10000L
                            HomeUtensilsPackage.STARTER to HomeUtensilsPackage.PLUS -> 15000L
                            else -> pkg.activationAmountUgx - currentPkg.activationAmountUgx
                        }
                        val inviterComm = (diff * 0.10f).toLong()
                        val isSelected = selectedPkg == pkg

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) DeepTeal else WarmCreamBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedPkg = pkg }
                                .testTag("upgrade_option_${pkg.name}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DeepTealContainer.copy(alpha = 0.35f) else WarmCreamSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPkg = pkg },
                                    colors = RadioButtonDefaults.colors(selectedColor = DeepTeal)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = pkg.displayName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTealDark
                                    )
                                    Text(
                                        text = "Duration: ${pkg.durationDays} days • Task reward: ${formatUgx(pkg.taskRewardUgx)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "Inviter 10% commission: ${formatUgx(inviterComm)} • Welcome bonus: UGX 0",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SoftGoldOnContainer,
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = formatUgx(diff),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTeal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notice box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(WarmCreamSurface)
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "No full price re-charge. Only the difference between tiers is charged. Upgrades create a new activation cycle and preserve all transaction history.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    selectedPkg?.let { target ->
                        val diff = when (currentPkg to target) {
                            HomeUtensilsPackage.STARTER to HomeUtensilsPackage.STANDARD -> 5000L
                            HomeUtensilsPackage.STANDARD to HomeUtensilsPackage.PLUS -> 10000L
                            HomeUtensilsPackage.STARTER to HomeUtensilsPackage.PLUS -> 15000L
                            else -> target.activationAmountUgx - currentPkg.activationAmountUgx
                        }
                        Button(
                            onClick = { onUpgrade(target.displayName) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_confirm_upgrade"),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Confirm Upgrade (${formatUgx(diff)})",
                                fontWeight = FontWeight.Bold,
                                color = WarmWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Rule 4: Reactivation
 * - When existing member's package expires and reactivates:
 *   - Starter: 5,000 UGX, 60 days, tasks: 4, reward: UGX 500, welcome bonus: UGX 0
 *   - Standard: 10,000 UGX, 60 days, tasks: 6, reward: UGX 500, welcome bonus: UGX 0
 *   - Plus: 20,000 UGX, 90 days, tasks: 7, reward: UGX 1,000, welcome bonus: UGX 0
 * - Creates a NEW activation cycle.
 * - Never overwrites previous activation cycle.
 * - Keeps full history of all previous cycles, tasks, earnings and transactions.
 */
@Composable
fun ReactivatePackageDialog(
    currentPackageName: String,
    onDismiss: () -> Unit,
    onReactivate: (packageName: String) -> Unit
) {
    val packages = listOf(
        HomeUtensilsPackage.STARTER,
        HomeUtensilsPackage.STANDARD,
        HomeUtensilsPackage.PLUS
    )
    val defaultSelection = HomeUtensilsPackage.fromName(currentPackageName)
    var selectedPkg by remember { mutableStateOf(defaultSelection) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("reactivate_package_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = "Reactivate",
                                tint = SoftGoldOnContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Package Reactivation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "New Activation Cycle • Preserves All History",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Package for Reactivation:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                packages.forEach { pkg ->
                    val isSelected = selectedPkg == pkg

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) DeepTeal else WarmCreamBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedPkg = pkg }
                            .testTag("reactivate_option_${pkg.name}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DeepTealContainer.copy(alpha = 0.35f) else WarmCreamSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedPkg = pkg },
                                colors = RadioButtonDefaults.colors(selectedColor = DeepTeal)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = pkg.displayName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                Text(
                                    text = "Duration: ${pkg.reactivationDurationDays} days • Tasks: ${pkg.reactivationInitialTasks}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Task Reward: ${formatUgx(pkg.reactivationTaskRewardUgx)} • Welcome bonus: UGX 0",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                            Text(
                                text = formatUgx(pkg.reactivationAmountUgx),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTeal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Preservation note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmCreamSurface)
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Each reactivation creates a NEW activation cycle. Previous cycle history, tasks, earnings, and ledger records are never overwritten.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onReactivate(selectedPkg.displayName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_confirm_reactivate"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Reactivate ${selectedPkg.displayName} (${formatUgx(selectedPkg.reactivationAmountUgx)})",
                        fontWeight = FontWeight.Bold,
                        color = WarmWhite
                    )
                }
            }
        }
    }
}

/**
 * Rule 4, 5, 6, 7, 8, 9: Withdrawal execution with schedule gatekeeper & fee calculation
 * - Min: UGX 2,000, no max
 * - Fee: 10% deducted from requested amount
 * - Schedule: Mon-Fri 6AM-8PM, closed on weekends
 * - Only one pending withdrawal per member
 * - Transaction PIN required
 * - Multi-provider payout methods configuration
 */
@Composable
fun WithdrawDialog(
    isExpired: Boolean,
    availableBalanceUgx: Long,
    activeMethods: List<WithdrawalMethodEntity>,
    beneficiaryDetails: BeneficiaryDetailsEntity?,
    pendingWithdrawal: WithdrawalRequestEntity?,
    isScheduleOpen: Boolean,
    availabilityMessage: String = "Withdrawals are currently closed. Withdrawal requests are accepted Monday–Saturday, 6:00 AM–8:00 PM, except on Uganda public holidays. We are currently processing pending withdrawal requests. Please try again when withdrawals reopen.",
    simulatedScheduleOverride: Boolean,
    onToggleSimulateSchedule: () -> Unit,
    onRequestBeneficiaryHelp: () -> Unit,
    onDismiss: () -> Unit,
    onWithdraw: (amountUgx: Long, methodId: String, account: String, name: String, pin: String) -> Unit,
    onReactivateClick: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var selectedMethodId by remember(activeMethods) {
        mutableStateOf(activeMethods.firstOrNull { it.isDefault }?.id ?: activeMethods.firstOrNull()?.id ?: "METHOD-MOMO")
    }
    val selectedMethod = activeMethods.firstOrNull { it.id == selectedMethodId }

    var accountText by remember(beneficiaryDetails) {
        mutableStateOf(beneficiaryDetails?.accountIdentifier ?: "+256 772 458 921")
    }
    var beneficiaryNameText by remember(beneficiaryDetails) {
        mutableStateOf(beneficiaryDetails?.beneficiaryName ?: "Nalubega Sarah")
    }
    var pinText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val amount = amountText.toLongOrNull() ?: 0L
    val feeUgx = (amount * 0.10f).toLong()
    val netPayoutUgx = if (amount >= feeUgx) amount - feeUgx else 0L

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("withdraw_funds_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isExpired) WarningRedContainer else DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isExpired) Icons.Default.Warning else Icons.Default.Payments,
                                contentDescription = "Withdraw",
                                tint = if (isExpired) WarningRedText else DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Withdraw Funds",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "10% processing fee deducted",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(12.dp))

                if (isExpired) {
                    // Rule 6: Prevent withdrawal when package is expired
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WarningRedContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRed, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Withdrawals Locked: Package Expired",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningRedText
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "As per HomeUtensils policy, withdrawals and P2P transfers are paused while your package is expired. Please reactivate your package to unlock cashouts.",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarningRedText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Note: You can still complete eligible tasks and invite referrals to earn toward reactivation!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onReactivateClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_reactivate_from_withdraw"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reactivate Package to Unlock Withdrawals", fontWeight = FontWeight.Bold)
                    }
                } else if (!isScheduleOpen) {
                    // Rule 4: Schedule closed
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftGoldContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = SoftGoldDark, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Withdrawal Window Closed",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGoldDark
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = availabilityMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Testing toggle to simulate open window
                    OutlinedButton(
                        onClick = onToggleSimulateSchedule,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simulate Open Window (Mon-Sat 6AM-8PM)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (pendingWithdrawal != null) {
                    // Rule 4: Only one pending withdrawal per member at a time
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftGoldContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = SoftGoldDark, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pending Withdrawal in Progress",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGoldDark
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Request ID: ${pendingWithdrawal.id}\nAmount: ${formatUgx(pendingWithdrawal.requestedAmountUgx)} (Net: ${formatUgx(pendingWithdrawal.netAmountUgx)})\nMethod: ${pendingWithdrawal.payoutMethodName}\nAccount: ${pendingWithdrawal.beneficiaryAccount}\nStatus: ${pendingWithdrawal.status}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "HomeUtensils allows only one pending withdrawal per member at a time. Please wait until this request is completed or rejected by admin.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                } else {
                    // Available Balance Notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepTealContainer)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Withdrawable Balance",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = formatUgx(availableBalanceUgx),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTealDark
                                )
                            }
                            StatusBadge(text = "Available")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Payout Method Selector
                    Text(
                        text = "Select Payout Method:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        activeMethods.forEach { method ->
                            val isSel = selectedMethodId == method.id
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedMethodId = method.id }
                                    .testTag("method_${method.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) DeepTeal.copy(alpha = 0.08f) else WarmWhite
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) DeepTeal else WarmCreamBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = method.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isSel) DeepTealDark else TextPrimary
                                        )
                                        Text(
                                            text = "${method.supportedCountries} • ${method.currency}",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    RadioButton(
                                        selected = isSel,
                                        onClick = { selectedMethodId = method.id },
                                        colors = RadioButtonDefaults.colors(selectedColor = DeepTeal)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary Details with Verification Lock
                    val maskedDestination = maskDestination(accountText)
                    val maskedBeneficiary = maskBeneficiaryName(beneficiaryNameText)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("beneficiary_details_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmCreamBackground),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Saved Payout Destination",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTealDark
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = StatusGreenText, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Verified / Locked", fontSize = 10.sp, color = StatusGreenText, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Withdrawal Method:",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = selectedMethod?.name ?: "MTN Mobile Money",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Destination:",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = maskedDestination,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Beneficiary:",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = maskedBeneficiary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Status: Verified / Locked",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusGreenText
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Casual edits are prohibited to safeguard member funds. Changing locked payout details requires secure verification / Admin assistance and is recorded in the audit trail.",
                                fontSize = 10.sp,
                                color = TextSecondary,
                                lineHeight = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedButton(
                                onClick = onRequestBeneficiaryHelp,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_request_account_change")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = DeepTeal)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Request Destination Change / Verification", fontSize = 11.sp, color = DeepTeal, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Amount input (Min 2,000 UGX, No max limit)
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = {
                            amountText = it.filter { char -> char.isDigit() }
                            errorMsg = null
                        },
                        label = { Text("Withdrawal Amount (UGX) *") },
                        placeholder = { Text("Minimum UGX 2,000 (No Max Limit)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_amount_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dynamic Fee & Confirmation Summary Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdrawal_confirmation_summary_card"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftGoldContainer.copy(alpha = 0.4f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Withdrawal Confirmation Summary", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Requested amount:", fontSize = 11.sp, color = TextSecondary)
                                Text("UGX ${formatUgx(amount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Withdrawal fee (10%):", fontSize = 11.sp, color = WarningRedText)
                                Text("UGX ${formatUgx(feeUgx)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningRedText)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WarmCreamBorder, thickness = 0.5.dp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("You receive (Net payout):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                                Text("UGX ${formatUgx(netPayoutUgx)}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Withdrawal method:", fontSize = 11.sp, color = TextSecondary)
                                Text(selectedMethod?.name ?: "Mobile Money", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Destination:", fontSize = 11.sp, color = TextSecondary)
                                Text(maskedDestination, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Beneficiary:", fontSize = 11.sp, color = TextSecondary)
                                Text(maskedBeneficiary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Transaction PIN Input
                    OutlinedTextField(
                        value = pinText,
                        onValueChange = {
                            if (it.length <= 4) {
                                pinText = it.filter { char -> char.isDigit() }
                                errorMsg = null
                            }
                        },
                        label = { Text("Security Transaction PIN (4 digits) *") },
                        placeholder = { Text("Enter 4-digit PIN (default 1234)") },
                        leadingIcon = {
                            Icon(Icons.Default.Security, contentDescription = null, tint = DeepTeal)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_pin_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = RoundedCornerShape(12.dp)
                    )

                    errorMsg?.let { err ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = err, color = WarningRed, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (amount < 2000L) {
                                errorMsg = "Minimum withdrawal amount is UGX 2,000."
                                return@Button
                            }
                            if (amount > availableBalanceUgx) {
                                errorMsg = "Amount exceeds Withdrawable Balance (${formatUgx(availableBalanceUgx)})."
                                return@Button
                            }
                            if (pinText.length < 4) {
                                errorMsg = "Please enter your 4-digit transaction PIN (default: 1234)."
                                return@Button
                            }
                            onWithdraw(
                                amount,
                                selectedMethodId,
                                accountText.trim(),
                                beneficiaryNameText.trim(),
                                pinText
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_submit_withdraw"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm Withdrawal Request", fontWeight = FontWeight.Bold, color = WarmWhite)
                    }
                }
            }
        }
    }
}

/**
 * Rule 1, 2, 9: P2P Member Transfer execution (24/7 available)
 * - Minimum: UGX 1,000
 * - Fee: 10% deducted from sender's transfer amount (NOT added on top)
 * - Recipient lookup & masked ID preview
 * - Transaction PIN required
 * - P2P Activation Funding target support
 */
@Composable
fun P2PTransferDialog(
    isExpired: Boolean,
    availableBalanceUgx: Long,
    onLookupRecipient: (String) -> RecipientLookupResult,
    onDismiss: () -> Unit,
    onTransfer: (recipientMemberId: String, amountUgx: Long, pin: String, isActivationFunding: Boolean, targetPackage: String) -> Unit,
    onReactivateClick: () -> Unit
) {
    var recipientId by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var pinText by remember { mutableStateOf("") }
    var isActivationFunding by remember { mutableStateOf(false) }
    var targetPackage by remember { mutableStateOf("STARTER PACKAGE") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val enteredAmount = amountText.toLongOrNull() ?: 0L
    val totalDeductedUgx = enteredAmount
    val recipientAmountUgx = kotlin.math.round(enteredAmount / 1.10).toLong()
    val feeUgx = if (enteredAmount > 0) totalDeductedUgx - recipientAmountUgx else 0L

    val recipientLookup = remember(recipientId) {
        if (recipientId.trim().isNotEmpty()) onLookupRecipient(recipientId) else null
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("p2p_transfer_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isExpired) WarningRedContainer else SoftGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isExpired) Icons.Default.Warning else Icons.Default.Send,
                                contentDescription = "P2P Transfer",
                                tint = if (isExpired) WarningRedText else SoftGoldOnContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Member P2P Transfer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Available 24/7 • 10% fee deducted",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(12.dp))

                if (isExpired) {
                    // Rule 6: Expired accounts cannot perform member transfers
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WarningRedContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRed, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "P2P Transfers Disabled: Expired Account",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningRedText
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Normal member-to-member P2P transfers are disabled for expired accounts. Please reactivate your package to resume sending funds.",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarningRedText,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onReactivateClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_reactivate_from_p2p"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reactivate Package to Enable P2P", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "Available: ${formatUgx(availableBalanceUgx)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepTeal,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Recipient Member ID
                    OutlinedTextField(
                        value = recipientId,
                        onValueChange = {
                            recipientId = it.uppercase()
                            errorMsg = null
                        },
                        label = { Text("Recipient Member ID *") },
                        placeholder = { Text("e.g. HU2P9X14R or HU8M1Q33P") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("p2p_recipient_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Real-time recipient lookup verification badge
                    if (recipientLookup != null && recipientId.length >= 4) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = StatusGreenContainer)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreenText, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Recipient: ${recipientLookup.name} (${recipientLookup.maskedId})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusGreenText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Transfer Amount
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = {
                            amountText = it.filter { char -> char.isDigit() }
                            errorMsg = null
                        },
                        label = { Text("Transfer Amount (UGX) *") },
                        placeholder = { Text("Minimum UGX 1,000") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("p2p_amount_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rule 2: P2P Activation Funding Checkbox
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isActivationFunding = !isActivationFunding },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isActivationFunding) DeepTealContainer else WarmCreamBackground
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isActivationFunding) DeepTeal else WarmCreamBorder
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = isActivationFunding,
                                    onClick = { isActivationFunding = !isActivationFunding },
                                    colors = RadioButtonDefaults.colors(selectedColor = DeepTeal)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = "Contribute to Activation Funding Target",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTealDark
                                    )
                                    Text(
                                        text = "Funds go toward activating the recipient's package. Activates automatically once target is reached!",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (isActivationFunding) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Target Package to Fund:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("STARTER PACKAGE", "STANDARD PACKAGE", "PLUS PACKAGE").forEach { pkg ->
                                        val isSel = targetPackage == pkg
                                        OutlinedButton(
                                            onClick = { targetPackage = pkg },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSel) DeepTeal else WarmWhite,
                                                contentColor = if (isSel) WarmWhite else DeepTealDark
                                            )
                                        ) {
                                            Text(pkg.replace(" PACKAGE", ""), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Calculation Breakdown Card: 10% fee is INCLUDED inside the sender's entered amount
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftGoldContainer.copy(alpha = 0.4f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TOTAL YOU ARE SENDING:", fontSize = 11.sp, color = DeepTealDark, fontWeight = FontWeight.Bold)
                                Text(formatUgx(enteredAmount), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("RECIPIENT RECEIVES:", fontSize = 11.sp, color = StatusGreenText, fontWeight = FontWeight.Bold)
                                Text(formatUgx(recipientAmountUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StatusGreenText)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("P2P FEE (10%):", fontSize = 11.sp, color = WarningRedText, fontWeight = FontWeight.SemiBold)
                                Text(formatUgx(feeUgx), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningRedText)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = WarmCreamBorder, thickness = 0.8.dp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TOTAL DEDUCTED FROM YOUR BALANCE:", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                                Text(formatUgx(totalDeductedUgx), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Transaction PIN Input
                    OutlinedTextField(
                        value = pinText,
                        onValueChange = {
                            if (it.length <= 4) {
                                pinText = it.filter { char -> char.isDigit() }
                                errorMsg = null
                            }
                        },
                        label = { Text("Security Transaction PIN (4 digits) *") },
                        placeholder = { Text("Enter 4-digit PIN (default 1234)") },
                        leadingIcon = {
                            Icon(Icons.Default.Security, contentDescription = null, tint = DeepTeal)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("p2p_pin_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = RoundedCornerShape(12.dp)
                    )

                    errorMsg?.let { err ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = err, color = WarningRed, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (recipientId.isBlank()) {
                                errorMsg = "Please enter recipient Member ID."
                                return@Button
                            }
                            if (enteredAmount < 1000L) {
                                errorMsg = "Minimum P2P transfer is UGX 1,000."
                                return@Button
                            }
                            if (enteredAmount > availableBalanceUgx) {
                                errorMsg = "Total deduction (UGX ${formatUgx(enteredAmount)}) exceeds Withdrawable Balance (${formatUgx(availableBalanceUgx)})."
                                return@Button
                            }
                            if (pinText.length < 4) {
                                errorMsg = "Please enter your 4-digit transaction PIN (default: 1234)."
                                return@Button
                            }
                            onTransfer(recipientId.trim(), enteredAmount, pinText, isActivationFunding, targetPackage)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_submit_p2p"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm & Send P2P Transfer", fontWeight = FontWeight.Bold, color = WarmWhite)
                    }
                }
            }
        }
    }
}

/**
 * Rule 6: Beneficiary Account Change Assistance & Payout Destination Setup Dialog (Audit Logged)
 * Supports all admin configured payout methods (MTN MoMo, Airtel Money, Chipper Cash, Binance, Bank Transfer)
 */
@Composable
fun BeneficiaryHelpDialog(
    currentBeneficiary: BeneficiaryDetailsEntity?,
    activeMethods: List<WithdrawalMethodEntity> = emptyList(),
    onDismiss: () -> Unit,
    onSaveUpdate: (account: String, name: String, reason: String) -> Unit,
    onSaveFullPayoutDetails: ((methodId: String, methodName: String, account: String, name: String, bankName: String, binanceType: String, network: String, token: String, reason: String, pin: String) -> Unit)? = null
) {
    var selectedMethodId by remember {
        mutableStateOf(currentBeneficiary?.methodId ?: activeMethods.firstOrNull()?.id ?: "METHOD-MOMO")
    }
    val selectedMethod = activeMethods.firstOrNull { it.id == selectedMethodId }

    var accountInput by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.accountIdentifier ?: "")
    }
    var nameInput by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.beneficiaryName ?: "")
    }
    var bankNameInput by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.bankName ?: "Stanbic Bank Uganda")
    }
    var binanceAddressType by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.binanceAddressType ?: "ID")
    }
    var networkInput by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.network ?: "TRC20")
    }
    var assetTokenInput by remember(currentBeneficiary) {
        mutableStateOf(currentBeneficiary?.assetToken ?: "USDT")
    }
    var reasonText by remember { mutableStateOf("") }
    var pinText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val methodName = selectedMethod?.name ?: currentBeneficiary?.methodName ?: "Mobile Money"
    val isBinance = methodName.contains("Binance", ignoreCase = true)
    val isBank = methodName.contains("Bank", ignoreCase = true)
    val isChipper = methodName.contains("Chipper", ignoreCase = true)
    val isAirtel = methodName.contains("Airtel", ignoreCase = true)
    val isMtn = methodName.contains("MTN", ignoreCase = true)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("payout_destination_setup_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Secure Payout Destination", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepTealDark)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "As per HomeUtensils policy, verified payout destinations are locked to prevent fraud. Changes require transaction PIN authentication and are permanently recorded in the audit trail.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Select Method if activeMethods available
                if (activeMethods.isNotEmpty()) {
                    Text("Select Payout Method:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        activeMethods.forEach { method ->
                            val isSel = method.id == selectedMethodId
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedMethodId = method.id },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) DeepTealContainer else WarmCreamBackground
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) DeepTeal else WarmCreamBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(method.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (isSel) DeepTealDark else TextPrimary)
                                        Text("${method.currency} • ${method.requiredAccountInfo}", fontSize = 9.sp, color = TextSecondary)
                                    }
                                    RadioButton(
                                        selected = isSel,
                                        onClick = { selectedMethodId = method.id },
                                        colors = RadioButtonDefaults.colors(selectedColor = DeepTeal)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Dynamic inputs based on method
                if (isMtn) {
                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it; errorMsg = null },
                        label = { Text("MTN Registered Mobile Number *") },
                        placeholder = { Text("077... / 078...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("input_mtn_phone")
                    )
                } else if (isAirtel) {
                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it; errorMsg = null },
                        label = { Text("Airtel Registered Mobile Number *") },
                        placeholder = { Text("070... / 075...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("input_airtel_phone")
                    )
                } else if (isChipper) {
                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it; errorMsg = null },
                        label = { Text("Chipper Cash Username/Handle *") },
                        placeholder = { Text("@username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_chipper_handle")
                    )
                } else if (isBinance) {
                    Text("Binance Identifier Type:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { binanceAddressType = "ID" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (binanceAddressType == "ID") DeepTeal else WarmWhite,
                                contentColor = if (binanceAddressType == "ID") WarmWhite else DeepTealDark
                            )
                        ) {
                            Text("Binance ID", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { binanceAddressType = "WALLET" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (binanceAddressType == "WALLET") DeepTeal else WarmWhite,
                                contentColor = if (binanceAddressType == "WALLET") WarmWhite else DeepTealDark
                            )
                        ) {
                            Text("Wallet Address", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (binanceAddressType == "ID") {
                        OutlinedTextField(
                            value = accountInput,
                            onValueChange = { accountInput = it; errorMsg = null },
                            label = { Text("Binance Pay ID (UID) *") },
                            placeholder = { Text("e.g. 192837465") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_binance_id")
                        )
                    } else {
                        OutlinedTextField(
                            value = accountInput,
                            onValueChange = { accountInput = it; errorMsg = null },
                            label = { Text("Crypto Wallet Address *") },
                            placeholder = { Text("e.g. Txyz...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_wallet_address")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = networkInput,
                                onValueChange = { networkInput = it },
                                label = { Text("Network *") },
                                placeholder = { Text("TRC20 / BEP20") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = assetTokenInput,
                                onValueChange = { assetTokenInput = it },
                                label = { Text("Asset/Token *") },
                                placeholder = { Text("USDT") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else if (isBank) {
                    OutlinedTextField(
                        value = bankNameInput,
                        onValueChange = { bankNameInput = it; errorMsg = null },
                        label = { Text("Bank Name *") },
                        placeholder = { Text("e.g. Stanbic Bank, Centenary, Absa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_bank_name")
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it; errorMsg = null },
                        label = { Text("Bank Account Number *") },
                        placeholder = { Text("e.g. 9030012345678") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("input_bank_account")
                    )
                } else {
                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it; errorMsg = null },
                        label = { Text("Account Identifier / Details *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_generic_account")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it; errorMsg = null },
                    label = { Text("Beneficiary / Account Full Name *") },
                    placeholder = { Text("Must match legal KYC name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_beneficiary_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reasonText,
                    onValueChange = { reasonText = it; errorMsg = null },
                    label = { Text("Reason for Setup/Change (Audit Requirement) *") },
                    placeholder = { Text("e.g. Initial verified setup, Updated account") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_audit_reason")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pinText,
                    onValueChange = {
                        if (it.length <= 4) {
                            pinText = it.filter { char -> char.isDigit() }
                            errorMsg = null
                        }
                    },
                    label = { Text("Transaction PIN for Verification (4 digits) *") },
                    placeholder = { Text("Enter 4-digit PIN (default 1234)") },
                    leadingIcon = {
                        Icon(Icons.Default.Security, contentDescription = null, tint = DeepTeal)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.fillMaxWidth().testTag("input_audit_pin")
                )

                errorMsg?.let { err ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(err, color = WarningRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (accountInput.isBlank() || nameInput.isBlank()) {
                            errorMsg = "Please enter both the account details and beneficiary name."
                            return@Button
                        }
                        if (reasonText.isBlank()) {
                            errorMsg = "Please provide an audit reason for this setup/change."
                            return@Button
                        }
                        if (pinText.length < 4) {
                            errorMsg = "Please enter your 4-digit transaction PIN for security verification."
                            return@Button
                        }

                        if (onSaveFullPayoutDetails != null) {
                            onSaveFullPayoutDetails(
                                selectedMethodId,
                                methodName,
                                accountInput.trim(),
                                nameInput.trim(),
                                bankNameInput.trim(),
                                binanceAddressType,
                                networkInput.trim(),
                                assetTokenInput.trim(),
                                reasonText.trim(),
                                pinText
                            )
                        } else {
                            onSaveUpdate(accountInput.trim(), nameInput.trim(), reasonText.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_save_lock_payout"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save & Lock Verified Payout Details", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Section 5 & 6: Agent Application Flow Dialog for eligible PLUS members
 */
@Composable
fun AgentApplicationDialog(
    userProfile: com.example.data.model.UserProfileEntity?,
    onDismiss: () -> Unit,
    onSubmit: (fullName: String, phone: String, region: String, idType: String, idNumber: String, idDocUri: String, profilePhotoUri: String, reason: String, agreed: Boolean) -> Unit
) {
    var fullName by remember { mutableStateOf(userProfile?.fullName ?: "Nalubega Sarah") }
    var phoneNumber by remember { mutableStateOf(userProfile?.phoneNumber ?: "+256 772 458 921") }
    var selectedRegion by remember { mutableStateOf(userProfile?.region ?: "Central") }
    val regions = listOf("Central", "Eastern", "Northern", "Western")

    var selectedIdType by remember { mutableStateOf("National ID/NIN") }
    val idTypes = listOf("National ID/NIN", "Passport", "Driving Licence")

    var idNumber by remember { mutableStateOf("CM9084729183HG") }
    var idDocUri by remember { mutableStateOf("Uganda_National_ID_Front_Back.pdf") }
    var profilePhotoUri by remember { mutableStateOf("sarah_passport_photo.jpg") }
    var reason by remember { mutableStateOf("Active Plus member with extensive community reach. Committed to facilitating activations and supporting regional utensil reviewers.") }
    var agreed by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("agent_application_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = SoftGoldDark, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Field Agent Application", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepTealDark)
                            Text("Plus Package Exclusive Opportunity", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftGoldContainer.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Plus members are eligible to apply. Once submitted, your application status will be Pending Admin Review.",
                            fontSize = 11.sp,
                            color = DeepTealDark,
                            lineHeight = 15.sp
                        )
                        HorizontalDivider(color = SoftGold, thickness = 0.5.dp)
                        Text(
                            text = "• Minimum Agent Float: UGX 50,000 (No maximum float purchase).\n• Company Float Bonus: +15% (After Admin approval, the company adds 15% of the approved float purchase to the Agent Float).\n• Example: UGX 50,000 float purchase + UGX 7,500 company bonus = UGX 57,500 total Agent Float.\n• Agent Commission: 2% (Separate from Agent Float, withdrawable according to existing commission rules).\n• Agent Working Float — Not Personal Withdrawable Income.",
                            fontSize = 10.sp,
                            color = DeepTealDark,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it; errorMsg = null },
                    label = { Text("Full Legal Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("agent_app_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Phone Number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it; errorMsg = null },
                    label = { Text("Contact Phone Number *") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("agent_app_phone")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Region Selection
                Text("Operating Region *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    regions.forEach { reg ->
                        val isSel = selectedRegion == reg
                        OutlinedButton(
                            onClick = { selectedRegion = reg },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSel) DeepTeal else WarmWhite,
                                contentColor = if (isSel) WarmWhite else DeepTealDark
                            )
                        ) {
                            Text(reg, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ID Type Selection
                Text("Identity Document Type *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    idTypes.forEach { idt ->
                        val isSel = selectedIdType == idt
                        OutlinedButton(
                            onClick = { selectedIdType = idt },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSel) DeepTeal else WarmWhite,
                                contentColor = if (isSel) WarmWhite else DeepTealDark
                            )
                        ) {
                            Text(idt.replace("National ID/NIN", "NIN"), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ID / NIN Number
                OutlinedTextField(
                    value = idNumber,
                    onValueChange = { idNumber = it.uppercase(); errorMsg = null },
                    label = { Text("ID / NIN Number *") },
                    placeholder = { Text("e.g. CM9084729183HG") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("agent_app_id_number")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Upload ID Document / Photo Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmCreamBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Upload ID Document / Photo *", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                                Text(idDocUri, fontSize = 10.sp, color = TextSecondary)
                            }
                            OutlinedButton(
                                onClick = { idDocUri = "National_ID_Front_Scan_${System.currentTimeMillis().toString().takeLast(4)}.pdf" },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Attach Document", fontSize = 10.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Optional Profile Photo Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmCreamBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Optional Profile Photo", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepTealDark)
                                Text(profilePhotoUri.ifBlank { "None attached" }, fontSize = 10.sp, color = TextSecondary)
                            }
                            OutlinedButton(
                                onClick = { profilePhotoUri = "agent_headshot_${System.currentTimeMillis().toString().takeLast(4)}.jpg" },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Attach Photo", fontSize = 10.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Application Reason
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it; errorMsg = null },
                    label = { Text("Application Reason / Background *") },
                    placeholder = { Text("Why do you want to become a HomeUtensils Agent?") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("agent_app_reason")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Responsibilities Agreement Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { agreed = !agreed },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = { agreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = DeepTeal)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "I confirm and agree to all agent responsibilities, including maintaining float transparency, 0% fee package facilitation, and regional compliance.",
                        fontSize = 10.sp,
                        color = TextPrimary,
                        lineHeight = 14.sp
                    )
                }

                errorMsg?.let { err ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(err, color = WarningRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (fullName.isBlank() || phoneNumber.isBlank() || idNumber.isBlank() || reason.isBlank()) {
                            errorMsg = "Please fill in all mandatory fields."
                            return@Button
                        }
                        if (!agreed) {
                            errorMsg = "Please confirm the responsibilities agreement."
                            return@Button
                        }
                        onSubmit(
                            fullName.trim(),
                            phoneNumber.trim(),
                            selectedRegion,
                            selectedIdType,
                            idNumber.trim(),
                            idDocUri,
                            profilePhotoUri,
                            reason.trim(),
                            agreed
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_submit_agent_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Agent Application", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
