package br.com.arml.cep.ui.screen.component.common.fastscroll

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import br.com.arml.cep.R
import kotlinx.coroutines.launch

@Composable
fun FastScrollList(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    content: @Composable (LazyListState) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollDirectionState by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val isListCanScroll = listState.run { (canScrollBackward || canScrollForward) }

            if(!isListCanScroll || totalItemsCount == 0){
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
        content(listState)
        if (isFabVisible) {
            Button(
                modifier = Modifier
                    .align(alignmentButton)
                    .testTag(stringResource(R.string.fastScrollList_fab_testTag)),
                onClick = {
                    scope.launch {
                        if (isFabPointsDown) {
                            val lastItem = listState.layoutInfo.totalItemsCount-1
                            listState.animateScrollToItem(lastItem)
                        } else {
                            listState.animateScrollToItem(0)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = if (isFabPointsDown)
                        Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (isFabPointsDown)
                        stringResource(R.string.fastScrollList_fabToEnd_contentDescription)
                    else
                        stringResource(R.string.fastScrollList_fabToStart_contentDescription)
                )
                Text(
                    text = if (isFabPointsDown) {
                        stringResource(R.string.fastScrollList_downButton_text)
                    } else {
                        stringResource(R.string.fastScrollList_upButton_text)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FastScrollListPreview(){
    val elements = List<Int>(100){ it }
    FastScrollList() { lazyListState ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState
        ) {
            items(elements){
                Text(it.toString())
            }
        }
    }
}