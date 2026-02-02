package br.com.arml.cep.ui.screen.log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.ui.screen.component.log.LogListPaneComponent
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteAllLogs
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteLog
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByFinalDate
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByInitialDate
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByNone
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByRangeDate
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    val uiStateHolder = rememberLogScreenUiStateHolder()
    val viewModel = hiltViewModel<LogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val marginScreen = modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LogEffect.ShowSnackbar -> {
                    uiStateHolder.snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        modifier = marginScreen,
        snackbarHost = { SnackbarHost(uiStateHolder.snackbarHostState) }
    ) { innerPadding ->
        LogListPaneComponent(
            modifier = Modifier.padding(innerPadding),
            entries = state.logs,
            onFilterByCep = { query -> viewModel.onEvent(OnFilterByCep(query)) },
            onFilterByInitialDate = { from ->
                viewModel.onEvent(OnFilterByInitialDate(from))
            },
            onFilterByFinalDate = { until ->
                viewModel.onEvent(OnFilterByFinalDate(until))
            },
            onFilterByRangeDate = { from, until ->
                viewModel.onEvent(OnFilterByRangeDate(from, until))
            },
            onFilterByNone = { viewModel.onEvent(OnFilterByNone) },
            onClickToDelete = { entry -> viewModel.onEvent(OnDeleteLog(entry)) },
            onClickToDeleteAll = { viewModel.onEvent(OnDeleteAllLogs) },
            onCopyToClipboard = { entry -> uiStateHolder.onCopyToClipboard(entry) }
        )
    }
}