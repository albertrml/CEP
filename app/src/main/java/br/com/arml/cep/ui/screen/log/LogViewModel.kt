package br.com.arml.cep.ui.screen.log

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.LogUseCase
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteAllLogs
import br.com.arml.cep.ui.screen.log.LogEvent.OnDeleteLog
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
            is OnFilterByNone -> fetchAllLogs()
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
                sendEventForEffect(LogEvent.OnDeleteAllLogsResponse(response))
            }
        }
    }

    private fun deleteLog(log: Log) {
        viewModelScope.launch {
            logUseCase.deleteLog(log).collect { response ->
                sendEventForEffect(LogEvent.OnDeleteLogResponse(response))
            }
        }
    }

    private fun fetchAllLogs() {
        viewModelScope.launch {
            logUseCase.fetchAllLogs().collectLatest { response ->
                sendEvent(LogEvent.OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByCep(cep: String) {
        viewModelScope.launch {
            logUseCase.filterLogsByCep(cep).collectLatest { response ->
                sendEvent(LogEvent.OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByFinalDate(finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByFinalDate(finalDate).collectLatest { response ->
                sendEvent(LogEvent.OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByInitialDate(initialDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByInitialDate(initialDate).collectLatest { response ->
                sendEvent(LogEvent.OnFetchAllLogsResponse(response))
            }
        }
    }

    private fun filterByRangeDate(initialDate: Long, finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByRangeDate(initialDate, finalDate).collectLatest { response ->
                sendEvent(LogEvent.OnFetchAllLogsResponse(response))
            }
        }
    }

    /*private var fetchEntriesJob: Job? = null


    fun onEvent(event: LogEvent) {
        when (event) {
            is LogEvent.OnFetchAllLogs -> fetchAllLogs()
            is LogEvent.OnFilterByCep -> filterByCep(event.query)
            is LogEvent.OnFilterByInitialDate -> filterByInitialDate(event.initialDate)
            is LogEvent.OnFilterByFinalDate -> filterByFinalDate(event.finalDate)
            is LogEvent.OnFilterByRangeDate -> with(event) {
                filterByRangeDate(initialDate,finalDate)
            }
            is LogEvent.OnFilterByNone -> filterByNone()
            is LogEvent.OnDeleteAllEntries -> deleteAllLogs()
            is LogEvent.OnDeleteEntry -> deleteLog(event.entry)
        }
    }

    private fun launchFetchEntriesFlow(
        flow: Flow<Response<List<Log>>>,
        operation: LogFilterOption
    ) {
        fetchEntriesJob?.cancel()
        fetchEntriesJob = viewModelScope.launch {
            flow.collectLatest { response ->
                _state.update {
                    it.copy(
                        filterOperation = operation,
                        fetchEntries = response
                    )
                }
            }
        }
    }

    private fun fetchAllLogs() {
        launchFetchEntriesFlow(logUseCase.fetchAllLogs(), LogFilterOption.None)
    }

    private fun filterByNone() {
        if (state.value.filterOperation !is LogFilterOption.None) {
            launchFetchEntriesFlow(logUseCase.fetchAllLogs(), LogFilterOption.None)
        }
    }

    private fun filterByCep(query: String) {
        launchFetchEntriesFlow(logUseCase.filterLogsByCep(query), LogFilterOption.ByCep)
    }

    private fun filterByInitialDate(initialDate: Long) {
        launchFetchEntriesFlow(
            logUseCase.filterLogsByInitialDate(initialDate),
            LogFilterOption.ByInitialDate
        )
    }

    private fun filterByFinalDate(finalDate: Long) {
        launchFetchEntriesFlow(
            logUseCase.filterLogsByFinalDate(finalDate),
            LogFilterOption.ByFinalDate
        )
    }

    private fun filterByRangeDate(initialDate: Long, finalDate: Long) {
        launchFetchEntriesFlow(
            logUseCase.filterLogsByRangeDate(initialDate, finalDate),
            LogFilterOption.ByRangeDate
        )
    }

    private fun deleteAllLogs() {
        viewModelScope.launch {
            logUseCase.deleteAllLogs().collect { response ->
                _state.update { it.copy(deleteLog = response) }
            }
        }
    }

    private fun deleteLog(log: Log) {
        viewModelScope.launch {
            logUseCase.deleteLog(log).collect { response ->
                _state.update { it.copy(deleteLog = response) }
            }
        }
    }*/
}