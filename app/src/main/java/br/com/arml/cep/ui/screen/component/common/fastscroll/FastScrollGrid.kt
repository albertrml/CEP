package br.com.arml.cep.ui.screen.component.common.fastscroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R.string.fastScrollGrid_component_testTag
import br.com.arml.cep.R.string.fastScrollGrid_downButton_text
import br.com.arml.cep.R.string.fastScrollGrid_downButton_testTag
import br.com.arml.cep.R.string.fastScrollGrid_downButton_contentDescription
import br.com.arml.cep.R.string.fastScrollGrid_upButton_contentDescription
import br.com.arml.cep.R.string.fastScrollGrid_upButton_text
import br.com.arml.cep.R.string.fastScrollGrid_upButton_testTag
import br.com.arml.cep.ui.screen.component.common.fastscroll.button.FastScrollButton
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun FastScrollGrid(
    modifier: Modifier = Modifier,
    staggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    columns: StaggeredGridCells = StaggeredGridCells
        .Adaptive(minSize = MaterialTheme.dimens.minSize),
    verticalItemSpacing: Dp = MaterialTheme.dimens.mediumSpacing,
    horizontalArrangement: Arrangement.Horizontal = Arrangement
        .spacedBy(MaterialTheme.dimens.mediumSpacing),
    content: LazyStaggeredGridScope.() -> Unit
) {
    val scope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf { staggeredGridState.firstVisibleItemIndex > 0 }
    }

    val showScrollToBottom by remember {
        derivedStateOf {
            val layoutInfo = staggeredGridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem < totalItems - 1
        }
    }

    Column(
        modifier = modifier.testTag(stringResource(fastScrollGrid_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showScrollToTop) {
            FastScrollButton(
                modifier = Modifier
                    .testTag(stringResource(fastScrollGrid_upButton_testTag)),
                text = stringResource(fastScrollGrid_upButton_text),
                icon = Icons.Filled.KeyboardArrowUp,
                iconContentDescription = stringResource(
                    fastScrollGrid_upButton_contentDescription
                ),
                onClick = { scope.launch { staggeredGridState.animateScrollToItem(0) } }
            )
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier.weight(1f),
            state = staggeredGridState,
            columns = columns,
            verticalItemSpacing = verticalItemSpacing,
            horizontalArrangement = horizontalArrangement,
            content = content
        )

        if (showScrollToBottom) {
            FastScrollButton(
                modifier = Modifier
                    .testTag(stringResource(fastScrollGrid_downButton_testTag)),
                text = stringResource(fastScrollGrid_downButton_text),
                icon = Icons.Filled.KeyboardArrowDown,
                iconContentDescription = stringResource(
                    fastScrollGrid_downButton_contentDescription
                ),
                onClick = {
                    scope.launch {
                        val lastItem = staggeredGridState.layoutInfo.totalItemsCount - 1
                        if (lastItem >= 0) staggeredGridState.animateScrollToItem(lastItem)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1080dp,height=640dp")
@Composable
fun FastScrollGridPreview() {
    FastScrollGrid{
        items(100) {
            Text(
                modifier = Modifier.width(180.dp),
                text = "Item $it"
            )
        }
    }
}