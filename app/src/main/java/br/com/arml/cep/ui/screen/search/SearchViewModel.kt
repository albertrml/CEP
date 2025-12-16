package br.com.arml.cep.ui.screen.search

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.SearchUseCase
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase
) : BaseViewModel<SearchState, SearchEvent, SearchEffect>(
    initialState = SearchState(),
    reducer = SearchReducer()
) {
    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> searchCep(event.code)
            is SearchEvent.OnFavorite -> favoriteCep(event.place)
            else -> sendEventForEffect(event)
        }
    }

    private fun favoriteCep(place: Place) {
        viewModelScope.launch {
            useCase.addToFavorite(place).collect { response ->
                sendEventForEffect(SearchEvent.OnFavoriteResponse(response))
            }
        }
    }

    private fun searchCep(code: String) {
        viewModelScope.launch {
            useCase.searchPlace(code).collectLatest { response ->
                sendEvent(SearchEvent.OnSearchResponse(response))
            }
        }
    }
}