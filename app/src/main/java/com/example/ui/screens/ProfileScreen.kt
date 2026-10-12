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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.UserProfileEntity
import com.example.ui.components.LocationBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.PackageBadgeChip
import com.example.ui.components.PackageTierIcon
import com.example.ui.components.PackageVisualTier
import com.example.ui.components.RoyalCrownIcon
import com.example.ui.components.formatUgx
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ui.theme.PlusCrownGold
import com.example.ui.theme.PlusPurpleDark
import com.example.ui.theme.StandardBlue
import com.example.ui.theme.StarterGreenDark
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite

val UgandaDistricts = listOf(
    "Kampala Central",
    "Wakiso (Nansana / Kira)",
    "Mukono",
    "Jinja City",
    "Mbarara City",
    "Gulu City",
    "Entebbe Municipality",
    "Masaka City",
    "Fort Portal",
    "Mbale City"
)

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    showDistrictDialog: Boolean,
    onShowDistrictDialog: (Boolean) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onToggleRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("profile_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_header_card"),
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Profile Photo with image or fallback
                        val context = LocalContext.current
                        val avatarResId = remember {
                            val id = context.resources.getIdentifier(
                                "img_sarah_avatar_1791616150174",
                                "drawable",
                                context.packageName
                            )
                            if (id != 0) id else R.drawable.ic_launcher_foreground
                        }

                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(DeepTealContainer)
                                .border(2.dp, SoftGold.copy(alpha = 0.8f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = avatarResId),
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile?.fullName ?: "Sarah N. Okello",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "Verified User",
                                    tint = StatusGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Username / Member ID
                            val displayUserId = userProfile?.memberId ?: "HU7K4M92Q"
                            Text(
                                text = "@${displayUserId.lowercase()} • Member ID: $displayUserId",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepTeal
                            )

                            // Contact number (protected/masked format)
                            val rawPhone = userProfile?.phoneNumber ?: "+256 772 458921"
                            val maskedPhone = if (rawPhone.length >= 9) {
                                rawPhone.take(4) + " 7XX XXX " + rawPhone.takeLast(3)
                            } else rawPhone
                            Text(
                                text = maskedPhone,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                StatusBadge(text = "Verified Reviewer")
                                LocationBadge(district = "${userProfile?.district ?: "Kampala"}, Uganda")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Account Summary Stats
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmCreamSurface)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Wallet UGX",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = formatUgx(userProfile?.walletBalanceUgx ?: 0L),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(WarmCreamBorder)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Role",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = if (userProfile?.role == "ADMIN") "Administrator" else "Task Reviewer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (userProfile?.role == "ADMIN") SoftGold else DeepTealDark
                            )
                        }
                    }
                }
            }
        }

        // Package Information Card
        item {
            val visualTier = PackageVisualTier.fromPackageName(userProfile?.packageName)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("package_info_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, visualTier.borderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(visualTier.containerColor),
                                contentAlignment = Alignment.Center
                            ) {
                                PackageTierIcon(
                                    tier = visualTier,
                                    modifier = Modifier.size(26.dp),
                                    iconSize = 26.dp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = visualTier.displayTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = visualTier.darkColor
                                )
                                Text(
                                    text = "Package: ${visualTier.priceDisplay} • Task Reward: ${visualTier.taskRewardDisplay}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        PackageBadgeChip(tier = visualTier, isLarge = false)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Expiry & Duration Row
                    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
                    val expiryDateText = remember(userProfile?.packageExpiresAt) {
                        if (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0) {
                            dateFormat.format(Date(userProfile.packageExpiresAt))
                        } else {
                            "12 May 2025"
                        }
                    }
                    val isExpired = userProfile?.isPackageExpired == true ||
                        (userProfile?.packageExpiresAt != null && userProfile.packageExpiresAt > 0 && System.currentTimeMillis() >= userProfile.packageExpiresAt)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: ${if (isExpired) "EXPIRED" else "ACTIVE"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isExpired) WarningRedText else StatusGreenText
                        )
                        Text(
                            text = "Expires: $expiryDateText",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(visualTier.containerColor)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Paid Capital: ${formatUgx(userProfile?.paidCapitalUgx ?: visualTier.priceUgx)} • Working Capital (Non-withdrawable)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = visualTier.darkColor
                        )
                    }
                }
            }
        }

        // Change Uganda District Item
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowDistrictDialog(true) }
                    .testTag("change_district_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                .background(DeepTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditLocation,
                                contentDescription = "District",
                                tint = DeepTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Your Operational District",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${userProfile?.district ?: "Kampala Central"}, Uganda (Tap to change)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Edit",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Household Utensils Tasks Guidelines & FAQs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Help",
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Household Utensil Guidelines in Uganda",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Eligible Utensils: Cookware (sufuria, frying pans, kettles, pressure cookers), Traditional items (sigiri charcoal stoves, wooden mwiko, kinu mortar), Dining cutlery & tableware.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Inspection Standards: Ensure photos have clear daylight or kitchen lighting, show full utensil boundaries, and indicate material condition (stainless steel, cast iron, Teflon coating, enamel).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Payment & Rewards: Completed tasks are audited by HomeUtensils administrators. Verified rewards immediately credit your wallet balance in UGX.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // App Information
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "HomeUtensils Uganda v1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Mobile platform for household-utensil tasks & rewards",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    // District Picker Dialog
    if (showDistrictDialog) {
        Dialog(onDismissRequest = { onShowDistrictDialog(false) }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(18.dp)),
                color = WarmWhite,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Select District in Uganda",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    UgandaDistricts.forEach { district ->
                        val isSelected = userProfile?.district == district
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDistrictSelected(district)
                                    onShowDistrictDialog(false)
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onDistrictSelected(district)
                                    onShowDistrictDialog(false)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = DeepTeal,
                                    unselectedColor = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = district,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DeepTeal else TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
