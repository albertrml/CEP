package br.com.arml.cep.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    // Corner radius
    val smallCornerRadius: Dp = 8.dp,
    val mediumCornerRadius: Dp = 12.dp,
    val largeCornerRadius: Dp = 16.dp,
    // Elevation
    val smallElevation: Dp = 0.dp,
    val mediumElevation: Dp = 0.dp,
    val largeElevation: Dp = 0.dp,
    // Margin
    val smallMargin: Dp = 0.dp,
    val mediumMargin: Dp = 0.dp,
    val largeMargin: Dp = 0.dp,
    val xLargeMargin: Dp = 0.dp,
    // Padding
    val smallPadding: Dp = 0.dp,
    val mediumPadding: Dp = 0.dp,
    val largePadding: Dp = 0.dp,
    // Spacing
    val smallSpacing: Dp = 0.dp,
    val mediumSpacing: Dp = 0.dp,
    val largeSpacing: Dp = 0.dp,
    // Thickness
    val smallThickness: Dp = 0.dp,
    val mediumThickness: Dp = 0.dp,
    val largeThickness: Dp = 0.dp,
)

// Width < 600: Phone in portrait
val compactDimens = Dimens(
    // Elevation
    smallElevation = 0.dp,
    mediumElevation = 1.dp,
    largeElevation = 2.dp,
    // Margin
    smallMargin = 8.dp,
    mediumMargin = 16.dp,
    largeMargin = 24.dp,
    xLargeMargin = 40.dp,
    // Padding
    smallPadding = 8.dp,
    mediumPadding = 16.dp,
    largePadding = 24.dp,
    // Spacing
    smallSpacing = 4.dp,
    mediumSpacing = 8.dp,
    largeSpacing = 12.dp,
    // Thickness
    smallThickness = 1.dp,
    mediumThickness = 2.dp,
    largeThickness = 3.dp
)

/* 600 ≤ width < 840:
*   - Tablet in portrait
*   - Foldable in portrait (unfolded)
 */
val mediumDimens = Dimens(
    // Elevation
    smallElevation = 1.dp,
    mediumElevation = 2.dp,
    largeElevation = 3.dp,
    // Margin
    smallMargin = 16.dp,
    mediumMargin = 24.dp,
    largeMargin = 32.dp,
    xLargeMargin = 48.dp,
    // Padding
    smallPadding = 8.dp,
    mediumPadding = 12.dp,
    largePadding = 16.dp,
    // Spacing
    smallSpacing = 16.dp,
    mediumSpacing = 24.dp,
    largeSpacing = 32.dp,
    // Thickness
    smallThickness = 2.dp,
    mediumThickness = 3.dp,
    largeThickness = 4.dp
)

/* 840 ≤ width < 1200:
*   - Phone in landscape
*   - Tablet in landscape
*   - Foldable in landscape (unfolded)
*   - Desktop
*/
val expandedDimens = Dimens(
    // Elevation
    smallElevation = 2.dp,
    mediumElevation = 3.dp,
    largeElevation = 4.dp,
    // Margin
    smallMargin = 16.dp,
    mediumMargin = 24.dp,
    largeMargin = 32.dp,
    xLargeMargin = 48.dp,
    // Padding
    smallPadding = 16.dp,
    mediumPadding = 24.dp,
    largePadding = 32.dp,
    // Spacing
    smallSpacing = 12.dp,
    mediumSpacing = 16.dp,
    largeSpacing = 20.dp,
    // Thickness
    smallThickness = 3.dp,
    mediumThickness = 4.dp,
    largeThickness = 5.dp
)