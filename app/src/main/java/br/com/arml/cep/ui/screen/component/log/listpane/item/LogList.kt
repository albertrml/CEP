package br.com.arml.cep.ui.screen.component.log.listpane.item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.utils.toFormattedUTCDate
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollList
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogList(
    modifier: Modifier = Modifier,
    logEntries: Map<String, List<Log>>,
    onClickToDelete: (Log) -> Unit,
    onCopyToClipboard: (Log) -> Unit
) {
    FastScrollList(
        modifier = modifier
            .testTag(stringResource(R.string.logList_component_testTag))
    ) {
        logEntries.forEach { (date, logEntries) ->
            stickyHeader {
                LogListHeader(
                    modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing),
                    date = date
                )
            }
            items(items = logEntries) {
                LogElement(
                    log = it,
                    onClickToDelete = onClickToDelete,
                    onClickToDetail = onCopyToClipboard
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogListPreview() {
    LogList(
        logEntries = mockLogEntries.groupBy { it.timestamp.toFormattedUTCDate() },
        onClickToDelete = {},
        onCopyToClipboard = {}
    )
}