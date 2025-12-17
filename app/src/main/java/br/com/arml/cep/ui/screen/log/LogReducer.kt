package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.application.EnvironmentVariables.LogReducerVariables.DELETE_ALL_LOGS_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.LogReducerVariables.DELETE_ALL_LOGS_SUCCESS_MSG
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.ui.common.Reducer

class LogReducer : Reducer<LogState, LogEvent, LogEffect> {
    override fun reduce(
        previousState: LogState,
        event: LogEvent
    ): Pair<LogState, LogEffect?> {
        return when(event){
            is LogEvent.OnFetchAllLogsResponse -> {
                val updatedState = previousState.copy(fetchEntries = event.response)
                updatedState to null
            }
            is LogEvent.OnDeleteAllLogs -> {
                val updatedState = previousState.copy(showDeleteAllLogAlert = true)
                updatedState to null
            }
            is LogEvent.OnDeleteAllLogsResponse -> {
                when(event.response){
                    is Loading -> { previousState to null }
                    is Success -> {
                        val updatedState = previousState.copy(showDeleteAllLogAlert = false)
                        val successMsg = DELETE_ALL_LOGS_SUCCESS_MSG
                        updatedState to LogEffect.ShowSnackbar(successMsg)
                    }
                    is Failure -> {
                        val updatedState = previousState.copy(showDeleteAllLogAlert = false)
                        val failureMsg = DELETE_ALL_LOGS_FAILURE_MSG
                        updatedState to LogEffect.ShowSnackbar(failureMsg)
                    }
                }
            }
            else -> previousState to null
        }
    }
}