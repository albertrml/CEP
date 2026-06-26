package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.search.SearchEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.search.SearchEvent.OnAddressSearchResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnClear
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavoriteResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnCepSearchResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnNavigateBackToDetailPane
import br.com.arml.cep.ui.screen.search.SearchEvent.OnNavigateToExtraPane
import br.com.arml.cep.ui.utils.UiText

class SearchReducer: Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when(event){
            is OnAddressSearchResponse -> {
                val places = event.response
                val updateState = previousState.copy(addressSearchResponse = places)
                updateState to null
            }
            is OnClear -> {
                val updatedState = SearchState()
                updatedState to null
            }
            is OnFavoriteResponse -> {
                val (response, zipcode) = event
                val effect = when(response){
                    is Loading -> null
                    is Success -> {
                        ShowSnackbar(UiText.StringResource(R.string.search_add_favorite_success, zipcode))
                    }
                    is Failure -> {
                        ShowSnackbar(UiText.StringResource(R.string.search_add_favorite_failure))
                    }
                }
                previousState to effect
            }
            is OnCepSearchResponse -> {
                val place = event.response
                val updatedState = previousState.copy(cepSearchResponse = place)
                updatedState to null
            }
            is OnNavigateToExtraPane -> {
                val updatedState = previousState.copy(selectedPlace = event.place)
                updatedState to null
            }
            is OnNavigateBackToDetailPane -> {
                val updatedState = previousState.copy(selectedPlace = null)
                updatedState to null
            }
            else -> previousState to null
        }
    }
}