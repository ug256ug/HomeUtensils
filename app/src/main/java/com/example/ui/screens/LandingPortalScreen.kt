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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldDark
import com.example.ui.theme.SoftGoldOnContainer
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite

@Composable
fun LandingPortalScreen(
    onSelectMemberPortal: () -> Unit,
    onSelectAgentPortal: () -> Unit,
    onSelectAdminPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("landing_portal_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Branding Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("landing_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Kitchen,
                                contentDescription = "HomeUtensils Logo",
                                tint = SoftGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "HomeUtensils",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTealDark
                            )
                            Text(
                                text = "Household Cookware & Utensil Network",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Welcome to the HomeUtensils prototype. Please select your login option to test the portal dashboards.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Section Title: Three Options
        item {
            Text(
                text = "Choose Login Option",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 1. Member Login Option
        item {
            PortalOptionCard(
                number = "1",
                title = "Member Login",
                subtitle = "Review household cookware items (cups, plates, bowls, spoons, basins) and earn UGX 500 per correct answer.",
                testCredentialsNote = "Phone: 0700000000 • Pass: Member123!",
                badgeText = "Member Portal",
                badgeColor = StatusGreenContainer,
                badgeTextColor = StatusGreenText,
                icon = Icons.Default.Person,
                iconBackground = DeepTealContainer,
                iconTint = DeepTeal,
                buttonText = "Open Member Login",
                onClick = onSelectMemberPortal,
                testTag = "portal_card_member"
            )
        }

        // 2. Agent Login Option
        item {
            PortalOptionCard(
                number = "2",
                title = "Agent Login",
                subtitle = "Regional field agent dashboard for Central, Eastern, Northern, and Western Uganda territory verification.",
                testCredentialsNote = "Agent ID: AGT-CENTRAL-001 • Pass: Agent123!",
                badgeText = "Agent Portal",
                badgeColor = SoftGoldContainer,
                badgeTextColor = SoftGoldOnContainer,
                icon = Icons.Default.SupportAgent,
                iconBackground = SoftGoldContainer,
                iconTint = SoftGoldDark,
                buttonText = "Open Agent Login",
                onClick = onSelectAgentPortal,
                testTag = "portal_card_agent"
            )
        }

        // 3. Admin Login Option
        item {
            PortalOptionCard(
                number = "3",
                title = "Admin Login",
                subtitle = "System administrator console for task moderation, regional user oversight, and platform metrics.",
                testCredentialsNote = "Admin ID: ADMIN001 • Pass: Admin123!",
                badgeText = "Admin Portal",
                badgeColor = DeepTealContainer,
                badgeTextColor = DeepTeal,
                icon = Icons.Default.AdminPanelSettings,
                iconBackground = DeepTealContainer,
                iconTint = DeepTealDark,
                buttonText = "Open Admin Login",
                onClick = onSelectAdminPortal,
                testTag = "portal_card_admin"
            )
        }

        // Demo Prototype Reference Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Prototype Test Accounts Reference",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DeepTealDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Member: 0700000000 / Member123! (ID: HU7K4M92Q)\n• Agent: AGT-CENTRAL-001 / Agent123!\n• Admin: ADMIN001 / Admin123!",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Each login screen features a 1-tap Autofill button to quickly test each portal.",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun PortalOptionCard(
    number: String,
    title: String,
    subtitle: String,
    testCredentialsNote: String,
    badgeText: String,
    badgeColor: Color,
    badgeTextColor: Color,
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    buttonText: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepTealDark
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Test credentials chip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmCreamSurface)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Demo: $testCredentialsNote",
                    fontSize = 11.sp,
                    color = DeepTealDark,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
