package br.com.arml.cep.ui.screen.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.component.log.LogFilterComponent
import br.com.arml.cep.ui.screen.component.log.LogList
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isExtraPaneExpanded by rememberSaveable { mutableStateOf(false) }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            LogListScreen {
                scope.launch {
                    isDetailPaneExpanded = true
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                }
            }
        },

        detailPane = {
            if (isDetailPaneExpanded) {
                LogEntryScreen(
                    onNavigateToExtra = {
                        isExtraPaneExpanded = true
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Extra
                            )
                        }
                    },
                    onNavigateBack = {
                        isDetailPaneExpanded = false
                        scope.launch {
                            navigator.navigateBack()
                        }
                    }
                )
            }
        },

        extraPane = {
            if (isDetailPaneExpanded == false) isExtraPaneExpanded = false
            if (isExtraPaneExpanded) {
                LogExtraScreen {
                    isExtraPaneExpanded = false
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            }
        }
    )

}

@Composable
fun LogListScreen(
    modifier: Modifier = Modifier,
    onNavigateToList: () -> Unit
) {
    val viewModel = hiltViewModel<LogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            title = stringResource(R.string.log_title),
            modifier = Modifier,
            backImgVec = Icons.AutoMirrored.Filled.List
        )
        Spacer(modifier = Modifier.padding(vertical = MaterialTheme.dimens.smallPadding))
        LogFilterComponent(
            modifier = Modifier,
            onFilterByCep = { query -> viewModel.onEvent(LogEvent.FilterByCep(query)) },
            onFilterByInitialDate = { from -> viewModel.onEvent(LogEvent.FilterByInitialDate(from)) },
            onFilterByFinalDate = { until -> viewModel.onEvent(LogEvent.FilterByFinalDate(until)) },
            onFilterByRangeDate = { from, until ->
                viewModel.onEvent(LogEvent.FilterByRangeDate(from, until))
            },
            onNoneFilter = { viewModel.onEvent(LogEvent.FetchAllLogs) }
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.smallPadding),
            thickness = MaterialTheme.dimens.mediumThickness
        )
        Box(modifier = Modifier.weight(1f)){
            state.fetchLog.ShowResults(
                successContent = { logList ->
                    LogList(
                        modifier = Modifier,
                        logEntries = logList,
                        onClickToDelete = { entry ->

                        },
                        onClickToDetail = { onNavigateToList() }
                    )
                },

                loadingContent = {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
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
    }
}

@Composable
fun LogEntryScreen(
    modifier: Modifier = Modifier,
    onNavigateToExtra: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(onClick = onNavigateToExtra) { Text("To Log Extra") }
            Button(onClick = onNavigateBack) { Text("Back to Log List") }
        }
    }
}

@Composable
fun LogExtraScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onNavigateBack) { Text("Back to Log Entry") }
    }
}