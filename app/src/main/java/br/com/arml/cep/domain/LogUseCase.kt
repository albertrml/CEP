package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.repository.LogRepository
import javax.inject.Inject

class LogUseCase @Inject constructor(
    private val repository: LogRepository
) {
    /** Read **/
    fun fetchAllLogs() = repository.fetchLogByZipcode("")
    fun filterLogsByCep(query: String) = repository.fetchLogByZipcode(query)
    fun filterLogsByInitialDate(initialDate: Long) = repository.fetchLogByPeriod(initialDate)
    fun filterLogsByFinalDate(finalDate: Long) = repository.fetchLogByPeriod(finalDate)
    fun filterLogsByRangeDate(initialDate: Long, finalDate: Long) = repository
        .fetchLogByPeriod(initialDate, finalDate)

    /** Delete **/
    fun deleteLog(entry: Log) = repository.deleteLog(entry.toEntity())
    fun deleteAllLogs() = repository.deleteAllLogs()
}