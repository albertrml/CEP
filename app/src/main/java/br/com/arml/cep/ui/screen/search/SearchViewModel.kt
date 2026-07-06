package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.domain.SearchUseCase
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.search.SearchEvent.OnAddressSearch
import br.com.arml.cep.ui.screen.search.SearchEvent.OnAddressSearchResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavorite
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavoriteResponse
import br.com.arml.cep.ui.screen.search.SearchEvent.OnCepSearch
import br.com.arml.cep.ui.screen.search.SearchEvent.OnCepSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
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
            is OnAddressSearch -> with(event) { searchAddress(uf, city, street) }
            is OnCepSearch -> searchCep(event.code)
            is OnFavorite -> favoriteCep(event.place)
            else -> sendEventForEffect(event)
        }
    }

    private fun favoriteCep(place: Place) {
        collectAction(
            flow = useCase.addToFavorite(place),
            onResponse = { OnFavoriteResponse(it, place.cep.text) }
        )
    }

    private fun searchCep(code: String) {
        collectAction(
            flow = useCase.searchPlace(code),
            onResponse = { OnCepSearchResponse(it) }
        )
    }

    private fun searchAddress(uf: String, city: String, street: String){
        collectAction(
            flow = useCase.searchPlaces(uf, city, street),
            onResponse = { OnAddressSearchResponse(it) }
        )
    }
}