package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.entity.LogEntry

sealed class LogEvent{
    object OnFetchAllLogs: LogEvent()
    data class OnFilterByCep(val query: String): LogEvent()
    data class OnFilterByInitialDate(val initialDate: Long): LogEvent()
    data class OnFilterByFinalDate(val finalDate: Long): LogEvent()
    data class OnFilterByRangeDate(val initialDate: Long, val finalDate: Long): LogEvent()
    object OnFilterByNone: LogEvent()
    object OnDeleteAllEntries: LogEvent()
    data class OnDeleteEntry(val entry: LogEntry): LogEvent()
}