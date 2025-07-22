package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.source.local.LogLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogRepository @Inject constructor(
    private val logLocalDataSource: LogLocalDataSource
) {
    fun getAllLogs(): Flow<Response<List<LogEntry>>> = logLocalDataSource.readAll().toResponseFlow()

    fun filterLogsByCep(query: String) = logLocalDataSource.filterByCep(query).toResponseFlow()

    fun filterLogsByInitialDate(initialDate: Long) =
        logLocalDataSource.filterByInitialTimestamp(initialDate).toResponseFlow()

    fun filterLogsByFinalDate(finalDate: Long) =
        logLocalDataSource.filterByFinalTimestamp(finalDate).toResponseFlow()

    fun filterLogsByRangeDate(initialDate: Long, finalDate: Long) =
        logLocalDataSource.filterByTimestamp(initialDate, finalDate).toResponseFlow()

    fun deleteAllLogs() = asResponse { logLocalDataSource.deleteAll() }

    fun deleteLog(entry: LogEntry) = asResponse { logLocalDataSource.delete(entry) }
}