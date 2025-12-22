package br.com.arml.cep.ui.screen.search

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.SearchUseCase
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavorite
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavoriteResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnSearch
import br.com.arml.cep.ui.screen.search.SearchEvent.OnSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    initialState: SearchState,
    reducer: SearchReducer
) : BaseViewModel<SearchState, SearchEvent, SearchEffect>(
    initialState = initialState,
    reducer = reducer
) {
    fun onEvent(event: SearchEvent) {
        when (event) {
            is OnSearch -> searchCep(event.code)
            is OnFavorite -> favoriteCep(event.place)
            else -> sendEventForEffect(event)
        }
    }

    private fun favoriteCep(place: Place) {
        viewModelScope.launch {
            useCase.addToFavorite(place).collect { response ->
                sendEventForEffect(OnFavoriteResponse(response, place.cep.text))
            }
        }
    }

    private fun searchCep(code: String) {
        viewModelScope.launch {
            useCase.searchPlace(code).collectLatest { response ->
                sendEvent(OnSearchResponse(response))
            }
        }
    }
}