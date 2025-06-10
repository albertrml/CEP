package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun ScrollableFab(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 64.dp,
    content: @Composable (ScrollState) -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val canScrollForward by remember {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }
    val showFab by remember {
        derivedStateOf { scrollState.maxValue > 0 }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content(scrollState)
        if (showFab) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(buttonSize)
                    .padding(
                        bottom = MaterialTheme.dimens.mediumMargin,
                        end = MaterialTheme.dimens.mediumMargin
                    ),
                onClick = {
                    coroutineScope.launch {
                        if (canScrollForward) {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        } else {
                            scrollState.animateScrollTo(0)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (canScrollForward) Icons.Filled.KeyboardArrowDown
                    else Icons.Filled.KeyboardArrowUp,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ScrollableFab(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val canScrollBackward by remember { derivedStateOf { listState.canScrollBackward } }
    val canScrollForward by remember { derivedStateOf { listState.canScrollForward } }
    val shouldShowFab by remember { derivedStateOf { canScrollBackward || canScrollForward } }
    val isScrolledPastFirstItem by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content()
        if (shouldShowFab) {
            Button(
                modifier = Modifier.align(
                    if (canScrollForward) Alignment.BottomCenter else Alignment.TopCenter
                ),
                onClick = {
                    scope.launch {
                        if (isScrolledPastFirstItem) {
                            listState.animateScrollToItem(0)
                        } else {
                            val totalItemsCount = listState.layoutInfo.totalItemsCount
                            if (totalItemsCount > 0 && canScrollForward) {
                                listState.animateScrollToItem(totalItemsCount - 1)
                            }
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = if (isScrolledPastFirstItem) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
                Text(
                    text = if (isScrolledPastFirstItem) {
                        stringResource(R.string.scrollable_button_up)
                    } else {
                        stringResource(R.string.scrollable_button_down)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}