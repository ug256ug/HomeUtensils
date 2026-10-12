package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.repository.TaskEvaluationResult
import com.example.ui.components.RewardBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.UtensilThumbnail
import com.example.ui.components.WarningBadge
import com.example.ui.components.PackageBadgeChip
import com.example.ui.components.PackageVisualTier
import com.example.ui.components.formatUgx
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TasksScreen(
    tasks: List<UtensilTaskEntity>,
    submissions: List<TaskSubmissionEntity>,
    userProfile: UserProfileEntity?,
    selectedCategory: String,
    tasksTab: String,
    isSubmitting: Boolean,
    lastTaskResult: TaskEvaluationResult?,
    onCategorySelected: (String) -> Unit,
    onTasksTabSelected: (String) -> Unit,
    onAnswerSelected: (UtensilTaskEntity, String) -> Unit,
    onDismissTaskResult: () -> Unit,
    onReloadDemoTasks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Cups", "Plates", "Bowls", "Spoons", "Basins", "Kettles", "Pans")

    // Separate active available tasks from completed tasks
    val availableTasks = tasks.filter { it.status == "ACTIVE" }.filter {
        selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar & Mode Tabs (Available vs Completed)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val visualTier = PackageVisualTier.fromPackageName(userProfile?.packageName)
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Household Utensil Tasks",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )
                                PackageBadgeChip(tier = visualTier, isLarge = false)
                            }
                            Text(
                                text = "Earn ${visualTier.taskRewardDisplay} (${visualTier.displayTitle} Package)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Compact Wallet Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGoldContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = formatUgx(userProfile?.walletBalanceUgx ?: 18500L),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGoldOnContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Segmented Tabs: Available Tasks vs Completed History
                    TabRow(
                        selectedTabIndex = if (tasksTab == "AVAILABLE") 0 else 1,
                        containerColor = WarmCreamSurface,
                        contentColor = DeepTeal,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[if (tasksTab == "AVAILABLE") 0 else 1]),
                                color = DeepTeal,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Tab(
                            selected = tasksTab == "AVAILABLE",
                            onClick = { onTasksTabSelected("AVAILABLE") },
                            text = {
                                Text(
                                    text = "Available (${tasks.count { it.status == "ACTIVE" }})",
                                    fontWeight = if (tasksTab == "AVAILABLE") FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_available_tasks")
                        )

                        Tab(
                            selected = tasksTab == "COMPLETED",
                            onClick = { onTasksTabSelected("COMPLETED") },
                            text = {
                                Text(
                                    text = "Completed (${submissions.size})",
                                    fontWeight = if (tasksTab == "COMPLETED") FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_completed_tasks")
                        )
                    }
                }
            }

            // Category Filter Chips (Available only when viewing Available tab)
            if (tasksTab == "AVAILABLE") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("category_filter_row")
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelected(category) },
                            label = {
                                Text(
                                    text = category,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = WarmWhite,
                                labelColor = TextSecondary,
                                selectedContainerColor = DeepTeal,
                                selectedLabelColor = WarmWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = WarmCreamBorder,
                                selectedBorderColor = DeepTeal
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("chip_$category")
                        )
                    }
                }
            }

            // Main Content: List of Available or Completed Tasks
            if (tasksTab == "AVAILABLE") {
                if (availableTasks.isEmpty()) {
                    // Empty State for Available Tasks
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(StatusGreenContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "All done",
                                        tint = StatusGreen,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "All Utensil Tasks Completed!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTealDark
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "You have answered all available sample household utensil tasks. Check your Completed tab to review your earned rewards, or refresh the sample tasks to practice again.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = onReloadDemoTasks,
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("reload_demo_tasks_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Refresh Sample Tasks", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("available_tasks_list"),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(availableTasks, key = { it.id }) { task ->
                            UtensilYesNoTaskCard(
                                task = task,
                                isSubmitting = isSubmitting,
                                onAnswer = { answer -> onAnswerSelected(task, answer) }
                            )
                        }
                    }
                }
            } else {
                // Completed Tasks History
                if (submissions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No completed tasks yet. Answer tasks in the Available tab to start earning!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("completed_tasks_list"),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(submissions, key = { it.id }) { submission ->
                            CompletedTaskHistoryCard(submission = submission)
                        }
                    }
                }
            }
        }

        // Result Dialog: Correct or Incorrect Feedback
        lastTaskResult?.let { result ->
            TaskEvaluationResultDialog(
                result = result,
                onDismiss = onDismissTaskResult
            )
        }
    }
}

/**
 * Mobile-friendly card showing:
 * - One utensil image
 * - Category & Title
 * - Reward (+UGX 500)
 * - One simple YES/NO question
 * - YES and NO answer buttons
 */
@Composable
fun UtensilYesNoTaskCard(
    task: UtensilTaskEntity,
    isSubmitting: Boolean,
    onAnswer: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Category Badge & Reward Pill (+UGX 500)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DeepTealContainer)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = task.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                }

                // Reward Tag: +UGX 500
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SoftGoldContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Reward",
                            tint = SoftGoldOnContainer,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+UGX 500",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftGoldOnContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Task Title
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepTealDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Utensil Image (One image per task)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WarmCreamSurface)
            ) {
                UtensilThumbnail(
                    imageResName = task.imageResName,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = task.title
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // One simple YES/NO question box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, SoftGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Question",
                        tint = DeepTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.questionPrompt,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // YES and NO Answer Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // YES Button
                Button(
                    onClick = { onAnswer("YES") },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("task_yes_${task.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepTeal,
                        contentColor = WarmWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Yes",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "YES",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                // NO Button
                Button(
                    onClick = { onAnswer("NO") },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("task_no_${task.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC53030), // Warning / Coral Red
                        contentColor = WarmWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "No",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NO",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Result Dialog showing whether the answer was correct or incorrect,
 * along with the awarded amount (UGX 500 or UGX 0).
 */
@Composable
fun TaskEvaluationResultDialog(
    result: TaskEvaluationResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (result.isCorrect) StatusGreenContainer else WarningRedContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (result.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = if (result.isCorrect) "Correct" else "Incorrect",
                        tint = if (result.isCorrect) StatusGreenText else WarningRed,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (result.isCorrect) "Correct Answer!" else "Incorrect Answer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (result.isCorrect) StatusGreenText else WarningRedText
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = result.taskTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!result.isCorrect) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarningRedContainer)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Correct Answer Highlighted:",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = WarningRedText
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DeepTeal)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = result.correctAnswer,
                                    color = WarmWhite,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Answer: ${result.userAnswer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Expected: ${result.correctAnswer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = WarmCreamBorder)
                Spacer(modifier = Modifier.height(10.dp))

                // Award Announcement Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (result.isCorrect) StatusGreenContainer else WarmCreamSurface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (result.isCorrect) "Reward Earned:" else "Reward Awarded:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )

                        Text(
                            text = if (result.isCorrect) "+UGX 500" else "UGX 0",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (result.isCorrect) StatusGreenText else TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (result.isCorrect) {
                        "UGX 500 has been automatically credited to your Withdrawable Balance without requiring admin approval."
                    } else {
                        "Wrong task answer earns UGX 0. The correct answer (${result.correctAnswer}) is highlighted above."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (result.isCorrect) DeepTeal else DeepTealDark
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("dialog_continue_button")
            ) {
                Text("Continue", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = WarmWhite,
        shape = RoundedCornerShape(16.dp)
    )
}

/**
 * Card for completed task history
 */
@Composable
fun CompletedTaskHistoryCard(submission: TaskSubmissionEntity) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(submission.submittedAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Utensil Thumbnail
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmCreamSurface)
            ) {
                UtensilThumbnail(
                    imageResName = submission.imageResName.ifBlank { "hero_utensils_banner_1790683214191" },
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = submission.taskTitle
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = submission.taskTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Answer: ${submission.selectedAnswer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Result Badge
                if (submission.isCorrect || submission.rewardUgx > 0) {
                    StatusBadge(text = "Correct (+UGX ${submission.rewardUgx})")
                } else {
                    WarningBadge(text = "Incorrect (UGX 0)")
                }
            }
        }
    }
}
