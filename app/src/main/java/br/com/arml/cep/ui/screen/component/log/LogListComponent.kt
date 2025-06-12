package br.com.arml.cep.ui.screen.component.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.log.LogEvent
import br.com.arml.cep.ui.screen.log.LogViewModel
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun LogListComponent(
    modifier: Modifier = Modifier,
    onCopyToClipboard: (LogEntry) -> Unit
) {
    val viewModel = hiltViewModel<LogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteAlert by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            modifier = modifier,
            title = stringResource(R.string.log_title),
            logo = Icons.AutoMirrored.Filled.List
        )

        Spacer(modifier = Modifier.padding(vertical = MaterialTheme.dimens.smallPadding))

        LogFilterComponent(
            modifier = modifier,
            onFilterByCep = { query -> viewModel.onEvent(LogEvent.FilterByCep(query)) },
            onFilterByInitialDate = { from -> viewModel.onEvent(LogEvent.FilterByInitialDate(from)) },
            onFilterByFinalDate = { until -> viewModel.onEvent(LogEvent.FilterByFinalDate(until)) },
            onFilterByRangeDate = { from, until ->
                viewModel.onEvent(LogEvent.FilterByRangeDate(from, until))
            },
            onNoneFilter = { viewModel.onEvent(LogEvent.FetchAllLogs) }
        )

        Row(
            modifier = modifier.padding(vertical = MaterialTheme.dimens.smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = MaterialTheme.dimens.mediumThickness
            )
            Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.smallPadding))
            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = { showDeleteAlert = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.log_list_delete_all_button_description),
                )
                Text(
                    text = stringResource(R.string.log_list_delete_all_button),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        Box(modifier = modifier.weight(1f)){
            state.fetchLog.ShowResults(
                successContent = { logList ->
                    LogList(
                        modifier = Modifier,
                        logEntries = logList,
                        onClickToDelete = { entry -> viewModel.onEvent(LogEvent.DeleteLog(entry)) },
                        onCopyToClipboard = { entry -> onCopyToClipboard(entry) }
                    )
                },

                loadingContent = {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                },

                failureContent = {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = it.message ?: stringResource(R.string.log_error_unknown),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }

        DeleteAllLogAlert(
            showDialog = showDeleteAlert,
            onDismissRequest = { showDeleteAlert = false },
            onConfirmation = {
                viewModel.onEvent(LogEvent.DeleteAllLogs)
                showDeleteAlert = false
            }
        )
    }
}