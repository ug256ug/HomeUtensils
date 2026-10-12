package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = DeepTealContainer,
    onPrimaryContainer = DeepTealOnContainer,
    secondary = SoftGoldDark,
    onSecondary = Color.White,
    secondaryContainer = SoftGoldContainer,
    onSecondaryContainer = SoftGoldOnContainer,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    tertiaryContainer = StatusGreenContainer,
    onTertiaryContainer = StatusGreenText,
    error = WarningRed,
    onError = Color.White,
    errorContainer = WarningRedContainer,
    onErrorContainer = WarningRedText,
    background = WarmCreamBackground,
    onBackground = TextPrimary,
    surface = WarmWhite,
    onSurface = TextPrimary,
    surfaceVariant = WarmCreamSurface,
    onSurfaceVariant = TextSecondary,
    outline = WarmCreamBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = DeepTealLight,
    onPrimary = Color.White,
    primaryContainer = DeepTealDark,
    onPrimaryContainer = DeepTealContainer,
    secondary = SoftGold,
    onSecondary = Color.Black,
    secondaryContainer = SoftGoldDark,
    onSecondaryContainer = SoftGoldContainer,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    tertiaryContainer = StatusGreenText,
    onTertiaryContainer = StatusGreenContainer,
    error = WarningRed,
    onError = Color.White,
    errorContainer = WarningRedText,
    onErrorContainer = WarningRedContainer,
    background = Color(0xFF121A18),
    onBackground = Color(0xFFF1F5F4),
    surface = Color(0xFF1B2422),
    onSurface = Color(0xFFF1F5F4),
    surfaceVariant = Color(0xFF263330),
    onSurfaceVariant = Color(0xFFCAD5D2),
    outline = Color(0xFF3B4D49)
)

@Composable
fun HomeUtensilsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
