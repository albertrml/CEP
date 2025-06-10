package br.com.arml.cep.domain

import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.repository.LogRepository
import javax.inject.Inject

class LogUseCase @Inject constructor(
    private val repository: LogRepository
) {
    fun fetchAllLogs() = repository.getAllLogs()
    fun filterLogsByCep(query: String) = repository.filterLogsByCep(query)
    fun filterLogsByInitialDate(initialDate: Long) = repository.filterLogsByInitialDate(initialDate)
    fun filterLogsByFinalDate(finalDate: Long) = repository.filterLogsByFinalDate(finalDate)
    fun filterLogsByRangeDate(initialDate: Long, finalDate: Long) =
        repository.filterLogsByRangeDate(initialDate, finalDate)
    fun deleteAllLogs() = repository.deleteAllLogs()
    fun deleteLog(entry: LogEntry) = repository.deleteLog(entry)
}