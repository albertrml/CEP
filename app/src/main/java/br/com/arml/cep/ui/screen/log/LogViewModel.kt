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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    init { fetchAllLogs() }

    fun onEvent(event: LogEvent) {
        when (event) {
            is OnFilterByNone -> filterByNone()
            is OnFilterByCep -> filterByCep(event.query)
            is OnFilterByInitialDate -> filterByInitialDate(event.initialDate)
            is OnFilterByFinalDate -> filterByFinalDate(event.finalDate)
            is OnFilterByRangeDate -> with(event) {
                filterByRangeDate(initialDate, finalDate)
            }
            is OnDeleteAllLogs -> deleteAllLogs()
            is OnDeleteLog -> deleteLog(event.log)
            else -> sendEventForEffect(event)
        }
    }

    private fun deleteAllLogs() {
        viewModelScope.launch {
            logUseCase.deleteAllLogs().collect { response ->
                sendEventForEffect(OnDeleteAllLogsResponse(response))
            }
        }
    }

    private fun deleteLog(log: Log) {
        viewModelScope.launch {
            logUseCase.deleteLog(log).collect { response ->
                sendEventForEffect(OnDeleteLogResponse(response))
            }
        }
    }

    private fun fetchAllLogs() {
        viewModelScope.launch {
            logUseCase.fetchAllLogs().collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByCep(cep: String) {
        viewModelScope.launch {
            logUseCase.filterLogsByCep(cep).collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByFinalDate(finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByFinalDate(finalDate).collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByInitialDate(initialDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByInitialDate(initialDate).collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByNone() {
        viewModelScope.launch {
            logUseCase.fetchAllLogs().collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }
    private fun filterByRangeDate(initialDate: Long, finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByRangeDate(initialDate, finalDate).collectLatest { response ->
                sendEvent(OnFetchAllLogsResponse(response))
            }
        }
    }
}