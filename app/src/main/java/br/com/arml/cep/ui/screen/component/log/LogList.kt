package br.com.arml.cep.ui.screen.component.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.mock.mockLogEntry
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.theme.dimens


@Composable
fun LogList(
    modifier: Modifier = Modifier,
    logEntries: List<LogEntry>,
    onClickToDelete: (LogEntry) -> Unit,
    onClickToDetail: (LogEntry) -> Unit
){
    val listState = rememberLazyListState()
    ScrollableFab(listState = listState) {
        LazyColumn(
            modifier = modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = logEntries) {
                LogElement(
                    logEntry = it,
                    onClickToDelete = onClickToDelete,
                    onClickToDetail = onClickToDetail
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.smallPadding))
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogListPreview(){
    LogList(
        logEntries = mockLogEntry,
        onClickToDelete = {},
        onClickToDetail = {}
    )
}