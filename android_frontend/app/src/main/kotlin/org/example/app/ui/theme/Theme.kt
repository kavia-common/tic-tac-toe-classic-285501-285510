package org.example.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Ocean Professional palette
private val Primary = Color(0xFF2563EB) // blue
private val Secondary = Color(0xFFF59E0B) // amber
private val Error = Color(0xFFEF4444)
private val Background = Color(0xFFF9FAFB)
private val Surface = Color(0xFFFFFFFF)
private val Text = Color(0xFF111827)

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    error = Error,
    onError = Color.White,
    background = Background,
    onBackground = Text,
    surface = Surface,
    onSurface = Text
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    error = Error,
    onError = Color.White,
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFE5E7EB)
)

/**
 * PUBLIC_INTERFACE
 * OceanTheme applies the Ocean Professional color scheme and default Material3 typography and shapes.
 */
@Composable
fun OceanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
