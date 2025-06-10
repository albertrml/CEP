package br.com.arml.cep.ui.screen.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.LogUseCase
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.LogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logUseCase: LogUseCase
) : ViewModel() {
    val _state = MutableStateFlow(LogState())
    val state = _state.asStateFlow()

    init { fetchAllLogs() }

    fun onEvent(event: LogEvent) {
        when (event) {
            is LogEvent.FetchAllLogs -> fetchAllLogs()
            is LogEvent.FilterByCep -> filterByCep(event.query)
            is LogEvent.FilterByInitialDate -> filterByInitialDate(event.initialDate)
            is LogEvent.FilterByFinalDate -> filterByFinalDate(event.finalDate)
            is LogEvent.FilterByRangeDate -> filterByRangeDate(event.initialDate, event.finalDate)
            is LogEvent.DeleteAllLogs -> deleteAllLogs()
            is LogEvent.DeleteLog -> deleteLog(event.entry)
        }
    }

    private fun fetchAllLogs() {
        viewModelScope.launch {
            if(state.value.wasFiltered || state.value.fetchLog is Response.Loading) {
                logUseCase.fetchAllLogs().collectLatest {
                    _state.value = _state.value.copy(
                        wasFiltered = false,
                        fetchLog = it
                    )
                }
            }
        }
    }

    private fun filterByCep(query: String) {
        viewModelScope.launch {
            logUseCase.filterLogsByCep(query).collect {
                _state.value = _state.value.copy(
                    wasFiltered = true,
                    fetchLog = it
                )
            }
        }
    }

    private fun filterByInitialDate(initialDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByInitialDate(initialDate).collect {
                _state.value = _state.value.copy(
                    wasFiltered = true,
                    fetchLog = it
                )
            }
        }
    }

    private fun filterByFinalDate(finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByFinalDate(finalDate).collect {
                _state.value = _state.value.copy(
                    wasFiltered = true,
                    fetchLog = it
                )
            }
        }
    }

    private fun filterByRangeDate(initialDate: Long, finalDate: Long) {
        viewModelScope.launch {
            logUseCase.filterLogsByRangeDate(initialDate, finalDate).collect {
                _state.value = _state.value.copy(
                    wasFiltered = true,
                    fetchLog = it
                )
            }
        }
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

