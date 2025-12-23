package br.com.arml.cep.ui.screen.component.common.fastscroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import br.com.arml.cep.R.string
import br.com.arml.cep.R.string.fastScrollList_component_testTag
import br.com.arml.cep.R.string.fastScrollList_downButton_contentDescription
import br.com.arml.cep.R.string.fastScrollList_downButton_testTag
import br.com.arml.cep.R.string.fastScrollList_downButton_text
import br.com.arml.cep.R.string.fastScrollList_upButton_contentDescription
import br.com.arml.cep.R.string.fastScrollList_upButton_testTag
import br.com.arml.cep.ui.screen.component.common.fastscroll.button.FastScrollButton
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun FastScrollList(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = Arrangement
        .spacedBy(MaterialTheme.dimens.mediumSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: LazyListScope.() -> Unit
) {
    val scope = rememberCoroutineScope()

    val showScrollToTop by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    val showScrollToBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem < totalItems - 1
        }
    }

    Column(
        modifier = modifier.testTag(stringResource(fastScrollList_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showScrollToTop) {
            FastScrollButton(
                modifier = Modifier
                    .testTag(stringResource(fastScrollList_upButton_testTag)),
                text = stringResource(string.fastScrollList_upButton_text),
                icon = Icons.Filled.KeyboardArrowUp,
                iconContentDescription = stringResource(
                    fastScrollList_upButton_contentDescription
                ),
                onClick = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )

        if (showScrollToBottom) {
            FastScrollButton(
                modifier = Modifier
                    .testTag(stringResource(fastScrollList_downButton_testTag)),
                text = stringResource(fastScrollList_downButton_text),
                icon = Icons.Filled.KeyboardArrowDown,
                iconContentDescription = stringResource(
                    fastScrollList_downButton_contentDescription
                ),
                onClick = {
                    scope.launch {
                        val lastItem = listState.layoutInfo.totalItemsCount - 1
                        if (lastItem >= 0) listState.animateScrollToItem(lastItem)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FastScrollListPreview(){
    val elements = List(100){ it }
    FastScrollList(modifier = Modifier.fillMaxWidth()) {
        items(elements){
            Text(it.toString())
        }
    }
}