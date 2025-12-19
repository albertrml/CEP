package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

sealed class LogEvent : Reducer.ViewEvent {
    /** Events associated with fetch logs **/
    data class OnFetchAllLogsResponse(val response: Response<List<Log>>) : LogEvent()
    /** End events associated with fetch logs **/

    /** Events associated with filter logs **/
    data class OnFilterByCep(val query: String): LogEvent()
    data class OnFilterByInitialDate(val initialDate: Long): LogEvent()
    data class OnFilterByFinalDate(val finalDate: Long): LogEvent()
    data class OnFilterByRangeDate(val initialDate: Long, val finalDate: Long): LogEvent()
    data object OnFilterByNone: LogEvent()
    /** End events associated with filter logs **/

    /** Events associated with delete log **/
    data class OnDeleteLogResponse(val response: Response<Unit>) : LogEvent()
    data class OnDeleteLog(val log: Log): LogEvent()
    /** End events associated with delete log **/

    /** Events associated with delete all log **/
    data class OnDeleteAllLogsResponse(val response: Response<Unit>) : LogEvent()
    data object OnDeleteAllLogs: LogEvent()
    /** End events associated with delete all log **/
}