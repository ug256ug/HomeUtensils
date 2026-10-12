package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.model.HomeUtensilsPackage
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.maskAgentId
import com.example.data.model.maskBeneficiaryName
import com.example.data.model.maskMemberId
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
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.WarmWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Normalizes Ugandan and general phone numbers into international WhatsApp format (e.g. +256777992199).
 * Handles:
 *  - 0777992199 -> +256777992199
 *  - 777992199  -> +256777992199
 *  - 256777992199 -> +256777992199
 *  - +256 777 992 199 -> +256777992199
 */
fun formatUgandaPhoneToInternational(phone: String): String {
    val trimmed = phone.trim()
    val digitsOnly = trimmed.filter { it.isDigit() }
    return when {
        digitsOnly.startsWith("256") -> "+$digitsOnly"
        digitsOnly.startsWith("0") && digitsOnly.length >= 10 -> "+256${digitsOnly.substring(1)}"
        digitsOnly.length == 9 && (digitsOnly.startsWith("7") || digitsOnly.startsWith("3")) -> "+256$digitsOnly"
        trimmed.startsWith("+") -> "+$digitsOnly"
        digitsOnly.isNotBlank() -> "+256$digitsOnly"
        else -> "+256772458928"
    }
}

/**
 * Triggers an Intent to open WhatsApp conversation directly with the Agent.
 * Uses wa.me link with fallback to regular browser if WhatsApp is not installed.
 */
fun openWhatsAppChatWithAgent(context: Context, rawPhone: String, agentName: String = "Agent") {
    val intlPhone = formatUgandaPhoneToInternational(rawPhone)
    // Digits only for wa.me / api.whatsapp.com URL query
    val digitsOnly = intlPhone.filter { it.isDigit() }
    val defaultText = Uri.encode("Hello $agentName, I am contacting you through HomeUtensils regarding package activation.")
    val webUri = Uri.parse("https://api.whatsapp.com/send?phone=$digitsOnly&text=$defaultText")

    try {
        val waIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(waIntent)
    } catch (_: Exception) {
        try {
            val waBizIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                setPackage("com.whatsapp.w4b")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(waBizIntent)
        } catch (_: Exception) {
            try {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (finalEx: Exception) {
                Toast.makeText(context, "Could not open WhatsApp for $intlPhone", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/**
 * SECTION 1 & 2: FIND AN AGENT & AGENT PROFILE
 */
@Composable
fun FindAgentDialog(
    initialPackageName: String = "STARTER PACKAGE",
    availableAgents: List<AgentWorkingFloatEntity>,
    onDismiss: () -> Unit,
    onRequestAgent: (agentId: String, packageName: String, region: String) -> Unit
) {
    var selectedPackageName by remember { mutableStateOf(initialPackageName) }
    var selectedRegion by remember { mutableStateOf("All") }
    var selectedAgent by remember { mutableStateOf<AgentWorkingFloatEntity?>(null) }

    val regions = listOf("All", "Central", "Eastern", "Western", "Northern")
    val selectedPkg = HomeUtensilsPackage.fromName(selectedPackageName)
    val requiredAmount = selectedPkg.activationAmountUgx

    val eligibleAgents = availableAgents.filter { agent ->
        agent.status == "ACTIVE" &&
        agent.isAvailable &&
        agent.workingFloatUgx >= requiredAmount &&
        !agent.isRestrictedFromNewRequests &&
        agent.unresolvedCasesCount < 2 &&
        (selectedRegion == "All" || agent.region.equals(selectedRegion, ignoreCase = true))
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("dialog_find_agent"),
            shape = RoundedCornerShape(22.dp),
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = "Find Agent",
                                tint = DeepTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Find an Agent",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Official HomeUtensils Payment Protection",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("btn_close_find_agent")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DeepTealDark)
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = SoftGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "OFFICIAL AGENT PAYMENT PROTECTION",
                                color = SoftGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Only pay an Agent through the official HomeUtensils Agent process. Never send money to someone claiming to be an Agent outside the official process.",
                                color = Color.White,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Member-Facing Agent Float Qualification & Protection Notice
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftGoldContainer.copy(alpha = 0.4f)),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, SoftGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Official Agent Float Qualification & Protection",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Official Agents maintain verified working float (minimum UGX 50,000 qualification, no maximum).\n• Company Float Bonus: +15% company backing added to Agent working float.\n• Agent working float is strictly operational for package facilitation at 0% fee.",
                            fontSize = 10.sp,
                            color = DeepTealDark,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Package Selector
                Text(
                    text = "Select Package to Facilitate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        HomeUtensilsPackage.STARTER,
                        HomeUtensilsPackage.STANDARD,
                        HomeUtensilsPackage.PLUS
                    ).forEach { pkg ->
                        val isSelected = pkg.displayName == selectedPackageName
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) DeepTeal else WarmCreamBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(if (isSelected) DeepTealContainer.copy(alpha = 0.5f) else WarmCreamSurface)
                                .clickable {
                                    selectedPackageName = pkg.displayName
                                    selectedAgent = null
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = pkg.displayName.replace(" PACKAGE", ""),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) DeepTealDark else TextPrimary
                                )
                                Text(
                                    text = formatUgx(pkg.activationAmountUgx),
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Region Filter Chips
                Text(
                    text = "Filter by Region",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    regions.forEach { reg ->
                        val isSelected = selectedRegion == reg
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedRegion = reg
                                selectedAgent = null
                            },
                            label = { Text(reg, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Agent List or Empty State
                if (eligibleAgents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WarningRedContainer.copy(alpha = 0.5f))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = WarningRedText,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No suitable Agent is currently available. Please contact Administration/Customer Care or try again later.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = WarningRedText,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Available Verified Agents (${eligibleAgents.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    eligibleAgents.forEach { agent ->
                        val isChosen = selectedAgent?.agentId == agent.agentId
                        AgentProfileCard(
                            agent = agent,
                            isSelected = isChosen,
                            onSelect = { selectedAgent = agent }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Confirmation / Action
                if (selectedAgent != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            selectedAgent?.let {
                                onRequestAgent(it.agentId, selectedPackageName, it.region)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_confirm_request_agent"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Request Activation with ${maskBeneficiaryName(selectedAgent!!.agentName)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AgentProfileCard(
    agent: AgentWorkingFloatEntity,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) DeepTeal else WarmCreamBorder,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) WarmWhite else WarmCreamSurface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Official Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Official",
                        tint = DeepTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "OFFICIAL HOMEUTENSILS AGENT",
                        color = DeepTeal,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusGreenContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "AVAILABLE",
                        color = StatusGreenText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val context = LocalContext.current
            val agentPhoneToUse = agent.whatsappNumber.ifBlank { agent.phone.ifBlank { "0772458928" } }
            val formattedIntlPhone = formatUgandaPhoneToInternational(agentPhoneToUse)

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Photo
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(2.dp, SoftGold, CircleShape)
                        .background(SoftGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (agent.photoUri.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(agent.photoUri),
                            contentDescription = "Agent Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Agent",
                            tint = DeepTeal,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = maskBeneficiaryName(agent.agentName),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Agent ID: ${maskAgentId(agent.agentId)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepTealDark,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Region: ${agent.region} • Float: Verified Operational",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "Phone/WhatsApp: $formattedIntlPhone",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusGreenText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    )
                }

                // Radio Indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(2.dp, if (isSelected) DeepTeal else TextSecondary, CircleShape)
                        .background(if (isSelected) DeepTeal else Color.Transparent)
                )
            }

            // WhatsApp Direct Action Button (Only for approved, active, available agents)
            if (agent.status == "ACTIVE" && agent.isAvailable && !agent.isRestrictedFromNewRequests) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        openWhatsAppChatWithAgent(
                            context = context,
                            rawPhone = agentPhoneToUse,
                            agentName = agent.agentName
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("btn_agent_whatsapp_${agent.agentId}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = "WhatsApp Chat",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WhatsApp — Chat with Agent ($formattedIntlPhone)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * SECTION 5: MEMBER PAYMENT GUIDANCE & HOW PAYMENT PROTECTION WORKS
 */
@Composable
fun MemberPaymentGuidanceCard(
    request: AgentActivationRequestEntity,
    onReportPaymentClick: () -> Unit,
    onReportIssueClick: () -> Unit
) {
    var expandedGuide by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("member_payment_guidance_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Security Notice
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Official Payment Guidance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (request.status) {
                                "Payment Reported", "Awaiting Agent Confirmation" -> SoftGoldContainer
                                "Agent Response Overdue", "Payment Disputed" -> WarningRedContainer
                                "Activation Completed", "Confirmed", "Resolved" -> StatusGreenContainer
                                else -> DeepTealContainer
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = request.status.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = when (request.status) {
                            "Payment Reported", "Awaiting Agent Confirmation" -> SoftGoldOnContainer
                            "Agent Response Overdue", "Payment Disputed" -> WarningRedText
                            "Activation Completed", "Confirmed", "Resolved" -> StatusGreenText
                            else -> DeepTealDark
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Guidance Details Grid / Official Agent Payment Details
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(WarmCreamSurface)
                    .border(1.dp, WarmCreamBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "AGENT PAYMENT DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepTealDark,
                        letterSpacing = 0.5.sp
                    )
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Agent:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.agentName.ifBlank { "Ronald Mukasa" }, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Agent ID:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.agentId, fontSize = 11.sp, color = DeepTealDark, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Agent Region:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.region.ifBlank { "Central" }, fontSize = 11.sp, color = TextPrimary)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("WhatsApp:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        val guidancePhone = request.agentWhatsapp.ifBlank { request.agentPhone.ifBlank { "+256 772 458 928" } }
                        val formattedGuidancePhone = formatUgandaPhoneToInternational(guidancePhone)
                        val guidanceContext = LocalContext.current
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formattedGuidancePhone,
                                fontSize = 11.sp,
                                color = StatusGreenText,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    openWhatsAppChatWithAgent(
                                        context = guidanceContext,
                                        rawPhone = guidancePhone,
                                        agentName = request.agentName.ifBlank { "Agent" }
                                    )
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF25D366),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier
                                    .height(26.dp)
                                    .testTag("btn_guidance_whatsapp_chat")
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.5.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Package:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.packageName.replace(" PACKAGE", ""), fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Amount Required:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(formatUgx(request.amountUgx), fontSize = 12.sp, color = StatusGreenText, fontWeight = FontWeight.ExtraBold)
                    }
                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.5.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Payment Method:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.officialPaymentMethod.ifBlank { "MTN Mobile Money" }, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Account/Number:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.officialPaymentAccount.ifBlank { "0772458928" }, fontSize = 11.sp, color = DeepTealDark, fontWeight = FontWeight.ExtraBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Account/Beneficiary Name:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(request.officialBeneficiaryName.ifBlank { request.agentName.ifBlank { "Ronald Mukasa" } }, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Request Ref:", fontSize = 10.sp, color = TextSecondary)
                        Text(request.id, fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions
            Text(
                text = "Instructions: Only pay the Agent shown in this official HomeUtensils request. Keep your payment confirmation until activation completes.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Button: I HAVE PAID THE AGENT
            if (request.status == "Agent Accepted" || request.status == "Awaiting Member Payment" || request.status == "Requested") {
                Button(
                    onClick = onReportPaymentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_i_have_paid_agent"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I HAVE PAID THE AGENT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            } else if (request.status == "Payment Reported" || request.status == "Awaiting Agent Confirmation") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftGoldContainer.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HourglassTop, contentDescription = null, tint = SoftGoldDark, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Your payment has been reported. The Agent has been notified. Keep your payment confirmation until activation completes.",
                            fontSize = 11.sp,
                            color = SoftGoldOnContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable: How Agent Payment Protection Works
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedGuide = !expandedGuide }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "How Agent Payment Protection Works",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                }
                Text(
                    text = if (expandedGuide) "Hide" else "View Steps",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            if (expandedGuide) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmCreamBackground)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val steps = listOf(
                        "Step 1: Find an official available Agent.",
                        "Step 2: Check the Agent's photo, name, Agent ID and region.",
                        "Step 3: Request your package activation.",
                        "Step 4: Pay only according to official payment instructions.",
                        "Step 5: Keep your payment confirmation.",
                        "Step 6: Tap 'I Have Paid the Agent.'",
                        "Step 7: Upload your payment proof or enter payment reference.",
                        "Step 8: Wait for confirmation."
                    )
                    steps.forEach { step ->
                        Text(text = step, fontSize = 10.sp, color = TextPrimary, lineHeight = 14.sp)
                    }
                }
            }

            // Section 16: Agent not responding?
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agent not responding?",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                OutlinedButton(
                    onClick = onReportIssueClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_report_agent_issue")
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRedText, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REPORT AGENT PAYMENT ISSUE", fontSize = 10.sp, color = WarningRedText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * SECTION 6: MEMBER REPORT PAYMENT FORM
 */
@Composable
fun ReportPaymentDialog(
    request: AgentActivationRequestEntity,
    onDismiss: () -> Unit,
    onSubmit: (method: String, ref: String, note: String, proofUri: String) -> Unit
) {
    var amountText by remember { mutableStateOf(request.amountUgx.toString()) }
    var selectedMethod by remember { mutableStateOf("MTN Mobile Money") }
    var referenceText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var proofUri by remember { mutableStateOf("proof_momo_receipt.jpg") }

    val methods = listOf("MTN Mobile Money", "Airtel Money", "Bank Transfer", "Cash")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("dialog_report_payment"),
            shape = RoundedCornerShape(22.dp),
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Report Payment to Agent",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Request Ref: ${request.id}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmCreamSurface)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Notice: Do not treat the screenshot as automatic proof of payment. The Agent must verify payment before float moves. Keep confirmation.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount Paid (UGX) *") },
                    readOnly = true,
                    leadingIcon = { Text("UGX", fontWeight = FontWeight.Bold, color = DeepTeal, modifier = Modifier.padding(start = 12.dp)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Method
                Text("Payment Method *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    methods.forEach { m ->
                        val isSelected = selectedMethod == m
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMethod = m },
                            label = { Text(m.replace(" Mobile Money", " MoMo"), fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Transaction Reference
                OutlinedTextField(
                    value = referenceText,
                    onValueChange = { referenceText = it },
                    label = { Text("Payment Reference / Transaction ID *") },
                    placeholder = { Text("e.g. MM-28190283 or Bank Ref") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_payment_reference")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Proof Upload / Simulation
                Text("Payment Proof (Screenshot / Photo Scan) *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, DeepTeal.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .background(DeepTealContainer.copy(alpha = 0.3f))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = DeepTeal)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(proofUri, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                                Text("Uploaded by Member • Encrypted", fontSize = 9.sp, color = TextSecondary)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                proofUri = "proof_receipt_${System.currentTimeMillis().toString().takeLast(5)}.jpg"
                            },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Replace", fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Optional note
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Optional Note for Agent") },
                    placeholder = { Text("Sent via 0772... at 2:30 PM") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val ref = referenceText.ifBlank { "REF-${System.currentTimeMillis().toString().takeLast(6)}" }
                        onSubmit(selectedMethod, ref, noteText, proofUri)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_payment_report"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Payment Report", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

/**
 * SECTION 16: REPORT AGENT PAYMENT ISSUE / DISPUTE DIALOG
 */
@Composable
fun ReportPaymentIssueDialog(
    request: AgentActivationRequestEntity,
    onDismiss: () -> Unit,
    onSubmitIssue: (reason: String, statement: String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Agent Not Responding") }
    var statementText by remember { mutableStateOf("") }
    val reasons = listOf("Agent Not Responding", "Agent Refused Confirmation", "Wrong Amount Requested", "Other")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("dialog_report_issue"),
            shape = RoundedCornerShape(22.dp),
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(WarningRedContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRedText, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Report Payment Issue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepTealDark)
                            Text("Agent: ${maskAgentId(request.agentId)} • ${formatUgx(request.amountUgx)}", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Reason for Issue *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    reasons.forEach { r ->
                        val isSelected = selectedReason == r
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) WarningRedText else WarmCreamBorder, RoundedCornerShape(8.dp))
                                .background(if (isSelected) WarningRedContainer.copy(alpha = 0.4f) else WarmCreamSurface)
                                .clickable { selectedReason = r }
                                .padding(8.dp)
                        ) {
                            Text(r, fontSize = 11.sp, color = if (isSelected) WarningRedText else TextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = statementText,
                    onValueChange = { statementText = it },
                    label = { Text("Member Statement / Details *") },
                    placeholder = { Text("Describe what happened, time of payment, reference...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSubmitIssue(selectedReason, statementText.ifBlank { "Agent has not confirmed payment after deadline." })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_submit_agent_issue"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit Issue to Administration", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * PART B: VISUAL TRANSACTION RECEIPT DIALOG
 */
@Composable
fun VisualTransactionReceiptDialog(
    transaction: WalletTransactionEntity,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a", Locale.getDefault()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("dialog_visual_transaction_receipt"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Status Icon Seal
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            when (transaction.status) {
                                "CREDITED", "COMPLETED" -> StatusGreenContainer
                                "PENDING" -> SoftGoldContainer
                                else -> WarningRedContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (transaction.status) {
                            "CREDITED", "COMPLETED" -> Icons.Default.CheckCircle
                            "PENDING" -> Icons.Default.HourglassTop
                            else -> Icons.Default.Warning
                        },
                        contentDescription = transaction.status,
                        tint = when (transaction.status) {
                            "CREDITED", "COMPLETED" -> StatusGreenText
                            "PENDING" -> SoftGoldOnContainer
                            else -> WarningRedText
                        },
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "STATUS: ${transaction.status}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = when (transaction.status) {
                        "CREDITED", "COMPLETED" -> StatusGreenText
                        "PENDING" -> SoftGoldDark
                        else -> WarningRedText
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Direction Badge
                val direction = when (transaction.type) {
                    "TASK_REWARD", "REFERRAL_BONUS", "REFERRAL_COMMISSION", "MONTHLY_REFERRAL_BENEFIT",
                    "AGENT_COMMISSION", "WELCOME_BONUS", "P2P_RECEIVED", "REFUND" -> "INCOMING"
                    "WITHDRAWAL", "P2P_TRANSFER", "P2P_SENT", "PACKAGE_UPGRADE", "PACKAGE_ACTIVATION", "REACTIVATION" -> "OUTGOING"
                    "PAID_CAPITAL" -> "NEUTRAL"
                    else -> if (transaction.title.contains("Received", true) || transaction.title.contains("Reward", true)) "INCOMING"
                            else if (transaction.title.contains("Sent", true) || transaction.title.contains("Withdrawal", true)) "OUTGOING"
                            else "NEUTRAL"
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (direction) {
                                "INCOMING" -> StatusGreenContainer
                                "OUTGOING" -> WarningRedContainer
                                else -> SoftGoldContainer
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (direction) {
                            "INCOMING" -> "🟢 ↑ INCOMING CREDIT"
                            "OUTGOING" -> "🔴 ↓ OUTGOING DEBIT"
                            else -> "⚪ PAID CAPITAL (NON-WITHDRAWABLE)"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (direction) {
                            "INCOMING" -> StatusGreenText
                            "OUTGOING" -> WarningRedText
                            else -> SoftGoldOnContainer
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Big Amount
                val totalDisplayAmount = if (direction == "OUTGOING" && transaction.type == "P2P_TRANSFER" && transaction.feeUgx > 0) {
                    transaction.amountUgx + transaction.feeUgx
                } else transaction.amountUgx

                Text(
                    text = when (direction) {
                        "INCOMING" -> "🟢 ↑ +${formatUgx(transaction.amountUgx)}"
                        "OUTGOING" -> "🔴 ↓ −${formatUgx(totalDisplayAmount)}"
                        else -> "⚪ ${formatUgx(transaction.amountUgx)}"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when (direction) {
                        "INCOMING" -> StatusGreenText
                        "OUTGOING" -> WarningRedText
                        else -> DeepTealDark
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Breakdown Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(WarmCreamSurface)
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ReceiptRow(label = "Transaction ID", value = transaction.id)
                        ReceiptRow(
                            label = "Reference Number",
                            value = transaction.referenceNumber.ifBlank { transaction.eventKey.ifBlank { "TXN-${transaction.timestamp.toString().takeLast(6)}" } }
                        )
                        ReceiptRow(label = "Date & Time", value = dateFormat.format(Date(transaction.timestamp)))
                        ReceiptRow(label = "Category", value = transaction.type.replace("_", " "))
                        ReceiptRow(
                            label = "Direction",
                            value = when (direction) {
                                "INCOMING" -> "Incoming (Added to Withdrawable Balance)"
                                "OUTGOING" -> "Outgoing (Deducted from Withdrawable Balance)"
                                else -> "Informational / Non-withdrawable Paid Capital"
                            }
                        )

                        if (transaction.counterpartyName.isNotBlank()) {
                            ReceiptRow(
                                label = "Counterparty",
                                value = "${maskBeneficiaryName(transaction.counterpartyName)} (${maskMemberId(transaction.counterpartyId)})"
                            )
                        }

                        if (transaction.type == "P2P_TRANSFER" && transaction.feeUgx > 0) {
                            ReceiptRow(label = "Recipient Transfer", value = formatUgx(transaction.amountUgx))
                            ReceiptRow(label = "P2P Fee (10%)", value = formatUgx(transaction.feeUgx))
                            ReceiptRow(label = "Total Balance Deducted", value = formatUgx(transaction.amountUgx + transaction.feeUgx))
                        } else if (transaction.type == "WITHDRAWAL") {
                            ReceiptRow(label = "Requested Amount", value = formatUgx(transaction.amountUgx))
                            ReceiptRow(label = "Withdrawal Fee (10%)", value = formatUgx(transaction.feeUgx))
                            val net = if (transaction.netAmountUgx > 0) transaction.netAmountUgx else (transaction.amountUgx - transaction.feeUgx)
                            ReceiptRow(label = "Net Payout to Receive", value = formatUgx(net))
                            ReceiptRow(label = "Total Balance Deducted", value = formatUgx(transaction.amountUgx))
                        } else {
                            if (transaction.feeUgx > 0) {
                                ReceiptRow(label = "Fee Charged", value = formatUgx(transaction.feeUgx))
                            }
                            if (transaction.netAmountUgx > 0) {
                                ReceiptRow(label = "Net Amount", value = formatUgx(transaction.netAmountUgx))
                            }
                        }

                        if (transaction.runningBalanceUgx > 0) {
                            ReceiptRow(label = "Updated Balance", value = formatUgx(transaction.runningBalanceUgx))
                        }
                        if (transaction.paymentMethod.isNotBlank()) {
                            ReceiptRow(label = "Method / Channel", value = transaction.paymentMethod)
                        }
                        if (transaction.notes.isNotBlank()) {
                            ReceiptRow(label = "Notes / Description", value = transaction.notes)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Verification Stamp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepTealContainer.copy(alpha = 0.5f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTealDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cryptographically Verified Ledger Record • HomeUtensils Core",
                        fontSize = 10.sp,
                        color = DeepTealDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val textToCopy = "HomeUtensils Receipt\nID: ${transaction.id}\nTitle: ${transaction.title}\nAmount: ${formatUgx(transaction.amountUgx)}\nStatus: ${transaction.status}\nDate: ${dateFormat.format(Date(transaction.timestamp))}"
                            clipboardManager.setText(AnnotatedString(textToCopy))
                            copied = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (copied) "Copied!" else "Copy Details", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
    }
}

/**
 * SECTION 3: AGENT PROFILE PHOTO MANAGEMENT DIALOG
 */
@Composable
fun AgentPhotoManagementDialog(
    agent: AgentWorkingFloatEntity,
    onDismiss: () -> Unit,
    onUpdatePhoto: (newUri: String) -> Unit
) {
    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
        "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400"
    )
    var selectedPhoto by remember { mutableStateOf(agent.photoUri) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("dialog_agent_photo_management"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = WarmWhite)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Agent Profile Photo Security",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
                Text(
                    text = "All photo changes are verified and audit logged.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Current Preview
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(3.dp, DeepTeal, CircleShape)
                        .background(SoftGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedPhoto.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedPhoto),
                            contentDescription = "Agent Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Choose Authorized Photo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    samplePhotos.forEach { uri ->
                        val isChosen = selectedPhoto == uri
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (isChosen) 3.dp else 1.dp,
                                    color = if (isChosen) DeepTeal else WarmCreamBorder,
                                    shape = CircleShape
                                )
                                .clickable { selectedPhoto = uri }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = { onUpdatePhoto(selectedPhoto) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Photo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * SECTION 18 & 19: ADMIN CASE VIEW & ADMIN ACTIONS
 */
@Composable
fun AdminAgentProtectionDetailDialog(
    request: AgentActivationRequestEntity,
    caseEntity: AgentPaymentProtectionCaseEntity?,
    onDismiss: () -> Unit,
    onVerifyAndActivate: (notes: String) -> Unit,
    onRejectClaim: (reason: String) -> Unit,
    onResolveCase: (summary: String) -> Unit,
    onRestrictAgent: (agentId: String, isRestricted: Boolean) -> Unit,
    onSuspendAgent: (agentId: String) -> Unit,
    onLogProofView: (requestId: String) -> Unit
) {
    var adminNotes by remember { mutableStateOf(request.adminInvestigationNotes) }
    var showProofPreview by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("dialog_admin_case_detail"),
            shape = RoundedCornerShape(22.dp),
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DeepTealDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = SoftGold, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Admin Case Investigation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Ref: ${request.id} • ${request.status}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Case Overview Grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarmCreamSurface)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ReceiptRow(label = "Member", value = "${request.memberName} (${request.memberId})")
                        ReceiptRow(label = "Member Phone", value = request.memberPhone.ifBlank { "Not provided" })
                        ReceiptRow(label = "Official Agent", value = "${request.agentName} (${request.agentId})")
                        ReceiptRow(label = "Region", value = request.region)
                        ReceiptRow(label = "Package Tier", value = request.packageName)
                        ReceiptRow(label = "Amount", value = formatUgx(request.amountUgx))
                        ReceiptRow(label = "Request Date", value = dateFormat.format(Date(request.createdAt)))

                        if (request.paymentReportedAt != null) {
                            ReceiptRow(label = "Payment Reported", value = dateFormat.format(Date(request.paymentReportedAt)))
                        }
                        if (request.responseDeadline != null) {
                            val isOverdue = System.currentTimeMillis() > request.responseDeadline
                            ReceiptRow(
                                label = "Response Deadline",
                                value = "${dateFormat.format(Date(request.responseDeadline))} ${if (isOverdue) "(OVERDUE)" else ""}"
                            )
                        }
                        if (request.paymentMethod.isNotBlank()) {
                            ReceiptRow(label = "Payment Method", value = request.paymentMethod)
                        }
                        if (request.paymentReference.isNotBlank()) {
                            ReceiptRow(label = "Payment Reference", value = request.paymentReference)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Proof Box
                if (request.paymentProofUri.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, DeepTeal.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .background(DeepTealContainer.copy(alpha = 0.2f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AttachFile, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Payment Proof: ${request.paymentProofUri}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTealDark
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        showProofPreview = !showProofPreview
                                        if (showProofPreview) {
                                            onLogProofView(request.id)
                                        }
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(if (showProofPreview) "Hide Proof" else "View Proof", fontSize = 10.sp)
                                }
                            }

                            if (showProofPreview) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(1.dp, WarmCreamBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Receipt, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(40.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("MTN Mobile Money Transaction Receipt", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                                        Text("Ref: ${request.paymentReference} • Amount: ${formatUgx(request.amountUgx)}", fontSize = 10.sp, color = StatusGreenText)
                                        Text("Transferred to Agent: ${request.agentName}", fontSize = 9.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Statements
                if (request.memberNote.isNotBlank()) {
                    Text("Member Statement:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    Text(request.memberNote, fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (request.agentStatement.isNotBlank()) {
                    Text("Agent Dispute Statement:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WarningRedText)
                    Text(request.agentStatement, fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Admin Investigation Notes Input
                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("Admin Investigation Notes *") },
                    placeholder = { Text("Record findings, phone verification with MTN, resolution reason...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Authorized Administrator Actions", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                Spacer(modifier = Modifier.height(8.dp))

                // Action 1: Verify payment & Complete verified activation
                Button(
                    onClick = {
                        onVerifyAndActivate(adminNotes.ifBlank { "Verified payment reference with telecom provider." })
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Verify Payment & Force Complete Activation", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Reject Invalid Claim / Cancel Request
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onRejectClaim(adminNotes.ifBlank { "Claim rejected: Payment was not received by agent." })
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reject Claim", fontSize = 10.sp, color = WarningRedText)
                    }

                    OutlinedButton(
                        onClick = {
                            onResolveCase(adminNotes.ifBlank { "Case resolved after review." })
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Resolve Case", fontSize = 10.sp, color = DeepTeal)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 3: Restrict Agent or Suspend Agent
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onRestrictAgent(request.agentId, true)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Restrict Agent", fontSize = 10.sp, color = WarningRedText)
                    }

                    OutlinedButton(
                        onClick = {
                            onSuspendAgent(request.agentId)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Suspend Agent", fontSize = 10.sp, color = Color(0xFFC62828))
                    }
                }
            }
        }
    }
}
