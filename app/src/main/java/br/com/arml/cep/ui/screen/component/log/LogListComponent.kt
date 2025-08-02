package br.com.arml.cep.ui.screen.component.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.ui.screen.component.common.DeleteAllComponent
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.log.LogState
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun LogListComponent(
    modifier: Modifier = Modifier,
    state: LogState,
    onFilterByCep: (String) -> Unit,
    onFilterByInitialDate: (Long) -> Unit,
    onFilterByFinalDate: (Long) -> Unit,
    onFilterByRangeDate: (Long, Long) -> Unit,
    onFilterByNone: () -> Unit,
    onClickToDeleteEntry: (LogEntry) -> Unit,
    onConfirmDeleteAllEntries: () -> Unit,
    onCopyToClipboard: (LogEntry) -> Unit
) {

    var showDeleteAlert by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        Header(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_logScreen_header)),
            title = stringResource(R.string.log_title),
            logo = Icons.Default.History
        )
        LogFilterComponent(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_logScreen_filterComponent)),
            onFilterByCep = { query -> onFilterByCep(query) },
            onFilterByInitialDate = { from -> onFilterByInitialDate(from) },
            onFilterByFinalDate = { until -> onFilterByFinalDate(until) },
            onFilterByRangeDate = { from, until -> onFilterByRangeDate(from, until) },
            onNoneFilter = onFilterByNone
        )

        DeleteAllComponent(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_logScreen_filter_deleteAllComponent)),
            typeName = stringResource(R.string.log_title),
            showDeleteAlert = { showDeleteAlert = true }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ){
            state.fetchEntries.ShowResults(
                successContent = { logList ->
                    LogList(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .testTag(stringResource(R.string.testTag_logScreen_fetching_OnSuccess)),
                        logEntries = logList,
                        onClickToDelete = { entry -> onClickToDeleteEntry(entry) },
                        onCopyToClipboard = { entry -> onCopyToClipboard(entry) }
                    )
                },

                loadingContent = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(stringResource(R.string.testTag_logScreen_fetching_OnLoading))
                    )
                },

                failureContent = { exception ->
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(stringResource(R.string.testTag_logScreen_fetching_OnFailure)),
                        text = exception.message ?: FetchPlaceException().message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }

        DeleteAllLogAlert(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_logScreen_DeleteAllLogAlert)),
            showDialog = showDeleteAlert,
            onDismissRequest = { showDeleteAlert = false },
            onConfirmation = {
                onConfirmDeleteAllEntries()
                showDeleteAlert = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogListComponentPreview(){
    LogListComponent(
        state = LogState(),
        onFilterByCep = {},
        onFilterByInitialDate = {},
        onFilterByFinalDate = {},
        onFilterByRangeDate = { _, _ -> },
        onFilterByNone = {},
        onClickToDeleteEntry = {},
        onConfirmDeleteAllEntries = {},
        onCopyToClipboard = {}
    )
}