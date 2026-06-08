package com.gibs.kadeesebi.presentation.common

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb

private const val ORNAMENT_AGSL = """
    uniform float2 iResolution;
    layout(color) uniform half4 iColor;

    half4 main(float2 fragCoord) {
        return half4(0.0);
    }
"""

fun Modifier.ornamentBackground(tint: Color, base: Color): Modifier =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.drawWithCache {
            val shader = RuntimeShader(ORNAMENT_AGSL)
            shader.setFloatUniform("iResolution", size.width, size.height)
            shader.setColorUniform("iColor", tint.toArgb())
            val brush = ShaderBrush(shader)
            onDrawBehind {
                drawRect(base)
                drawRect(brush)
            }
        }
    } else {
        this.background(base)
    }
