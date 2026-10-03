package com.v2ray.ang.ui.dirac

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val DiracSage = Color(0xFF8FA99A)
internal val DiracBackground = Color(0xFF0B0D0E)
internal val DiracSurface = Color(0xFF121516)
internal val DiracSurfaceRaised = Color(0xFF191D1E)
internal val DiracBorder = Color(0xFF2A2F31)
internal val DiracMuted = Color(0xFF9AA2A0)
internal val DiracText = Color(0xFFF2F4F3)

private val DiracColors = darkColorScheme(
    primary = DiracText,
    onPrimary = Color(0xFF101213),
    secondary = Color(0xFFB9C0BE),
    onSecondary = Color(0xFF151819),
    tertiary = DiracSage,
    onTertiary = Color(0xFF0D1510),
    background = DiracBackground,
    onBackground = DiracText,
    surface = DiracSurface,
    onSurface = DiracText,
    surfaceVariant = DiracSurfaceRaised,
    onSurfaceVariant = DiracMuted,
    outline = DiracBorder,
    error = Color(0xFFE4A3A3),
)

private val DiracShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
)

@Composable
fun DiracTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DiracColors,
        typography = MaterialTheme.typography,
        shapes = DiracShapes,
        content = content,
    )
}
