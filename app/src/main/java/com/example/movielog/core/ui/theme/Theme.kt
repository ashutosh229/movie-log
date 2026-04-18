package com.example.movielog.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B4A),
    onPrimary = Color(0xFF2B0B05),
    primaryContainer = Color(0xFF4A170D),
    onPrimaryContainer = Color(0xFFFFDBD3),
    secondary = Color(0xFFFFC857),
    onSecondary = Color(0xFF2B1800),
    secondaryContainer = Color(0xFF443000),
    onSecondaryContainer = Color(0xFFFFE9BC),
    tertiary = Color(0xFF78D6C6),
    onTertiary = Color(0xFF03201B),
    tertiaryContainer = Color(0xFF0D3A33),
    onTertiaryContainer = Color(0xFFA6F4E6),
    background = Color(0xFF0B1018),
    onBackground = Color(0xFFF1F4FA),
    surface = Color(0xFF141B24),
    onSurface = Color(0xFFF1F4FA),
    surfaceVariant = Color(0xFF212B37),
    onSurfaceVariant = Color(0xFFB8C4D3),
    outline = Color(0xFF607080),
    outlineVariant = Color(0xFF33414E),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF3A0905),
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB54528),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBD3),
    onPrimaryContainer = Color(0xFF3B0E05),
    secondary = Color(0xFF8B6500),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE9BC),
    onSecondaryContainer = Color(0xFF2A1C00),
    tertiary = Color(0xFF006B5B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFA6F4E6),
    onTertiaryContainer = Color(0xFF00201A),
    background = Color(0xFFF5F1EA),
    onBackground = Color(0xFF1D1B1A),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1D1B1A),
    surfaceVariant = Color(0xFFE9E1D8),
    onSurfaceVariant = Color(0xFF4F463F),
    outline = Color(0xFF81756C),
    outlineVariant = Color(0xFFD3C6BB),
    error = Color(0xFFB3261E),
    onError = Color.White,
    scrim = Color(0xFF000000)
)

private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MovieLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
