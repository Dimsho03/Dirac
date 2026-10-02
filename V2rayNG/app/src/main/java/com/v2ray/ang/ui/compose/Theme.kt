package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val LightColor = lightColorScheme(
    primary = Color(0xFF000000), // Black
    onPrimary = Color(0xFFFFFFFF), // White
    primaryContainer = Color(0xFFE0E0E0), // Light Gray
    onPrimaryContainer = Color(0xFF000000), // Black
    secondary = Color(0xFF34393C), // Neutral graphite
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFE8EAEB), // Soft neutral
    onSecondaryContainer = Color(0xFF17191A), // Near black
    tertiary = Color(0xFF6F8B76), // Dirac sage
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFDDE6DF), // Soft sage
    onTertiaryContainer = Color(0xFF18201A), // Deep sage
    error = Color(0xFFBA1A1A), // Red
    errorContainer = Color(0xFFFFDAD6), // Light Red
    onError = Color(0xFFFFFFFF), // White
    onErrorContainer = Color(0xFF410002), // Dark Red
    background = Color(0xFFF5F6F6), // Soft white
    onBackground = Color(0xFF101213), // Near black
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF101213), // Near black
    surfaceVariant = Color(0xFFE9EBEC), // Neutral gray
    onSurfaceVariant = Color(0xFF62686B), // Muted gray
    outline = Color(0xFF9CA1A4), // Medium gray
    outlineVariant = Color(0xFFD9DCDE), // Light gray
    inverseSurface = Color(0xFF313033), // Dark Gray
    inverseOnSurface = Color(0xFFF4EFF4), // Very Light Gray
    inversePrimary = Color(0xFFC0C0C0), // Silver Gray
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFF000000), // Black
    surfaceContainerLowest = Color(0xFFFFFFFF), // White
    surfaceContainerLow = Color(0xFFFAFAFA), // Very light gray
    surfaceContainer = Color(0xFFF3F4F4), // Light gray
    surfaceContainerHigh = Color(0xFFEDEFEF), // Light gray
    surfaceContainerHighest = Color(0xFFE6E8E9), // Light gray
)

private val DarkColor = darkColorScheme(
    primary = Color(0xFFF1F3F3), // Off white
    onPrimary = Color(0xFF111314), // Near black
    primaryContainer = Color(0xFF24282A), // Graphite
    onPrimaryContainer = Color(0xFFF1F3F3), // Off white
    secondary = Color(0xFFB9BFC2), // Neutral silver
    onSecondary = Color(0xFF17191A), // Near black
    secondaryContainer = Color(0xFF292D2F), // Graphite
    onSecondaryContainer = Color(0xFFE8EAEB), // Soft neutral
    tertiary = Color(0xFF8FA997), // Dirac sage
    onTertiary = Color(0xFF152019), // Deep sage
    tertiaryContainer = Color(0xFF29382E), // Dark sage
    onTertiaryContainer = Color(0xFFDCE7DF), // Pale sage
    error = Color(0xFFFFB4AB), // Light Red
    errorContainer = Color(0xFF93000A), // Dark Red
    onError = Color(0xFF690005), // Deep Red
    onErrorContainer = Color(0xFFFFDAD6), // Light Red
    background = Color(0xFF0B0D0E), // Dirac black
    onBackground = Color(0xFFEDEFEF), // Off white
    surface = Color(0xFF111315), // Raised black
    onSurface = Color(0xFFEDEFEF), // Off white
    surfaceVariant = Color(0xFF25292B), // Graphite
    onSurfaceVariant = Color(0xFFA9AFB2), // Muted silver
    outline = Color(0xFF555C60), // Neutral outline
    outlineVariant = Color(0xFF303538), // Subtle outline
    inverseSurface = Color(0xFFEDEFEF), // Off white
    inverseOnSurface = Color(0xFF111315), // Near black
    inversePrimary = Color(0xFF111315), // Near black
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFFB9BFC2), // Neutral silver
    surfaceContainerLowest = Color(0xFF080A0B), // Deep black
    surfaceContainerLow = Color(0xFF101214), // Dark surface
    surfaceContainer = Color(0xFF15181A), // Dark surface
    surfaceContainerHigh = Color(0xFF1B1F21), // Raised surface
    surfaceContainerHighest = Color(0xFF24292B), // Highest surface
)

// Semantic Colors
val colorPing = Color(0xFF6F8B76) // Dirac sage
val colorPingRed = Color(0xFFD96A6A) // Muted red
val colorConfigType = Color(0xFF8E969A) // Neutral metadata
val colorFabActive = Color(0xFF6F8B76) // Connected sage
val colorFabInactiveLight = Color(0xFF34393C) // Graphite
val colorFabInactiveDark = Color(0xFFB9BFC2) // Silver
val dividerColorLight = Color(0xFFE2E5E6) // Light gray
val dividerColorDark = Color(0xFF2D3235) // Dark gray

// Toast Colors 85%
val toastNormalBgLight = Color(0xD9353A3E) // Dark Gray
val toastNormalBgDark = Color(0xD94A4F54) // Darker Gray
val toastSuccessBg = Color(0xD9388E3C) // Green
val toastErrorBg = Color(0xD9D50000) // Red
val toastInfoBg = Color(0xD93F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        "2" -> true
        else -> isSystemInDarkTheme()
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor by ThemeManager.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
