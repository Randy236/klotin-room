// Theme.kt
package com.example.mobileapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🎨 Dark mode moderno
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = AccentCaution,
    tertiary = AccentPositive,
    background = Color(0xFF1E1E1E), // Fondo oscuro puro para mejor contraste
    surface = Color(0xFF2C2C2C),    // Superficies ligeramente más claras
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// 🎨 Light mode moderno
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = AccentCaution,
    tertiary = AccentPositive,
    background = Background,         // Color crema claro
    surface = BackgroundLight,       // Amarillo pastel suave
    onPrimary = Color.White,
    onSecondary = TextPrimary,       // Negro solo títulos
    onTertiary = TextSecondary,      // Gris para texto
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun MobileAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
