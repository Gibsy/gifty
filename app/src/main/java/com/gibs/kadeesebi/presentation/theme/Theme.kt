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
    primary = Color(0xFF9CCBF0),
    onPrimary = Color(0xFF00344F),
    primaryContainer = Color(0xFF1A4A6E),
    onPrimaryContainer = Color(0xFFCEE5FF),
    inversePrimary = SteppeBlue,
    secondary = Color(0xFFE6C36E),
    onSecondary = Color(0xFF3D2F00),
    secondaryContainer = Color(0xFF574500),
    onSecondaryContainer = Color(0xFFFFE08C),
    tertiary = Color(0xFFB4CCB8),
    onTertiary = Color(0xFF20352A),
    tertiaryContainer = Color(0xFF364B3F),
    onTertiaryContainer = Color(0xFFD0E8D5),
    background = Color(0xFF14130F),
    onBackground = Color(0xFFE9E2D4),
    surface = Color(0xFF14130F),
    onSurface = Color(0xFFE9E2D4),
    surfaceVariant = Color(0xFF49453B),
    onSurfaceVariant = Color(0xFFCBC5B5),
    surfaceTint = Color(0xFF9CCBF0),
    inverseSurface = Color(0xFFE9E2D4),
    inverseOnSurface = Color(0xFF323028),
    outline = Color(0xFF948F80),
    outlineVariant = Color(0xFF49453B),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surfaceDim = Color(0xFF14130F),
    surfaceBright = Color(0xFF3B3930),
    surfaceContainerLowest = Color(0xFF0E0D0A),
    surfaceContainerLow = Color(0xFF1C1B16),
    surfaceContainer = Color(0xFF201F1A),
    surfaceContainerHigh = Color(0xFF2B2924),
    surfaceContainerHighest = Color(0xFF36342E),
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
