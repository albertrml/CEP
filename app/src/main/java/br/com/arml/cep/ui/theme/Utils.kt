package br.com.arml.cep.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

internal fun getDimensAndTypographyByWindowsSize(
    windowWidthSizeClass: WindowWidthSizeClass
): Pair<Dimens, Typography> {
    return when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> compactDimens to compactTypography
        WindowWidthSizeClass.Medium -> mediumDimens to mediumTypography
        else -> expandedDimens to expandedTypography
    }
}