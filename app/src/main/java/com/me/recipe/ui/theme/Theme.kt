package com.me.recipe.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    primaryContainer = Grey1,
    onPrimary = Black2,
    secondary = Color.White,
    tertiary = Grey3,
    secondaryContainer = Teal300,
    onSecondary = Black2,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Color.White,
    onBackground = Color.Black,
    surface = Grey1,
    surfaceVariant = Grey2,
    onSurface = Black2,
    onSurfaceVariant = Black1,
    outline = Grey3,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue700,
    primaryContainer = Black1,
    onPrimary = Color.White,
    secondary = Black1,
    tertiary = Grey2,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    surfaceVariant = Grey3,
    onSurface = Color.White,
    onSurfaceVariant = Grey1,
    outline = Grey1,
)

@Composable
fun RecipeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    // Add primary status bar color from chosen color scheme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primaryContainer.toArgb()
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
