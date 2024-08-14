package br.com.shubudo.ui.theme

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

private val DarkBrancaColorScheme = darkColorScheme(
    primary = PrimaryColorBranca,
    primaryContainer = PrimaryContainerColorBranca,
    secondary = SecondaryColorBranca,
    tertiary = TertiaryColorBranca,
    onPrimary = Color.White,
    outline = Color.Black,

    )

private val LightBrancaColorScheme = lightColorScheme(
    primary = LightPrimaryColorBranca,
    primaryContainer = LightPrimaryContainerColorBranca,
    secondary = SecondaryColorBranca,
    tertiary = TertiaryColorBranca,
    onPrimary = Color.Black,
    outline = Color.Black,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val DarkAmarelaColorScheme = darkColorScheme(
    primary = PrimaryColorAmarela,
    primaryContainer = PrimaryContainerColorAmarela,
    secondary = SecondaryColorAmarela,
    tertiary = TertiaryColorAmarela,
    onPrimary = Color.White,
    outline = Color.White,

    )

private val LightAmarelaColorScheme = lightColorScheme(
    primary = LightPrimaryColorAmarela,
    primaryContainer = LightPrimaryContainerColorAmarela,
    secondary = SecondaryColorAmarela,
    tertiary = TertiaryColorAmarela,
    onPrimary = Color.Black,
    outline = Color.Black,

    )

private val DarkLaranjaColorScheme = darkColorScheme(
    primary = PrimaryColorLaranja,
    primaryContainer = PrimaryContainerColorLaranja,
    secondary = SecondaryColorLaranja,
    tertiary = TertiaryColorLaranja,
    onPrimary = Color.White,
    outline = Color.White,

    )

private val LightLaranjaColorScheme = lightColorScheme(
    primary = LightPrimaryColorLaranja,
    primaryContainer = LightPrimaryContainerColorLaranja,
    secondary = SecondaryColorLaranja,
    tertiary = TertiaryColorLaranja,
    onPrimary = Color.Black,
    outline = Color.Black,

    )

private val DarkVerdeColorScheme = darkColorScheme(
    primary = PrimaryColorVerde,
    primaryContainer = PrimaryContainerColorVerde,
    secondary = SecondaryColorVerde,
    tertiary = TertiaryColorVerde,
    onPrimary = Color.White,
    outline = Color.White,

    )

private val LightVerdeColorScheme = lightColorScheme(
    primary = LightPrimaryColorVerde,
    primaryContainer = LightPrimaryContainerColorVerde,
    secondary = SecondaryColorVerde,
    tertiary = TertiaryColorVerde,
    onPrimary = Color.Black,
    outline = Color.Black,

    )

private val DarkRoxaColorScheme = darkColorScheme(
    primary = PrimaryColorRoxa,
    primaryContainer = PrimaryContainerColorRoxa,
    secondary = SecondaryColorRoxa,
    tertiary = TertiaryColorRoxa,
    onPrimary = Color.White,
    outline = Color.White,

    )

private val LightRoxaColorScheme = lightColorScheme(
    primary = LightPrimaryColorRoxa,
    primaryContainer = LightPrimaryContainerColorRoxa,
    secondary = SecondaryColorRoxa,
    tertiary = TertiaryColorRoxa,
    onPrimary = Color.Black,
    outline = Color.White,

    )

private val DarkMarromColorScheme = darkColorScheme(
    primary = PrimaryColorMarron,
    primaryContainer = PrimaryContainerColorMarron,
    secondary = SecondaryColorMarron,
    tertiary = TertiaryColorMarron,
    onPrimary = Color.White,
    outline = Color.White,

    )

private val LightMarromColorScheme = lightColorScheme(
    primary = LightPrimaryColorMarron,
    primaryContainer = LightPrimaryContainerColorMarron,
    secondary = SecondaryColorMarron,
    tertiary = TertiaryColorMarron,
    onPrimary = Color.Black,
    outline = Color.White,

    )


@Composable
fun AppShubudoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    faixa: String,
    content: @Composable () -> Unit
) {
    val colorScheme = when (faixa) {
        "Branca" -> if (darkTheme) DarkBrancaColorScheme else LightBrancaColorScheme
        "Amarela" -> if (darkTheme) DarkAmarelaColorScheme else LightAmarelaColorScheme
        "Laranja" -> if (darkTheme) DarkLaranjaColorScheme else LightLaranjaColorScheme
        "Verde" -> if (darkTheme) DarkVerdeColorScheme else LightVerdeColorScheme
        "Roxa" -> if (darkTheme) DarkRoxaColorScheme else LightRoxaColorScheme
        "Marrom" -> if (darkTheme) DarkMarromColorScheme else LightMarromColorScheme
        else -> if (darkTheme) DarkBrancaColorScheme else LightBrancaColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}