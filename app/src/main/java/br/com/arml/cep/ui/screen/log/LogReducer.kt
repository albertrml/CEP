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
                when(val response = event.response){
                    is Loading -> previousState to null
                    else -> previousState.copy(logs = response) to null
                }
                /*val updatedState = previousState.copy(fetchEntries = event.response)
                updatedState to null*/
            }
            is LogEvent.OnDeleteAllLogsResponse -> {
                val effect = when(event.response){
                    is Loading -> null
                    is Success -> {
                        val successMsg = DELETE_ALL_LOGS_SUCCESS_MSG
                        LogEffect.ShowSnackbar(successMsg)
                    }
                    is Failure -> {
                        val failureMsg = DELETE_ALL_LOGS_FAILURE_MSG
                        LogEffect.ShowSnackbar(failureMsg)
                    }
                }
                previousState to effect
            }
            else -> previousState to null
        }
    }
}