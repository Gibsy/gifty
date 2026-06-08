package com.gibs.kadeesebi.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val SteppeBlue = Color(0xFF1B6CA8)
val SteppeGold = Color(0xFFE0A82E)
val Felt = Color(0xFFFBF6EC)

private val LightColors = lightColorScheme(
    primary = SteppeBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2E4F3),
    onPrimaryContainer = Color(0xFF09314F),
    secondary = Color(0xFF9C7414),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBE3B6),
    onSecondaryContainer = Color(0xFF402D00),
    tertiary = Color(0xFF4F6354),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD2E8D5),
    onTertiaryContainer = Color(0xFF0D1F13),
    background = Felt,
    onBackground = Color(0xFF1D1B16),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1D1B16),
    surfaceVariant = Color(0xFFEDE6D6),
    onSurfaceVariant = Color(0xFF4D4639),
    surfaceTint = SteppeBlue,
    outline = Color(0xFF7F7767),
    outlineVariant = Color(0xFFD0C8B6),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8FC5EE),
    onPrimary = Color(0xFF08304F),
    primaryContainer = Color(0xFF13496F),
    onPrimaryContainer = Color(0xFFD2E4F3),
    secondary = Color(0xFFEBC56B),
    onSecondary = Color(0xFF3E2E00),
    secondaryContainer = Color(0xFF59440E),
    onSecondaryContainer = Color(0xFFFBE3B6),
    tertiary = Color(0xFFB6CCBA),
    onTertiary = Color(0xFF223528),
    tertiaryContainer = Color(0xFF384B3D),
    onTertiaryContainer = Color(0xFFD2E8D5),
    background = Color(0xFF15130E),
    onBackground = Color(0xFFE9E2D4),
    surface = Color(0xFF1E1B15),
    onSurface = Color(0xFFE9E2D4),
    surfaceVariant = Color(0xFF4D4639),
    onSurfaceVariant = Color(0xFFD0C8B6),
    surfaceTint = Color(0xFF8FC5EE),
    outline = Color(0xFF999080),
    outlineVariant = Color(0xFF4D4639),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
)

private val KadeShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun KadeEsebiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = KadeShapes,
        content = content,
    )
}
