package br.com.arml.cep.ui.screen.log

import android.util.Log
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
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    val uiStateHolder = rememberLogScreenUiStateHolder()
    val viewModel = hiltViewModel<LogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val marginScreen = Modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    Log.d("LogScreen", "LogScreen: ${state.logs}")

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
        LogListPaneComponent(
            modifier = marginScreen.padding(innerPadding),
            entries = state.logs,
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
            onFilterByNone = {
                Log.d("LogScreen", "onFilterByNone: ${state.logs}")
                viewModel.onEvent(LogEvent.OnFilterByNone)
            },
            onClickToDelete = { entry ->
                viewModel.onEvent(LogEvent.OnDeleteLog(entry))
            },
            onClickToDeleteAll = { viewModel.onEvent(LogEvent.OnDeleteAllLogs) },
            onCopyToClipboard = { entry -> uiStateHolder.onCopyToClipboard(entry) }
        )
    }
}