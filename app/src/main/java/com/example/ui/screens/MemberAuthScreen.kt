package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText
import com.example.ui.viewmodel.MemberAuthMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberAuthScreen(
    authMode: MemberAuthMode,
    errorMessage: String?,
    onSwitchMode: (MemberAuthMode) -> Unit,
    onLogin: (phone: String, pass: String) -> Unit,
    onRegister: (name: String, phone: String, pass: String, inviterId: String, region: String) -> Unit,
    onBackToLanding: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackToLanding()
    }

    // Form local states
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var inviterId by remember { mutableStateOf("") }

    // Region: Exactly Central, Eastern, Northern, Western
    val validRegions = listOf("Central", "Eastern", "Northern", "Western")
    var selectedRegion by remember { mutableStateOf("Central") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("member_auth_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Navigation: Back to Landing
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToLanding,
                    modifier = Modifier.testTag("member_auth_back_button")
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
                        text = "Member Portal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Text(
                        text = "HomeUtensils Review Community",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Mode Switcher Tab (Login / Register)
        item {
            TabRow(
                selectedTabIndex = if (authMode == MemberAuthMode.LOGIN) 0 else 1,
                containerColor = WarmWhite,
                contentColor = DeepTeal,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[if (authMode == MemberAuthMode.LOGIN) 0 else 1]),
                        color = DeepTeal,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, WarmCreamBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = authMode == MemberAuthMode.LOGIN,
                    onClick = { onSwitchMode(MemberAuthMode.LOGIN) },
                    text = {
                        Text(
                            text = "Member Login",
                            fontWeight = if (authMode == MemberAuthMode.LOGIN) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_member_login")
                )

                Tab(
                    selected = authMode == MemberAuthMode.REGISTER,
                    onClick = { onSwitchMode(MemberAuthMode.REGISTER) },
                    text = {
                        Text(
                            text = "Register",
                            fontWeight = if (authMode == MemberAuthMode.REGISTER) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_member_register")
                )
            }
        }

        // Error Banner if any
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

        if (authMode == MemberAuthMode.LOGIN) {
            // LOGIN FORM
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Welcome Back",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        Text(
                            text = "Log in with your registered Uganda phone number",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Phone Number
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            placeholder = { Text("0700000000") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = DeepTeal)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("member_login_phone_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
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
                                .testTag("member_login_password_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Demo Credential Button
                        OutlinedButton(
                            onClick = {
                                phoneNumber = "0700000000"
                                password = "Member123!"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("member_demo_autofill_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Autofill Test Member (0700000000 / Member123!)", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onLogin(phoneNumber, password) },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("member_login_submit_button")
                        ) {
                            Text("Login to Member Portal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("New reviewer? ", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = "Register here",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal,
                                modifier = Modifier
                                    .clickable { onSwitchMode(MemberAuthMode.REGISTER) }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // REGISTER FORM
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Create Reviewer Account",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        Text(
                            text = "A random non-sequential Member ID will be generated upon registration",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name *") },
                            placeholder = { Text("e.g. Nalubega Sarah") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_fullname_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Phone Number
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number *") },
                            placeholder = { Text("+256 7...") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = DeepTeal)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_phone_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password *") },
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
                                .testTag("register_password_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Inviter / Referral ID
                        OutlinedTextField(
                            value = inviterId,
                            onValueChange = { inviterId = it },
                            label = { Text("Inviter / Referral ID (Optional)") },
                            placeholder = { Text("e.g. UG-SARAH-402 or HU2P9X14R") },
                            leadingIcon = {
                                Icon(Icons.Default.Kitchen, contentDescription = null, tint = DeepTeal)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_inviter_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Region Selection: Exactly Central, Eastern, Northern, Western
                        Text(
                            text = "Select Uganda Region *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            validRegions.forEach { region ->
                                val isSelected = selectedRegion == region
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedRegion = region },
                                    label = {
                                        Text(
                                            text = region,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepTeal,
                                        selectedLabelColor = WarmWhite,
                                        containerColor = WarmCreamSurface
                                    ),
                                    modifier = Modifier.weight(1f).testTag("region_chip_$region")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Fill Sample Data
                        OutlinedButton(
                            onClick = {
                                fullName = "Kato Ivan"
                                phoneNumber = "+256 701 984512"
                                password = "password123"
                                inviterId = "UG-SARAH-402"
                                selectedRegion = "Central"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_autofill_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Fill Sample Registration Data", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                onRegister(fullName, phoneNumber, password, inviterId, selectedRegion)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("register_submit_button")
                        ) {
                            Text("Register & Generate Member ID", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Already a member? ", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = "Login here",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal,
                                modifier = Modifier
                                    .clickable { onSwitchMode(MemberAuthMode.LOGIN) }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
