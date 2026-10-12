package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.SoftGoldOnContainer
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusGreenText
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedText
import java.text.NumberFormat
import java.util.Locale

fun formatUgx(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "UGX " + formatter.format(amount)
}

@Composable
fun StatusBadge(
    text: String = "Active",
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(StatusGreenContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("status_badge_active")
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(StatusGreen)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = StatusGreenText,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun WarningBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(WarningRedContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("status_badge_warning")
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            tint = WarningRed,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = WarningRedText,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun RewardBadge(
    amountUgx: Long,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SoftGoldContainer)
            .border(1.dp, SoftGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("reward_badge")
    ) {
        Text(
            text = "+ " + formatUgx(amountUgx),
            style = MaterialTheme.typography.labelMedium,
            color = SoftGoldOnContainer,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LocationBadge(
    district: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DeepTealContainer.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = DeepTeal,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$district, Uganda",
            style = MaterialTheme.typography.labelSmall,
            color = DeepTeal,
            fontWeight = FontWeight.Medium
        )
    }
}
