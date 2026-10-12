package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusCrownGold
import com.example.ui.theme.PlusPurple
import com.example.ui.theme.PlusPurpleBorder
import com.example.ui.theme.PlusPurpleContainer
import com.example.ui.theme.PlusPurpleDark
import com.example.ui.theme.PlusPurpleText
import com.example.ui.theme.StandardBlue
import com.example.ui.theme.StandardBlueBorder
import com.example.ui.theme.StandardBlueContainer
import com.example.ui.theme.StandardBlueDark
import com.example.ui.theme.StandardBlueText
import com.example.ui.theme.StarterGreen
import com.example.ui.theme.StarterGreenBorder
import com.example.ui.theme.StarterGreenContainer
import com.example.ui.theme.StarterGreenDark
import com.example.ui.theme.StarterGreenText
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText

enum class PackageVisualTier(
    val tierKey: String,
    val displayTitle: String,
    val fullPackageTitle: String,
    val primaryColor: Color,
    val darkColor: Color,
    val containerColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val priceUgx: Long,
    val priceDisplay: String,
    val taskRewardUgx: Long,
    val taskRewardDisplay: String,
    val subtitleTagline: String
) {
    STARTER(
        tierKey = "STARTER",
        displayTitle = "STARTER",
        fullPackageTitle = "STARTER PACKAGE",
        primaryColor = StarterGreen,
        darkColor = StarterGreenDark,
        containerColor = StarterGreenContainer,
        borderColor = StarterGreenBorder,
        textColor = StarterGreenText,
        priceUgx = 5000L,
        priceDisplay = "UGX 5,000",
        taskRewardUgx = 500L,
        taskRewardDisplay = "UGX 500 per correct task",
        subtitleTagline = "Starter Member Package • Clean Green Activation"
    ),
    STANDARD(
        tierKey = "STANDARD",
        displayTitle = "STANDARD",
        fullPackageTitle = "STANDARD PACKAGE",
        primaryColor = StandardBlue,
        darkColor = StandardBlueDark,
        containerColor = StandardBlueContainer,
        borderColor = StandardBlueBorder,
        textColor = StandardBlueText,
        priceUgx = 10000L,
        priceDisplay = "UGX 10,000",
        taskRewardUgx = 500L,
        taskRewardDisplay = "UGX 500 per correct task",
        subtitleTagline = "Standard Member Package • Daily Utensil Reviews"
    ),
    PLUS(
        tierKey = "PLUS",
        displayTitle = "PLUS",
        fullPackageTitle = "PLUS PACKAGE",
        primaryColor = PlusPurple,
        darkColor = PlusPurpleDark,
        containerColor = PlusPurpleContainer,
        borderColor = PlusPurpleBorder,
        textColor = PlusPurpleText,
        priceUgx = 20000L,
        priceDisplay = "UGX 20,000",
        taskRewardUgx = 1000L,
        taskRewardDisplay = "UGX 1,000 per correct task",
        subtitleTagline = "Plus Premium Package • Crown Tier Member"
    );

    companion object {
        fun fromPackageName(name: String?): PackageVisualTier {
            val upper = name?.trim()?.uppercase() ?: ""
            return when {
                upper.contains("PLUS") -> PLUS
                upper.contains("STARTER") -> STARTER
                else -> STANDARD
            }
        }
    }
}

/**
 * Custom Canvas-rendered Royal Crown / King Icon for the PLUS package tier.
 * Visually distinctive royal gold crown with jewels on peaks and baseband.
 */
@Composable
fun RoyalCrownIcon(
    modifier: Modifier = Modifier,
    crownColor: Color = PlusCrownGold,
    baseColor: Color = PlusPurpleDark
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 5-peak royal crown body
        val path = Path().apply {
            moveTo(w * 0.10f, h * 0.85f) // bottom left
            lineTo(w * 0.05f, h * 0.35f) // left peak
            lineTo(w * 0.28f, h * 0.52f) // left inner valley
            lineTo(w * 0.50f, h * 0.18f) // center high peak
            lineTo(w * 0.72f, h * 0.52f) // right inner valley
            lineTo(w * 0.95f, h * 0.35f) // right peak
            lineTo(w * 0.90f, h * 0.85f) // bottom right
            close()
        }
        drawPath(path, color = crownColor, style = Fill)
        drawPath(path, color = baseColor.copy(alpha = 0.4f), style = Stroke(width = w * 0.04f))

        // Jewels on the 3 prominent peaks
        val jewelRadius = w * 0.065f
        drawCircle(color = Color.White, radius = jewelRadius, center = Offset(w * 0.05f, h * 0.35f))
        drawCircle(color = Color(0xFFFFEB3B), radius = jewelRadius * 1.3f, center = Offset(w * 0.50f, h * 0.18f))
        drawCircle(color = Color.White, radius = jewelRadius * 0.6f, center = Offset(w * 0.50f, h * 0.18f))
        drawCircle(color = Color.White, radius = jewelRadius, center = Offset(w * 0.95f, h * 0.35f))

        // Crown Headband
        val bandPath = Path().apply {
            moveTo(w * 0.08f, h * 0.82f)
            lineTo(w * 0.92f, h * 0.82f)
            lineTo(w * 0.90f, h * 0.94f)
            lineTo(w * 0.10f, h * 0.94f)
            close()
        }
        drawPath(bandPath, color = baseColor, style = Fill)

        // Jewels on headband
        drawCircle(color = PlusCrownGold, radius = jewelRadius * 0.7f, center = Offset(w * 0.28f, h * 0.88f))
        drawCircle(color = Color.White, radius = jewelRadius * 0.8f, center = Offset(w * 0.50f, h * 0.88f))
        drawCircle(color = PlusCrownGold, radius = jewelRadius * 0.7f, center = Offset(w * 0.72f, h * 0.88f))
    }
}

@Composable
fun PackageTierIcon(
    tier: PackageVisualTier,
    modifier: Modifier = Modifier,
    iconSize: Dp = 22.dp,
    tintOverride: Color? = null
) {
    when (tier) {
        PackageVisualTier.STARTER -> {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Starter Icon",
                tint = tintOverride ?: tier.primaryColor,
                modifier = modifier.size(iconSize)
            )
        }
        PackageVisualTier.STANDARD -> {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Standard Star Icon",
                tint = tintOverride ?: tier.primaryColor,
                modifier = modifier.size(iconSize)
            )
        }
        PackageVisualTier.PLUS -> {
            RoyalCrownIcon(
                modifier = modifier.size(iconSize),
                crownColor = PlusCrownGold,
                baseColor = tier.darkColor
            )
        }
    }
}

@Composable
fun PackageBadgeChip(
    tier: PackageVisualTier,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(if (isLarge) 10.dp else 6.dp))
            .background(tier.containerColor)
            .border(1.dp, tier.borderColor, RoundedCornerShape(if (isLarge) 10.dp else 6.dp))
            .padding(
                horizontal = if (isLarge) 10.dp else 6.dp,
                vertical = if (isLarge) 4.dp else 2.dp
            )
            .testTag("package_badge_${tier.tierKey.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (isLarge) 6.dp else 4.dp)
        ) {
            PackageTierIcon(
                tier = tier,
                modifier = Modifier.size(if (isLarge) 16.dp else 12.dp),
                iconSize = if (isLarge) 16.dp else 12.dp
            )
            Text(
                text = tier.displayTitle,
                fontSize = if (isLarge) 11.sp else 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = tier.darkColor
            )
        }
    }
}

/**
 * Clean Package Badge / Header shown near the top of the dashboard.
 * Prominently displays:
 * - STARTER (Green visual theme, simple Starter-related icon, UGX 5,000, UGX 500/task)
 * - STANDARD (Blue visual theme, standard/star icon, UGX 10,000, UGX 500/task)
 * - PLUS (Purple visual theme, Crown/King icon, UGX 20,000, UGX 1,000/task)
 */
@Composable
fun DashboardPackageTopHeader(
    tier: PackageVisualTier,
    isExpired: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_top_package_header"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpired) WarningRedContainer else tier.containerColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isExpired) WarningRed else tier.borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isExpired) WarningRedText else tier.primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isExpired) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Expired",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (tier == PackageVisualTier.PLUS) {
                        RoyalCrownIcon(
                            modifier = Modifier.size(24.dp),
                            crownColor = Color(0xFFFFD54F),
                            baseColor = Color.White
                        )
                    } else if (tier == PackageVisualTier.STARTER) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Starter",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Standard",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = tier.displayTitle,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isExpired) WarningRedText else tier.darkColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PACKAGE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isExpired) WarningRedText else tier.textColor.copy(alpha = 0.85f)
                        )
                    }
                    Text(
                        text = "Price: ${tier.priceDisplay} • Task Reward: ${tier.taskRewardDisplay}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isExpired) WarningRedText else tier.darkColor,
                        lineHeight = 14.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isExpired) WarningRed else tier.darkColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isExpired) "EXPIRED" else "ACTIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}
