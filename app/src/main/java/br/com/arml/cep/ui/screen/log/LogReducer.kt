package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.R
import br.com.arml.core.response.Response.*
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.utils.UiText

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
                        LogEffect.ShowSnackbar(UiText.StringResource(R.string.log_delete_all_success))
                    }
                    is Failure -> {
                        LogEffect.ShowSnackbar(UiText.StringResource(R.string.log_delete_all_failure))
                    }
                }
                previousState to effect
            }
            else -> previousState to null
        }
    }
}