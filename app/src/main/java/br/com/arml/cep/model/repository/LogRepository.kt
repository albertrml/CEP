package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.source.local.LogDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogRepository @Inject constructor(
    private val logDao: LogDao
) {

    fun getAllLogs(): Flow<Response<List<LogEntry>>> = logDao.readAll().toResponseFlow()

    fun filterLogsByCep(query: String) = logDao.filterByCep(query).toResponseFlow()

    fun filterLogsByInitialDate(initialDate: Long) =
        logDao.filterByInitialTimestamp(initialDate).toResponseFlow()

    fun filterLogsByFinalDate(finalDate: Long) =
        logDao.filterByFinalTimestamp(finalDate).toResponseFlow()

    fun filterLogsByRangeDate(initialDate: Long, finalDate: Long) =
        logDao.filterByTimestamp(initialDate, finalDate).toResponseFlow()

    fun deleteAllLogs() = asResponse { logDao.deleteAll() }

    fun deleteLog(entry: LogEntry) = asResponse { logDao.delete(entry) }
}