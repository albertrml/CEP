package br.com.arml.cep.ui.screen.log

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.LogUseCase
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteAllLogs
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteAllLogsResponse
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteLog
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteLogResponse
import br.com.arml.cep.ui.screen.log.LogEvent.OnFetchAllLogsResponse
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByFinalDate
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByInitialDate
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByNone
import br.com.arml.cep.ui.screen.log.LogEvent.OnFilterByRangeDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logUseCase: LogUseCase,
    reducer: LogReducer,
    state: LogState
) : BaseViewModel<LogState, LogEvent, LogEffect>(
    initialState = state,
    reducer = reducer
) {

    private sealed class LogFilter {
        data object None : LogFilter()
        data class ByCep(val query: String) : LogFilter()
        data class ByInitialDate(val initialDate: Long) : LogFilter()
        data class ByFinalDate(val finalDate: Long) : LogFilter()
        data class ByRangeDate(val initialDate: Long, val finalDate: Long) : LogFilter()
    }

    private val _filter = MutableStateFlow<LogFilter>(LogFilter.None)

    init { fetchAllLogs() }

    fun onEvent(event: LogEvent) {
        when (event) {
            is OnFilterByNone -> _filter.update { LogFilter.None }
            is OnFilterByCep -> _filter.update { LogFilter.ByCep(event.query) }
            is OnFilterByInitialDate -> _filter.update { LogFilter.ByInitialDate(event.initialDate) }
            is OnFilterByFinalDate -> _filter.update { LogFilter.ByFinalDate(event.finalDate) }
            is OnFilterByRangeDate -> _filter.update {
                LogFilter.ByRangeDate(
                    initialDate = event.initialDate,
                    finalDate = event.finalDate
                )
            }
            is OnDeleteAllLogs -> deleteAllLogs()
            is OnDeleteLog -> deleteLog(event.log)
            else -> sendEventForEffect(event)
        }
    }

    private fun deleteAllLogs() {
        collectAction(
            flow = logUseCase.deleteAllLogs(),
            onResponse = { OnDeleteAllLogsResponse(it) }
        )
    }

    private fun deleteLog(log: Log) {
        collectAction(
            flow = logUseCase.deleteLog(log),
            onResponse = { OnDeleteLogResponse(it) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchAllLogs() {
        _filter
            .flatMapLatest { filter ->
                with(logUseCase) {
                    when (filter) {
                        is LogFilter.None -> fetchAllLogs()
                        is LogFilter.ByCep -> filterLogsByCep(filter.query)
                        is LogFilter.ByFinalDate -> filterLogsByFinalDate(filter.finalDate)
                        is LogFilter.ByInitialDate -> filterLogsByInitialDate(filter.initialDate)
                        is LogFilter.ByRangeDate ->
                            filterLogsByRangeDate(filter.initialDate, filter.finalDate)
                    }
                }
            }
            .onEach { response -> sendEventForEffect(OnFetchAllLogsResponse(response)) }
            .launchIn(viewModelScope)
    }
}