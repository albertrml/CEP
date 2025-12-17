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
import br.com.arml.cep.ui.screen.component.log.LogScreenListComponent
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    val uiStateHolder = rememberLogScreenUiStateHolder()
    val viewModel = hiltViewModel<LogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val marginScreen = Modifier
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
        modifier = modifier,
        snackbarHost = { SnackbarHost(uiStateHolder.snackbarHostState) }
    ) { innerPadding ->
        LogScreenListComponent(
            modifier = marginScreen.padding(innerPadding),
            state = state,
            onFilterByCep = { query -> viewModel.onEvent(LogEvent.OnFilterByCep(query)) },
            onFilterByInitialDate = { from ->
                viewModel.onEvent(LogEvent.OnFilterByInitialDate(from))
            },
            onFilterByFinalDate = { until ->
                viewModel.onEvent(LogEvent.OnFilterByFinalDate(until))
            },
            onFilterByRangeDate = { from, until ->
                viewModel.onEvent(
                    LogEvent.OnFilterByRangeDate(from, until)
                )
            },
            onFilterByNone = { viewModel.onEvent(LogEvent.OnFilterByNone) },
            onClickToDeleteEntry = { entry ->
                viewModel.onEvent(LogEvent.OnDeleteLog(entry))
            },
            onConfirmDeleteAllEntries = { viewModel.onEvent(LogEvent.OnDeleteAllLogs) },
            onCopyToClipboard = { entry -> uiStateHolder.onCopyToClipboard(entry) }
        )
    }
}