package br.com.arml.cep.ui.screen.component.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.screen.component.log.listpane.LogListPaneOnFailure
import br.com.arml.cep.ui.screen.component.log.listpane.LogListPaneOnSuccess
import br.com.arml.cep.ui.screen.component.log.listpane.filter.LogFilterComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.LogFilterOption
import br.com.arml.cep.ui.utils.ShowResults


@Composable
fun LogListPaneComponent(
    modifier: Modifier = Modifier,
    entries: Response<List<Log>>,
    onFilterByCep: (String) -> Unit,
    onFilterByInitialDate: (Long) -> Unit,
    onFilterByFinalDate: (Long) -> Unit,
    onFilterByRangeDate: (Long, Long) -> Unit,
    onFilterByNone: () -> Unit,
    onClickToDelete: (Log) -> Unit,
    onClickToDeleteAll: () -> Unit,
    onCopyToClipboard: (Log) -> Unit
) {
    var selectedFilter by rememberSaveable(stateSaver = LogFilterOption.saver) {
        mutableStateOf(LogFilterOption.None)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(
                modifier = Modifier
                    .testTag(stringResource(R.string.logListPaneComponent_header_testTag)),
                title = stringResource(R.string.log_title),
                logo = Icons.Default.History
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            LogFilterComponent(
                modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
                selectedFilter = selectedFilter,
                onFilterChange = { selectedFilter = it },
                onFilterByCep = { query -> onFilterByCep(query) },
                onFilterByInitialDate = { from -> onFilterByInitialDate(from) },
                onFilterByFinalDate = { until -> onFilterByFinalDate(until) },
                onFilterByRangeDate = { from, until -> onFilterByRangeDate(from, until) },
                onNoneFilter = { onFilterByNone() }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                entries.ShowResults(
                    successContent = { logList ->
                        LogListPaneOnSuccess(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(horizontal = MaterialTheme.dimens.smallPadding),
                            logList = logList,
                            onClickToDelete = onClickToDelete,
                            onClickToDeleteAll = onClickToDeleteAll,
                            onCopyToClipboard = onCopyToClipboard
                        )
                    },
                    /*loadingContent = {
                        LogListPaneOnLoading(modifier = Modifier
                            .testTag(
                                stringResource(logListPaneOnLoading_component_testTag)
                            )
                        )
                    },*/
                    failureContent = { exception ->
                        LogListPaneOnFailure(
                            modifier = Modifier
                                .testTag(
                                    stringResource(
                                        R.string.logListPaneOnFailure_component_testTag
                                    )
                                ),
                            failureMsg = exception.message ?: FetchPlaceException().message,
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogListPaneComponentPreview() {
    LogListPaneComponent(
        entries = Response.Success(mockLogEntries),
        onFilterByCep = {},
        onFilterByInitialDate = {},
        onFilterByFinalDate = {},
        onFilterByRangeDate = { _, _ -> },
        onFilterByNone = {},
        onClickToDelete = {},
        onClickToDeleteAll = {},
        onCopyToClipboard = {}
    )
}