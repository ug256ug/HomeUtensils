package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UtensilTaskEntity
import com.example.ui.components.RewardBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.UtensilThumbnail
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.DeepTealContainer
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SoftGoldContainer
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmWhite

@Composable
fun TaskSubmissionDialog(
    task: UtensilTaskEntity,
    selectedAnswer: String,
    userNotes: String,
    isSubmitting: Boolean,
    onAnswerSelected: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val options = task.optionsString.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp))
                .background(WarmWhite)
                .testTag("task_submission_dialog"),
            color = WarmWhite,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(text = task.category)
                            Spacer(modifier = Modifier.width(8.dp))
                            RewardBadge(amountUgx = task.rewardUgx)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepTealDark
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_task_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Utensil Image Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, WarmCreamBorder, RoundedCornerShape(16.dp))
                ) {
                    UtensilThumbnail(
                        imageResName = task.imageResName,
                        modifier = Modifier.matchParentSize(),
                        contentDescription = task.title
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Time estimate & instructions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmCreamSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = DeepTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Estimated time: ${task.estimatedMinutes} mins • Ugandan Household Review",
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepTealDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = task.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Question Prompt
                Text(
                    text = task.questionPrompt,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepTealDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Options list
                options.forEach { option ->
                    val isSelected = selectedAnswer == option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) SoftGoldContainer else WarmCreamBackground)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) SoftGold else WarmCreamBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onAnswerSelected(option) }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                            .testTag("option_${option.take(10)}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Select",
                            tint = if (isSelected) DeepTeal else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) DeepTealDark else TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional notes
                OutlinedTextField(
                    value = userNotes,
                    onValueChange = onNotesChanged,
                    label = { Text("Utensil Observations / Notes (Optional)") },
                    placeholder = { Text("e.g., Handle is heat-resistant, verified clean surface") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_notes_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepTeal,
                        unfocusedBorderColor = WarmCreamBorder,
                        focusedLabelColor = DeepTeal
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("cancel_task_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarmCreamSurface,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cancel", fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = onSubmit,
                        enabled = selectedAnswer.isNotEmpty() && !isSubmitting,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("submit_task_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepTeal,
                            contentColor = WarmWhite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = WarmWhite,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Submit for UGX ${task.rewardUgx}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
