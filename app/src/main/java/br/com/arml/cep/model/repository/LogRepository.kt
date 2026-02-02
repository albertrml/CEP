package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.mapSuccess
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.source.local.LogDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogRepository @Inject constructor(
    private val logDao: LogDao
) {
    /** Read **/
    fun fetchLogByZipcode(query: String): Flow<Response<List<Log>>> = logDao
        .selectLogEntitiesByZipcode(query)
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    fun fetchLogByPeriod(
        startDate: Long = 0L,
        endDate: Long = System.currentTimeMillis()
    ): Flow<Response<List<Log>>> = logDao
        .selectLogEntitiesByPeriod(startDate, endDate)
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    /** Delete **/
    fun deleteLog(entry: LogEntity) = asResponse { logDao.deleteLogEntity(entry) }

    fun deleteAllLogs() = asResponse { logDao.deleteAllLogEntities() }
}