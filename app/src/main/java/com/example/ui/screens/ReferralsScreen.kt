package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReferralEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.LocationBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarningBadge
import com.example.ui.components.formatUgx
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldContainer
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

@Composable
fun ReferralsScreen(
    userProfile: UserProfileEntity?,
    referrals: List<ReferralEntity>,
    onClaimCode: (String) -> Unit,
    onSimulateInvite: (name: String, phone: String, district: String, packageName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var claimInput by remember { mutableStateOf("") }
    var showClaimField by remember { mutableStateOf(false) }
    var selectedSimPackage by remember { mutableStateOf("STANDARD PACKAGE") }

    val activeCount = referrals.count { it.status == "ACTIVE" }
    val totalReferralEarned = referrals.filter { it.status == "ACTIVE" }.sumOf { it.bonusUgx }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("referrals_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Invite Hero & Referral Code Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("referral_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Uganda Referral Rewards",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGoldContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+ UGX 2,000 / Invite",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SoftGoldOnContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Invite friends, family, and neighbors across Kampala and all districts of Uganda. When they register and verify their first household utensil task, you both receive UGX 2,000!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Code Box
                    val referralCode = userProfile?.referralCode ?: "UG-HOME-100"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WarmCreamSurface)
                            .border(1.5.dp, SoftGold.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "YOUR REFERRAL CODE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = referralCode,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepTeal
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("HomeUtensils Referral Code", referralCode)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Code $referralCode copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(DeepTealContainer)
                                        .testTag("copy_referral_code_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy code",
                                        tint = DeepTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Join HomeUtensils in Uganda and earn UGX with household utensil image tasks! Use my code: $referralCode"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Share HomeUtensils Code"))
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SoftGoldContainer)
                                        .testTag("share_referral_code_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = SoftGoldOnContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Referral Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Friends Invited",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "${referrals.size}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Active & Verified",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "$activeCount",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = StatusGreen
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1.3f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmWhite)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Referral UGX Earned",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = formatUgx(totalReferralEarned),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SoftGoldDark
                        )
                    }
                }
            }
        }

        // Claim code / Quick invite tools
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
                        Text(
                            text = "Were you referred by a friend?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        OutlinedButton(
                            onClick = { showClaimField = !showClaimField },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (showClaimField) "Hide" else "Enter Code")
                        }
                    }

                    if (showClaimField) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = claimInput,
                                onValueChange = { claimInput = it },
                                placeholder = { Text("e.g., UG-KATO-881") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("claim_code_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DeepTeal,
                                    unfocusedBorderColor = WarmCreamBorder
                                ),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    if (claimInput.isNotBlank()) {
                                        onClaimCode(claimInput.trim())
                                        claimInput = ""
                                        showClaimField = false
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DeepTeal,
                                    contentColor = WarmWhite
                                ),
                                modifier = Modifier.testTag("apply_code_button")
                            ) {
                                Text("Claim")
                            }
                        }
                    }
                }
            }
        }

        // Commission Rules Card (Rule 4: 10% Direct Activation Referral Commission)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Direct Package Activation Commission (10%)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "When your invited friend activates a package, 10% direct commission is automatically credited to your Withdrawable Balance. No Admin approval needed.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Starter
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarmCreamSurface)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("STARTER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text("+UGX 500", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                        }

                        // Standard
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGoldContainer)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("STANDARD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SoftGoldOnContainer)
                                Text("+UGX 1,000", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                        }

                        // Plus
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DeepTealContainer)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("PLUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepTealDark)
                                Text("+UGX 2,000", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = DeepTealDark)
                            }
                        }
                    }
                }
            }
        }

        // Demo Helper: Simulate New Referral Signup & Activation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Test Referral Activation",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Simulate friend joining & activating package",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                val names = listOf("Mukasa Joseph", "Namaganda Betty", "Ssemwanga Paul", "Nantongo Flavia")
                                val districts = listOf("Mbarara", "Jinja", "Gulu", "Masaka", "Entebbe")
                                val chosenName = names.random()
                                val chosenDistrict = districts.random()
                                val randomPhone = "+256 7" + (70..79).random() + " " + (100..999).random() + (100..999).random()
                                onSimulateInvite(chosenName, randomPhone, chosenDistrict, selectedSimPackage)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftGold,
                                contentColor = DeepTealDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("simulate_invite_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = "Simulate",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simulate", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("STARTER PACKAGE", "STANDARD PACKAGE", "PLUS PACKAGE").forEach { pkg ->
                            val isSelected = selectedSimPackage == pkg
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) DeepTeal else WarmWhite)
                                    .clickable { selectedSimPackage = pkg }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = pkg.replace(" PACKAGE", ""),
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) WarmWhite else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Referral List Header
        item {
            Text(
                text = "Your Invited Friends (${referrals.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Referral List Items
        items(referrals, key = { it.id }) { ref ->
            ReferralFriendCard(referral = ref)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReferralFriendCard(
    referral: ReferralEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("referral_item_${referral.id}"),
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
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(DeepTealContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = referral.friendName,
                    tint = DeepTeal,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = referral.friendName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${referral.friendPhone} • ${referral.district}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = "Joined: ${referral.joinedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                if (referral.status == "ACTIVE") {
                    StatusBadge(text = "Active")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+ " + formatUgx(referral.bonusUgx),
                        style = MaterialTheme.typography.labelSmall,
                        color = StatusGreenText,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftGoldContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Pending Task",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftGoldOnContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
