package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.domain.Log

sealed class LogEvent{
    data object OnFetchAllLogs: LogEvent()
    data class OnFilterByCep(val query: String): LogEvent()
    data class OnFilterByInitialDate(val initialDate: Long): LogEvent()
    data class OnFilterByFinalDate(val finalDate: Long): LogEvent()
    data class OnFilterByRangeDate(val initialDate: Long, val finalDate: Long): LogEvent()
    data object OnFilterByNone: LogEvent()
    data object OnDeleteAllEntries: LogEvent()
    data class OnDeleteEntry(val entry: Log): LogEvent()
}