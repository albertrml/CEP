package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.application.EnvironmentVariables.SearchReducerVariables.ADDING_TO_FAVORITE_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.SearchReducerVariables.getAddingToFavoriteSuccessMessage
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.search.SearchEvent.OnClear
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavoriteResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnSearchResponse

class SearchReducer: Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when(event){
            is OnClear -> {
                val updatedState = SearchState()
                updatedState to null
            }
            is OnFavoriteResponse -> {
                when(event.response){
                    is Loading -> previousState to null
                    is Success -> {
                        val zipcode = event.response.result
                        val successMessage = getAddingToFavoriteSuccessMessage(zipcode)
                        val effect = SearchEffect.ShowSnackbar(successMessage)
                        previousState to effect
                    }
                    is Failure -> {
                        val effect = SearchEffect.ShowSnackbar(ADDING_TO_FAVORITE_FAILURE_MSG)
                        previousState to effect
                    }
                }
            }
            is OnSearchResponse -> {
                val place = event.response
                val updatedState = previousState.copy(entry = place)
                updatedState to null
            }
            else -> previousState to null
        }
    }
}