package br.com.arml.cep.ui.screen.component.common.fastscroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun FastScrollGrid(
    modifier: Modifier = Modifier,
    staggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    content: @Composable (LazyStaggeredGridState) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollDirectionState by remember {
        derivedStateOf {
            val layoutInfo = staggeredGridState.layoutInfo
            val firstVisibleItemIndex = staggeredGridState.firstVisibleItemIndex
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItem =
                staggeredGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val isListCanScroll = staggeredGridState.run { (canScrollBackward || canScrollForward) }

            if (!isListCanScroll || totalItemsCount == 0) {
                false to false
            } else {
                val itemsAfterLastVisible = totalItemsCount - lastVisibleItem - 1
                if (firstVisibleItemIndex == 0 && itemsAfterLastVisible == 0) {
                    false to false
                } else {
                    true to (itemsAfterLastVisible >= firstVisibleItemIndex)
                }
            }
        }
    }
    val (isFabVisible, isFabPointsDown) = scrollDirectionState
    val alignmentButton by remember {
        derivedStateOf {
            if (isFabPointsDown) Alignment.BottomCenter else Alignment.TopCenter
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content(staggeredGridState)
        if (isFabVisible) {
            Button(
                modifier = Modifier
                    .align(alignmentButton)
                    .testTag(stringResource(R.string.fastScrollGrid_fab_testTag)),
                onClick = {
                    scope.launch {
                        if (isFabPointsDown) {
                            val lastItem = staggeredGridState.layoutInfo.totalItemsCount - 1
                            staggeredGridState.animateScrollToItem(lastItem)
                        } else {
                            staggeredGridState.animateScrollToItem(0)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = if (isFabPointsDown)
                        Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (isFabPointsDown)
                        stringResource(R.string.fastScrollGrid_fabToEnd_contentDescription)
                    else
                        stringResource(R.string.fastScrollGrid_fabToStart_contentDescription)
                )
                Text(
                    text = if (isFabPointsDown) {
                        stringResource(R.string.fastScrollGrid_downButton_text)
                    } else {
                        stringResource(R.string.fastScrollGrid_upButton_text)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1080dp,height=640dp")
@Composable
fun FastScrollGridPreview() {
    FastScrollGrid(
        staggeredGridState = rememberLazyStaggeredGridState()
    ) { staggeredGridState ->
        LazyVerticalStaggeredGrid(
            state = staggeredGridState,
            columns = StaggeredGridCells.Adaptive(minSize = MaterialTheme.dimens.minSize),
            verticalItemSpacing = MaterialTheme.dimens.mediumSpacing,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ){
            items(100){
                Text(
                    modifier = Modifier.width(180.dp),
                    text = "Item $it"
                )
            }
        }
    }
}