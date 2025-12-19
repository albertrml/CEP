package br.com.arml.cep.ui.screen.component.log.listpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.utils.toFormattedUTCDate
import br.com.arml.cep.ui.screen.component.common.DeleteAllComponent
import br.com.arml.cep.ui.screen.component.log.listpane.item.LogList
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogListPaneOnSuccess(
    modifier: Modifier = Modifier,
    logList: List<Log>,
    onClickToDelete: (Log) -> Unit,
    onClickToDeleteAll: () -> Unit,
    onCopyToClipboard: (Log) -> Unit
) {
    Column(
        modifier = modifier
            .testTag(stringResource(R.string.logListPaneOnSuccess_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {

        DeleteAllComponent(
            deleteLogAlertTitleId = R.string.log_delete_all_log_title,
            deleteLogAlertTextId = R.string.log_delete_all_log_alert,
            onConfirmDeleteAllEntries = onClickToDeleteAll
        )
        LogList(
            logEntries = logList.groupBy { it.timestamp.toFormattedUTCDate() },
            onClickToDelete = { entry -> onClickToDelete(entry) },
            onCopyToClipboard = { entry -> onCopyToClipboard(entry) }
        )
    }
}