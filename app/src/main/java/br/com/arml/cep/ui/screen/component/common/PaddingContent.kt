package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun calculateTopPadding(padding: PaddingValues) =
    padding.calculateTopPadding() +
    WindowInsets.systemBars.asPaddingValues().calculateTopPadding() +
    WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()

@Composable
private fun calculateBottomPadding(padding: PaddingValues) =
    padding.calculateBottomPadding() +
    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

@Composable
private fun calculateStartPadding(padding: PaddingValues) =
    padding.calculateStartPadding(LocalLayoutDirection.current) +
    WindowInsets.systemGestures
        .asPaddingValues()
        .calculateStartPadding(LocalLayoutDirection.current)

@Composable
private fun calculateEndPadding(padding: PaddingValues) =
    padding.calculateEndPadding(LocalLayoutDirection.current) +
    WindowInsets.systemGestures
        .asPaddingValues()
        .calculateEndPadding(LocalLayoutDirection.current)

@Composable
fun calculatePaddingInsets(padding: PaddingValues = PaddingValues(0.dp)) = PaddingValues(
    top = calculateTopPadding(padding),
    bottom = calculateBottomPadding(padding),
    start = calculateStartPadding(padding),
    end = calculateEndPadding(padding),
)

@Composable
fun PaddingValues.changeTop(newTop: Dp) =
    PaddingValues(
        top = newTop,
        bottom = this.calculateBottomPadding(),
        start = this.calculateStartPadding(LocalLayoutDirection.current),
        end = this.calculateEndPadding(LocalLayoutDirection.current)
    )



