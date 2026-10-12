package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.ui.components.formatUgandaPhoneToInternational
import com.example.ui.components.openWhatsAppChatWithAgent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.maskMemberId
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentFacilitationEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.repository.RecipientLookupResult
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarningBadge
import com.example.ui.components.formatUgx
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AgentLoginScreen(
    errorMessage: String?,
    onLogin: (agentId: String, pass: String) -> Unit,
    onBackToLanding: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackToLanding() }

    var agentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("agent_login_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Back Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToLanding,
                    modifier = Modifier.testTag("agent_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to Landing",
                        tint = DeepTeal
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column {
                    Text(
                        text = "Agent Portal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "Field Operations & Verification",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (errorMessage != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarningRedContainer)
                        .padding(12.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningRedText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Login Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoftGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = SoftGoldDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Field Agent Authentication",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Authorized regional coordinators only",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Agent ID
                    OutlinedTextField(
                        value = agentId,
                        onValueChange = { agentId = it },
                        label = { Text("Agent ID *") },
                        placeholder = { Text("AGT-CENTRAL-001") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = DeepTeal)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("agent_id_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Agent Password *") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = DeepTeal)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("agent_password_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Demo Agent Prefill
                    OutlinedButton(
                        onClick = {
                            agentId = "AGT-CENTRAL-001"
                            password = "Agent123!"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("agent_demo_autofill_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Autofill Test Agent (AGT-CENTRAL-001 / Agent123!)", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onLogin(agentId, password) },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("agent_login_submit_button")
                    ) {
                        Text("Login to Agent Dashboard", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AgentDashboardScreen(
    agentId: String,
    workingFloat: AgentWorkingFloatEntity? = null,
    facilitations: List<AgentFacilitationEntity> = emptyList(),
    agentRequests: List<AgentActivationRequestEntity> = emptyList(),
    onLookupRecipient: (String) -> RecipientLookupResult = { RecipientLookupResult(it, it, "Member", true) },
    onFacilitatePackage: (recipientId: String, packageName: String) -> Unit = { _, _ -> },
    onConfirmPayment: (requestId: String) -> Unit = {},
    onDisputePayment: (requestId: String, reason: String) -> Unit = { _, _ -> },
    onManagePhoto: () -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onLogout() }

    var selectedTab by remember { mutableStateOf(0) }
    var recipientInput by remember { mutableStateOf("") }
    var selectedPackageName by remember { mutableStateOf("STARTER PACKAGE") }
    var actionMessage by remember { mutableStateOf<String?>(null) }

    // Dialog states for Agent Payment Protection
    var confirmingRequest by remember { mutableStateOf<AgentActivationRequestEntity?>(null) }
    var disputingRequest by remember { mutableStateOf<AgentActivationRequestEntity?>(null) }
    var disputeStatementInput by remember { mutableStateOf("") }
    var viewingProofRequest by remember { mutableStateOf<AgentActivationRequestEntity?>(null) }

    val currentFloatBalance = workingFloat?.workingFloatUgx ?: 500000L
    val packageCost = when (selectedPackageName) {
        "STARTER PACKAGE" -> 5000L
        "PLUS PACKAGE" -> 20000L
        else -> 10000L // STANDARD
    }

    val isRestricted = workingFloat?.isRestrictedFromNewRequests == true || (workingFloat?.status != null && workingFloat.status != "APPROVED")
    val pendingActionCount = agentRequests.count { 
        it.status == "PAYMENT_REPORTED" || it.status == "AWAITING_AGENT_CONFIRMATION" || it.status == "OVERDUE" 
    }

    val tabs = listOf(
        "Payment Protection ($pendingActionCount)",
        "Direct Facilitation",
        "Records & Field Ops"
    )

    val recipientLookup = remember(recipientInput) {
        if (recipientInput.trim().length >= 4) onLookupRecipient(recipientInput.trim()) else null
    }

    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCreamBackground)
                .testTag("agent_dashboard_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Agent Header & Profile Photo Management
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                // Agent Photo with Edit Badge
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(SoftGoldContainer)
                                        .clickable { onManagePhoto() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    val photoUri = workingFloat?.photoUri
                                    if (!photoUri.isNullOrBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(photoUri),
                                            contentDescription = "Agent Photo",
                                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.SupportAgent,
                                            contentDescription = null,
                                            tint = SoftGoldDark,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = workingFloat?.agentName ?: agentId.ifBlank { "Ronald Mukasa" },
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        if (isRestricted) {
                                            WarningBadge(text = workingFloat?.status ?: "RESTRICTED")
                                        } else {
                                            StatusBadge(text = "Official Agent")
                                        }
                                    }
                                    Text(
                                        text = "${workingFloat?.agentId ?: agentId} • ${workingFloat?.territory ?: "Central Region"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Return to Landing / Logout Button
                            Button(
                                onClick = onLogout,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WarningRedContainer,
                                    contentColor = WarningRedText
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("agent_logout_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Logout", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Official HomeUtensils Facilitator",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepTeal
                                )
                            }

                            OutlinedButton(
                                onClick = onManagePhoto,
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepTealDark),
                                modifier = Modifier.testTag("btn_agent_manage_photo")
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Update Photo", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // WhatsApp Contact Action Button
                        val context = LocalContext.current
                        val rawAgentPhone = workingFloat?.whatsappNumber?.ifBlank { workingFloat.phone } ?: "0772458928"
                        val formattedAgentPhone = formatUgandaPhoneToInternational(rawAgentPhone)

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                openWhatsAppChatWithAgent(
                                    context = context,
                                    rawPhone = rawAgentPhone,
                                    agentName = workingFloat?.agentName ?: "Agent"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF25D366),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("btn_agent_portal_whatsapp_chat")
                        ) {
                            Icon(Icons.Default.SupportAgent, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp — Contact Support / Test Chat ($formattedAgentPhone)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Unresolved Case / Restriction Warning Banner
            if (isRestricted || (workingFloat?.unresolvedCasesCount ?: 0) > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("agent_restriction_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = WarningRedContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRedText, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AGENT RESTRICTION ACTIVE",
                                    fontWeight = FontWeight.Bold,
                                    color = WarningRedText,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = workingFloat?.restrictionReason?.ifBlank { null }
                                        ?: "New Agent Activation Facilitation is temporarily restricted due to unresolved payment cases. Float funds remain safe. Please review and resolve pending requests below.",
                                    fontSize = 11.sp,
                                    color = WarningRedText,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // Agent Working Float & Balances Overview (Always visible)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("agent_working_float_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepTealDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = SoftGoldLight,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Agent Float",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmWhite
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SoftGold)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "0% P2P Fee",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTealDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Current Float Balance",
                                    fontSize = 11.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = formatUgx(currentFloatBalance),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SoftGoldContainer
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF00241E))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Working Capital",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SoftGoldLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.2f), thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // 1. AGENT FLOAT QUALIFICATION
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Minimum Agent Float: UGX 50,000",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmWhite
                                )
                                Text(
                                    text = "No maximum float purchase.",
                                    fontSize = 11.sp,
                                    color = SoftGoldLight
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF00241E))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Min: UGX 50,000",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2. COMPANY FLOAT BONUS & 3. EXAMPLE
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF00382E))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Company Float Bonus: +15%",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = StatusGreenText
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(StatusGreenText)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "+15%",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepTealDark
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "After Admin approval, the company adds 15% of the approved float purchase to the Agent Float.",
                                    fontSize = 11.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.95f),
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Why is there a 15% Company Float Bonus?",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGold
                                )
                                Text(
                                    text = "The 15% Company Float Bonus is provided by HomeUtensils to increase the Agent’s working float and help Agents serve members more effectively.",
                                    fontSize = 10.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.95f),
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.2f), thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // 4. SEPARATE COMMISSION
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SoftGoldContainer)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Agent Commission: 2%",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Withdrawable",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusGreenText
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Separate withdrawable Agent Commission balance.",
                                    fontSize = 10.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.85f),
                                    lineHeight = 14.sp
                                )
                            }

                            Text(
                                text = formatUgx(workingFloat?.commissionEarnedUgx ?: 14500L),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmWhite
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Why does an Agent receive 2% commission?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftGold
                        )
                        Text(
                            text = "The 2% Agent Commission rewards Agents for facilitating eligible member activations/reactivations and supporting the task activity associated with those activation cycles.",
                            fontSize = 10.sp,
                            color = WarmCreamBackground.copy(alpha = 0.9f),
                            lineHeight = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 5. IMPORTANT
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF00241E))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = SoftGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Agent Working Float — Not Personal Withdrawable Income.",
                                        color = SoftGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "The 15% Company Float Bonus is Agent working float. It is NOT personal withdrawable income. Working float is kept strictly separate from Personal Withdrawable Balance, Agent Commission, and Member P2P balance.",
                                    fontSize = 10.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.9f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Section: MEMBER-FACING AGENT INFORMATION & FINANCIAL SEPARATION
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("agent_float_bonus_rules_card"),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Agent Float & Commission Guide",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTealDark
                                )
                                Text(
                                    text = "Official HomeUtensils Working Float & Commission Architecture",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepTealContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "OFFICIAL RULES",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTealDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Explanatory Question 1: Why is there a 15% Company Float Bonus?
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftGoldContainer.copy(alpha = 0.35f)),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, SoftGold.copy(alpha = 0.6f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Why is there a 15% Company Float Bonus?",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTealDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "The 15% Company Float Bonus is provided by HomeUtensils to increase the Agent’s working float and help Agents serve members more effectively.",
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Explanatory Question 2: Why does an Agent receive 2% commission?
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = StatusGreenContainer.copy(alpha = 0.4f)),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, StatusGreenText.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = StatusGreenText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Why does an Agent receive 2% commission?",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DeepTealDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "The 2% Agent Commission rewards Agents for facilitating eligible member activations/reactivations and supporting the task activity associated with those activation cycles.",
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Financial Separation of Balances Cards
                        Text(
                            text = "Financial Separation of Balances",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DeepTealDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To ensure absolute financial integrity, four balances are maintained completely separate:",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("agent_float_dashboard_visuals"),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Row 1: Agent Float & Agent Commission
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Card 1: Agent Float
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("card_agent_float_current"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Agent Float",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Current Working Float:",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = formatUgx(currentFloatBalance),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Min: UGX 50,000 • No max",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                // Card 2: Agent Commission
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("card_agent_commission"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Agent Commission",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepTealDark
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(DeepTeal)
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = "2%",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = WarmWhite
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Withdrawable Balance:",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = formatUgx(workingFloat?.commissionEarnedUgx ?: 14500L),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Separate withdrawable",
                                            fontSize = 9.sp,
                                            color = StatusGreenText
                                        )
                                    }
                                }
                            }

                            // Row 2: Company Float Bonus & Personal Withdrawable Balance
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Card 3: Company Float Bonus (+15%)
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("card_company_bonus"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = StatusGreenContainer.copy(alpha = 0.5f)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreenText.copy(alpha = 0.3f))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Company Float Bonus",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusGreenText
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Approved Addition:",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "+15%",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = StatusGreenText
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Working Float Only",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = StatusGreenText
                                        )
                                    }
                                }

                                // Card 4: Personal Withdrawable Balance
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("card_personal_withdrawable_balance"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Personal Balance",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Member Withdrawable:",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "Personal Wallet",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Separate from Agent Float",
                                            fontSize = 9.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Important Financial Distinction Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF00241E))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = SoftGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Important Financial Distinction",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = SoftGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "“The 15% Company Float Bonus is Agent working float. It is NOT personal withdrawable income.”",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = WarmWhite,
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Agent Float is strictly operational capital for facilitating member package activations. It is kept completely separate from Personal Withdrawable Balance, Agent Commission, and Member P2P balance.",
                                    fontSize = 10.sp,
                                    color = WarmCreamBackground.copy(alpha = 0.9f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Tab Navigation for Agent Portal
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = WarmWhite,
                    contentColor = DeepTeal,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = DeepTeal
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, WarmCreamBorder, RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // TAB 0: AGENT PAYMENT PROTECTION REQUESTS & RESPONSES
                    if (agentRequests.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = WarmWhite)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(36.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "No Activation Requests Pending",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "When members select you in 'Find an Agent' and report payments, requests will appear here for verification.",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        items(agentRequests, key = { it.id }) { req ->
                            val isActionable = req.status in listOf("PAYMENT_REPORTED", "AWAITING_AGENT_CONFIRMATION", "AWAITING_AGENT_RESPONSE", "OVERDUE")
                            val isOverdue = req.status == "OVERDUE"
                            val isDisputed = req.status in listOf("PAYMENT_DISPUTED", "ADMIN_REVIEW")
                            val isCompleted = req.status in listOf("CONFIRMED", "ACTIVATION_COMPLETED", "RESOLVED")

                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("agent_request_card_${req.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    when {
                                        isOverdue -> WarningRed
                                        isActionable -> DeepTeal
                                        isDisputed -> SoftGoldDark
                                        else -> WarmCreamBorder
                                    }
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    // Status Header & Amount
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    when {
                                                        isOverdue -> WarningRedContainer
                                                        isActionable -> DeepTealContainer
                                                        isDisputed -> SoftGoldContainer
                                                        isCompleted -> StatusGreenContainer
                                                        else -> WarmCreamSurface
                                                    }
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = when {
                                                    isOverdue -> "RESPONSE OVERDUE"
                                                    isActionable -> "PAYMENT REPORTED"
                                                    isDisputed -> "PAYMENT DISPUTED"
                                                    isCompleted -> "ACTIVATION COMPLETED"
                                                    else -> req.status.replace("_", " ")
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isOverdue -> WarningRedText
                                                    isActionable -> DeepTealDark
                                                    isDisputed -> SoftGoldOnContainer
                                                    isCompleted -> StatusGreenText
                                                    else -> TextSecondary
                                                }
                                            )
                                        }

                                        Text(
                                            text = formatUgx(req.amountUgx),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepTealDark
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Member and Package Details
                                    Text(
                                        text = "Member: ${maskMemberId(req.memberId)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Package: ${req.packageName} • Region: ${req.region}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Reported: ${dateFormat.format(Date(if ((req.paymentReportedAt ?: 0L) > 0L) (req.paymentReportedAt ?: 0L) else req.createdAt))}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )

                                    if (req.paymentReference.isNotBlank()) {
                                        Text(
                                            text = "Ref: ${req.paymentReference} (${req.paymentMethod})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepTeal
                                        )
                                    }

                                    if (req.memberNote.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Note: \"${req.memberNote}\"",
                                            fontSize = 10.sp,
                                            color = TextSecondary,
                                            style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                        )
                                    }

                                    // Proof of Payment View Button
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedButton(
                                            onClick = { viewingProofRequest = req },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.testTag("btn_view_proof_${req.id}")
                                        ) {
                                            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("View Payment Proof", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        if (isActionable) {
                                            Text(
                                                text = "Action Required",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isOverdue) WarningRedText else DeepTeal
                                            )
                                        }
                                    }

                                    // Action buttons for Actionable cases (Confirm vs Dispute)
                                    if (isActionable) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.5f))
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { confirmingRequest = req },
                                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1.2f).testTag("btn_confirm_payment_${req.id}"),
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                                            ) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("CONFIRM PAYMENT & ACTIVATE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    disputeStatementInput = ""
                                                    disputingRequest = req
                                                },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRedText),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f).testTag("btn_dispute_payment_${req.id}"),
                                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                            ) {
                                                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("DISPUTE / NOT RECEIVED", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    if (isDisputed) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(SoftGoldContainer)
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "Disputed: \"${req.agentStatement.ifBlank { "Agent reported payment not received" }}\". HomeUtensils Admin is reviewing.",
                                                fontSize = 10.sp,
                                                color = SoftGoldOnContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: DIRECT FACILITATE (Manual Member Float Transfer)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("agent_facilitation_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Facilitate Member Package",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                Text(
                                    text = "Assist a new or renewing member by funding their package directly from your working float with 0% fee.",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Recipient Member ID input
                                OutlinedTextField(
                                    value = recipientInput,
                                    onValueChange = {
                                        recipientInput = it.uppercase()
                                        actionMessage = null
                                    },
                                    label = { Text("Recipient Member ID *") },
                                    placeholder = { Text("e.g. HU2P9X14R") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal)
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("agent_facilitate_recipient_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                // Lookup preview
                                recipientLookup?.let { lookup ->
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
                                                text = "Target Member: ${lookup.name} (${lookup.maskedId})",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StatusGreenText
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Select Package to Facilitate (0% Fee):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                val packages = listOf(
                                    Triple("STARTER PACKAGE", "Starter", 5000L),
                                    Triple("STANDARD PACKAGE", "Standard", 10000L),
                                    Triple("PLUS PACKAGE", "Plus", 20000L)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    packages.forEach { (pkgKey, label, cost) ->
                                        val isSelected = selectedPackageName == pkgKey
                                        OutlinedButton(
                                            onClick = { selectedPackageName = pkgKey },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) DeepTeal else WarmWhite,
                                                contentColor = if (isSelected) WarmWhite else DeepTealDark
                                            ),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) DeepTeal else WarmCreamBorder
                                            ),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                Text(formatUgx(cost), fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                actionMessage?.let { msg ->
                                    Text(text = msg, color = WarningRed, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                Button(
                                    onClick = {
                                        if (recipientInput.isBlank()) {
                                            actionMessage = "Please enter the recipient Member ID."
                                            return@Button
                                        }
                                        if (currentFloatBalance < packageCost) {
                                            actionMessage = "Insufficient Working Float. Required: ${formatUgx(packageCost)}, Available: ${formatUgx(currentFloatBalance)}."
                                            return@Button
                                        }
                                        onFacilitatePackage(recipientInput.trim(), selectedPackageName)
                                        actionMessage = "Package facilitation sent for $recipientInput!"
                                        recipientInput = ""
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_agent_confirm_facilitation"),
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Facilitate ${selectedPackageName.replace(" PACKAGE", "")} (${formatUgx(packageCost)})",
                                        fontWeight = FontWeight.Bold,
                                        color = WarmWhite
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: FACILITATION HISTORY & FIELD OPERATIONS
                    if (facilitations.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
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
                                        Text(
                                            text = "Facilitation Records",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTealDark
                                        )
                                        Text(
                                            text = "${facilitations.size} completed",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = WarmCreamBorder, thickness = 0.6.dp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    facilitations.forEach { item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "${item.packageName} • ${item.recipientMemberId}",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp,
                                                    color = TextPrimary
                                                )
                                                Text(
                                                    text = "${item.id} • ${dateFormat.format(Date(item.timestamp))} • Fee: UGX 0 (0%)",
                                                    fontSize = 10.sp,
                                                    color = TextSecondary
                                                )
                                            }

                                            Text(
                                                text = formatUgx(item.amountUgx),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = DeepTeal
                                            )
                                        }
                                        HorizontalDivider(color = WarmCreamBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                                    }
                                }
                            }
                        }
                    }

                    // Field Operations Overview
                    item {
                        Text(
                            text = "Field Operations Overview",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
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
                                        Text("Territory Reviewers", fontSize = 11.sp, color = TextSecondary)
                                        Icon(Icons.Default.Group, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("48 Active", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepTealDark)
                                    Text("Across Kampala & Wakiso", fontSize = 10.sp, color = TextSecondary)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
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
                                        Text("Batch Reviews", fontSize = 11.sp, color = TextSecondary)
                                        Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("312 Items", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepTealDark)
                                    Text("Cups, Plates & Pans", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }

                    // Agent Commission Strip
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(StatusGreenContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = null,
                                            tint = StatusGreenText,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column {
                                        Text("Territory Commission", fontSize = 11.sp, color = TextSecondary)
                                        Text("UGX 145,000", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepTeal)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(StatusGreenContainer)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text("Approved", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusGreenText)
                                }
                            }
                        }
                    }

                    // Field Actions
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
                        ) {
                            Column {
                                AgentActionRow(
                                    title = "Verify Reviewer National ID",
                                    subtitle = "Inspect and approve field submissions",
                                    icon = Icons.Default.CheckCircle
                                )
                                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                                AgentActionRow(
                                    title = "Register Local Kitchen Retailer",
                                    subtitle = "Add new cookware batches from local vendors",
                                    icon = Icons.Default.LocationOn
                                )
                                HorizontalDivider(color = WarmCreamBorder, thickness = 0.8.dp)
                                AgentActionRow(
                                    title = "Scan Batch QR / Barcode",
                                    subtitle = "Rapid intake for cups, spoons, bowls, and basins",
                                    icon = Icons.Default.QrCodeScanner
                                )
                            }
                        }
                    }
                }
            }

            // Return to Landing Screen Button
            item {
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = WarmCreamSurface),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = DeepTealDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Return to Portal Selection",
                        color = DeepTealDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // CONFIRM PAYMENT & ACTIVATE MODAL
        confirmingRequest?.let { req ->
            AlertDialog(
                onDismissRequest = { confirmingRequest = null },
                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(32.dp)) },
                title = { Text("Confirm Payment & Activate?", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "You are verifying that ${maskMemberId(req.memberId)} has legitimately transferred ${formatUgx(req.amountUgx)} to your Mobile Money account.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                        Card(
                            colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Atomic Float Impact:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                                Text("Current Float: ${formatUgx(currentFloatBalance)}", fontSize = 11.sp)
                                Text("Deduction: -${formatUgx(req.amountUgx)} (${req.packageName})", fontSize = 11.sp, color = DeepTeal)
                                Text("New Float: ${formatUgx(currentFloatBalance - req.amountUgx)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = "Activation cycle, paid capital records, and ledger entries will be created immediately. This action cannot be reversed.",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val id = req.id
                            confirmingRequest = null
                            onConfirmPayment(id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("dialog_confirm_activate_btn")
                    ) {
                        Text("Confirm & Deduct Float", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { confirmingRequest = null },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = WarmWhite,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // DISPUTE / PAYMENT NOT RECEIVED MODAL
        disputingRequest?.let { req ->
            AlertDialog(
                onDismissRequest = { disputingRequest = null },
                icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = WarningRed, modifier = Modifier.size(32.dp)) },
                title = { Text("Payment Not Received / Dispute", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "You are reporting that no payment of ${formatUgx(req.amountUgx)} was received from member ${maskMemberId(req.memberId)} under reference '${req.paymentReference}'.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Neither party will be automatically penalized. A payment protection dispute will be logged for HomeUtensils Administration review.",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = disputeStatementInput,
                            onValueChange = { disputeStatementInput = it },
                            label = { Text("Agent Dispute Statement / Explanation") },
                            placeholder = { Text("e.g. Checked MTN statement, no transaction matching reference.") },
                            modifier = Modifier.fillMaxWidth().testTag("dispute_statement_input"),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val id = req.id
                            val statement = disputeStatementInput.ifBlank { "Agent reported payment not received" }
                            disputingRequest = null
                            onDisputePayment(id, statement)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("dialog_submit_dispute_btn")
                    ) {
                        Text("Submit Dispute", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { disputingRequest = null },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = WarmWhite,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // VIEW PAYMENT PROOF MODAL
        viewingProofRequest?.let { req ->
            Dialog(onDismissRequest = { viewingProofRequest = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Payment Proof", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepTealDark)
                            IconButton(onClick = { viewingProofRequest = null }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmCreamSurface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (req.paymentProofUri.isNotBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(req.paymentProofUri),
                                        contentDescription = "Payment Proof Image",
                                        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.Receipt, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Official Payment Receipt", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Amount Claimed: ${formatUgx(req.amountUgx)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTealDark)
                                Text("Reference: ${req.paymentReference.ifBlank { "Not provided" }}", fontSize = 11.sp, color = TextPrimary)
                                Text("Channel: ${req.paymentMethod}", fontSize = 10.sp, color = TextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGoldContainer)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Rule 10 Security Notice: Do not trust screenshots alone. A screenshot must never move money automatically. Always verify against your live telecom statement before confirming.",
                                fontSize = 10.sp,
                                color = SoftGoldOnContainer,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewingProofRequest = null },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Dismiss Proof")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AgentActionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
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
                contentDescription = null,
                tint = DeepTeal,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
