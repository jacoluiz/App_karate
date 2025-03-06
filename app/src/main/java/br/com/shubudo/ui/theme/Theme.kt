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
    onPrimary = Color.Black,
    outline = Color.Black,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightBrancaColorScheme = lightColorScheme(
    primary = LightPrimaryColorBranca,
    primaryContainer = LightPrimaryContainerColorBranca,
    secondary = SecondaryColorBranca,
    tertiary = LightTertiaryColorBranca,
    onPrimary = Color.Black,
    outline = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),
)

private val DarkAmarelaColorScheme = darkColorScheme(
    primary = PrimaryColorAmarela,
    primaryContainer = PrimaryContainerColorAmarela,
    secondary = SecondaryColorAmarela,
    tertiary = TertiaryColorAmarela,
    onPrimary = Color.Black,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightAmarelaColorScheme = lightColorScheme(
    primary = LightPrimaryColorAmarela,
    primaryContainer = LightPrimaryContainerColorAmarela,
    secondary = SecondaryColorAmarela,
    tertiary = TertiaryColorAmarela,
    onPrimary = Color.Black,
    outline = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),

    )

private val DarkLaranjaColorScheme = darkColorScheme(
    primary = PrimaryColorLaranja,
    primaryContainer = PrimaryContainerColorLaranja,
    secondary = SecondaryColorLaranja,
    tertiary = TertiaryColorLaranja,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),

    )

private val LightLaranjaColorScheme = lightColorScheme(
    primary = LightPrimaryColorLaranja,
    primaryContainer = LightPrimaryContainerColorLaranja,
    secondary = SecondaryColorLaranja,
    tertiary = TertiaryColorLaranja,
    onPrimary = Color.White,
    outline = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),
)

private val DarkVerdeColorScheme = darkColorScheme(
    primary = PrimaryColorVerde,
    primaryContainer = PrimaryContainerColorVerde,
    secondary = SecondaryColorVerde,
    tertiary = TertiaryColorVerde,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),

    )

private val LightVerdeColorScheme = lightColorScheme(
    primary = LightPrimaryColorVerde,
    primaryContainer = LightPrimaryContainerColorVerde,
    secondary = SecondaryColorVerde,
    tertiary = TertiaryColorVerde,
    onPrimary = Color.White,
    outline = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),
)

private val DarkRoxaColorScheme = darkColorScheme(
    primary = PrimaryColorRoxa,
    primaryContainer = PrimaryContainerColorRoxa,
    secondary = SecondaryColorRoxa,
    tertiary = TertiaryColorRoxa,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),

    )

private val LightRoxaColorScheme = lightColorScheme(
    primary = LightPrimaryColorRoxa,
    primaryContainer = LightPrimaryContainerColorRoxa,
    secondary = SecondaryColorRoxa,
    tertiary = TertiaryColorRoxa,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),
)

private val DarkMarromColorScheme = darkColorScheme(
    primary = PrimaryColorMarron,
    primaryContainer = PrimaryContainerColorMarron,
    secondary = SecondaryColorMarron,
    tertiary = TertiaryColorMarron,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightMarromColorScheme = lightColorScheme(
    primary = LightPrimaryColorMarron,
    primaryContainer = LightPrimaryContainerColorMarron,
    secondary = SecondaryColorMarron,
    tertiary = LightTertiaryColorMarron,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),

    )

private val DarkPretaColorScheme = darkColorScheme(
    primary = PrimaryColorPreta,
    primaryContainer = PrimaryContainerColorPreta,
    secondary = SecondaryColorPreta,
    tertiary = TertiaryColorPreta,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightPretaColorScheme = lightColorScheme(
    primary = LightPrimaryColorPreta,
    primaryContainer = LightPrimaryContainerColorPreta,
    secondary = SecondaryColorPreta,
    tertiary = LightTertiaryColorPreta,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),

    )

private val DarkMestreColorScheme = darkColorScheme(
    primary = PrimaryColorMestre,
    primaryContainer = PrimaryContainerColorMestre,
    secondary = SecondaryColorMestre,
    tertiary = TertiaryColorMestre,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightMestreColorScheme = lightColorScheme(
    primary = LightPrimaryColorMestre,
    primaryContainer = LightPrimaryContainerColorMestre,
    secondary = SecondaryColorMestre,
    tertiary = LightTertiaryColorMestre,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),

    )

private val DarkGraoMestreColorScheme = darkColorScheme(
    primary = PrimaryColorGraoMestre,
    primaryContainer = PrimaryContainerColorGraoMestre,
    secondary = SecondaryColorGraoMestre,
    tertiary = TertiaryColorGraoMestre,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFF202020),
    onBackground = Color(0xFFD5D5D5),
    surface = Color(0xFF383838),
)

private val LightGraoMestreColorScheme = lightColorScheme(
    primary = LightPrimaryColorGraoMestre,
    primaryContainer = LightPrimaryContainerColorGraoMestre,
    secondary = SecondaryColorGraoMestre,
    tertiary = LightTertiaryColorGraoMestre,
    onPrimary = Color.White,
    outline = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF202020),
    surface = Color(0xFFE2E2E2),

    )


@Composable
fun AppShubudoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), faixa: String, content: @Composable () -> Unit
) {
    val colorScheme = when (faixa) {
        "Branca" -> if (darkTheme) DarkBrancaColorScheme else LightBrancaColorScheme
        "Amarela" -> if (darkTheme) DarkAmarelaColorScheme else LightAmarelaColorScheme
        "Laranja" -> if (darkTheme) DarkLaranjaColorScheme else LightLaranjaColorScheme
        "Verde" -> if (darkTheme) DarkVerdeColorScheme else LightVerdeColorScheme
        "Roxa" -> if (darkTheme) DarkRoxaColorScheme else LightRoxaColorScheme
        "Marrom" -> if (darkTheme) DarkMarromColorScheme else LightMarromColorScheme
        "Preta" -> if (darkTheme) DarkPretaColorScheme else LightPretaColorScheme
        "Preta 1 dan" -> if (darkTheme) DarkPretaColorScheme else LightPretaColorScheme
        "Preta 2 dan" -> if (darkTheme) DarkPretaColorScheme else LightPretaColorScheme
        "Preta 3 dan" -> if (darkTheme) DarkPretaColorScheme else LightPretaColorScheme
        "Preta 4 dan" -> if (darkTheme) DarkPretaColorScheme else LightPretaColorScheme
        "Mestre" -> if (darkTheme) DarkMestreColorScheme else LightMestreColorScheme
        "Grão Mestre" -> if (darkTheme) DarkGraoMestreColorScheme else LightGraoMestreColorScheme


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
        colorScheme = colorScheme, typography = CustomTypography, content = content
    )
}