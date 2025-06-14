package br.com.arml.cep.ui.screen.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.LogUseCase
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.ui.utils.LogFilterOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logUseCase: LogUseCase
) : ViewModel() {
    val _state = MutableStateFlow(LogState())
    val state = _state.asStateFlow()
    private var fetchEntriesJob: Job? = null

    init { fetchAllLogs() }

    fun onEvent(event: LogEvent) {
        when (event) {
            is LogEvent.OnFetchAllLogs -> fetchAllLogs()
            is LogEvent.OnFilterByCep -> filterByCep(event.query)
            is LogEvent.OnFilterByInitialDate -> filterByInitialDate(event.initialDate)
            is LogEvent.OnFilterByFinalDate -> filterByFinalDate(event.finalDate)
            is LogEvent.OnFilterByRangeDate -> filterByRangeDate(event.initialDate, event.finalDate)
            is LogEvent.OnFilterByNone -> filterByNone()
            is LogEvent.OnDeleteAllEntries -> deleteAllLogs()
            is LogEvent.OnDeleteEntry -> deleteLog(event.entry)
        }
    }

    private fun launchFetchEntriesFlow(
        flow: Flow<Response<List<LogEntry>>>,
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
            logUseCase.deleteAllLogs().collect {
                _state.value = _state.value.copy(deleteLog = it)
            }
        }
    }

    private fun deleteLog(entry: LogEntry) {
        viewModelScope.launch {
            logUseCase.deleteLog(entry).collect {
                _state.value = _state.value.copy(deleteLog = it)
            }
        }
    }
}

