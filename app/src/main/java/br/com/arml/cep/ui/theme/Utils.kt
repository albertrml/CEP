package br.com.arml.cep.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

internal fun getDimensAndTypographyByScreenOrientation(
    screenOrientation: Int,
    windowSizeClass: WindowSizeClass
): Pair<Dimens, Typography> {
    return when (screenOrientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            return getDimensAndTypographyByWindowWidthSize(windowSizeClass.widthSizeClass)
        }
        else -> {
            return getDimensAndTypographyByWindowsHeightSize(windowSizeClass.heightSizeClass)
        }
    }
}

private fun getDimensAndTypographyByWindowWidthSize(
    windowWidthSizeClass: WindowWidthSizeClass
): Pair<Dimens, Typography> {
    return when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> compactDimens to compactTypography
        WindowWidthSizeClass.Medium -> mediumDimens to mediumTypography
        else -> expandedDimens to expandedTypography
    }
}

private fun getDimensAndTypographyByWindowsHeightSize(
    windowHeightSizeClass: WindowHeightSizeClass
): Pair<Dimens, Typography> {
    return when (windowHeightSizeClass) {
        WindowHeightSizeClass.Compact -> compactDimens to compactTypography
        WindowHeightSizeClass.Medium -> mediumDimens to mediumTypography
        else -> expandedDimens to expandedTypography
    }
}