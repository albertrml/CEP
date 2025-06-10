package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.entity.LogEntry

sealed class LogEvent{
    object FetchAllLogs: LogEvent()
    data class FilterByCep(val query: String): LogEvent()
    data class FilterByInitialDate(val initialDate: Long): LogEvent()
    data class FilterByFinalDate(val finalDate: Long): LogEvent()
    data class FilterByRangeDate(val initialDate: Long, val finalDate: Long): LogEvent()
    object DeleteAllLogs: LogEvent()
    data class DeleteLog(val entry: LogEntry): LogEvent()
}