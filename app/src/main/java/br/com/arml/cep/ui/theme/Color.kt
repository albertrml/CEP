package br.com.arml.cep.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// PRIMARY — azul institucional forte
val primaryLight = Color(0xFF1A237E)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFD7E2FC)
val onPrimaryContainerLight = Color(0xFF000D23)

// SECONDARY — azul complementar (menos dominante)
val secondaryLight = Color(0xFF01579B)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFAAD0F8)
val onSecondaryContainerLight = Color(0xFF001945)

// TERTIARY — amarelo Correios (destaque)
val tertiaryLight = Color(0xFFB79104)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFBE186)
val onTertiaryContainerLight = Color(0xFF221B00)

// ERROR — Material padrão
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)

// SURFACE & BACKGROUND — claros e neutros
val backgroundLight = Color(0xFFFFFFFF)
val onBackgroundLight = Color(0xFF100121)

val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF1B1B1F)

// VARIANTS — separação visual suave
val surfaceVariantLight = Color(0xFFF3F6FB)
val onSurfaceVariantLight = Color(0xFF44474F)

// CONTAINERS — Material 3 correto
val surfaceContainerLowLight = Color(0xFFEAF2FF)   // Cards
val surfaceContainerLight = Color(0xFFE6EEFD)      // Bottom bar / sheets

// OUTLINES — leves para daylight
val outlineLight = Color(0xFF8A8FA3)
val outlineVariantLight = Color(0xFFC2C6D4)


// PRIMARY — azul institucional adaptado ao dark
val primaryDark = Color(0xFFB6C4FF)
val onPrimaryDark = Color(0xFF0E1A4B)
val primaryContainerDark = Color(0xFF1A237E)
val onPrimaryContainerDark = Color(0xFFDDE3FF)

// SECONDARY — azul complementar
val secondaryDark = Color(0xFF8CC9FF)
val onSecondaryDark = Color(0xFF003354)
val secondaryContainerDark = Color(0xFF014A7A)
val onSecondaryContainerDark = Color(0xFFCFE6FF)

// TERTIARY — amarelo Correios (controle de brilho)
val tertiaryDark = Color(0xFFF0D35A)
val onTertiaryDark = Color(0xFF3A2F00)
val tertiaryContainerDark = Color(0xFF5E4B00)
val onTertiaryContainerDark = Color(0xFFFFF0C2)

// ERROR — padrão Material
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)

// BACKGROUND & SURFACE — escuros azulados
val backgroundDark = Color(0xFF0E1118)
val onBackgroundDark = Color(0xFFE4E6EB)

val surfaceDark = Color(0xFF0E1118)
val onSurfaceDark = Color(0xFFE4E6EB)

// VARIANTS — separação visual
val surfaceVariantDark = Color(0xFF1E2433)
val onSurfaceVariantDark = Color(0xFFC4C6D0)

// CONTAINERS — elevação Material 3
val surfaceContainerLowDark = Color(0xFF151A28)   // Cards
val surfaceContainerDark = Color(0xFF1B2233)      // Bottom bar / sheets

// OUTLINES — discretos
val outlineDark = Color(0xFF8E91A3)
val outlineVariantDark = Color(0xFF44485C)


private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerLow = surfaceContainerLowDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerLow = surfaceContainerLowLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight
)

internal fun getColorScheme(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) DarkColorScheme else LightColorScheme
}