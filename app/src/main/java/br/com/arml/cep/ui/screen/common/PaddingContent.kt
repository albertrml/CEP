package br.com.arml.cep.ui.screen.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import br.com.arml.cep.ui.theme.dimens


@Composable
fun calculateTopPadding(padding: PaddingValues) = padding.calculateTopPadding() +
        WindowInsets.systemBars.asPaddingValues().calculateTopPadding() +
        MaterialTheme.dimens.xLargeMargin

@Composable
fun calculateBottomPadding(padding: PaddingValues) = padding.calculateBottomPadding() +
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
        MaterialTheme.dimens.mediumMargin

@Composable
fun calculatePadding(padding: PaddingValues = PaddingValues(0.dp)) = PaddingValues(
    top = calculateTopPadding(padding),
    bottom = calculateBottomPadding(padding),
    start = MaterialTheme.dimens.mediumMargin,
    end = MaterialTheme.dimens.mediumMargin,
)