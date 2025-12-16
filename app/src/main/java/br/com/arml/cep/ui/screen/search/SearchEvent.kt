package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

sealed class SearchEvent: Reducer.ViewEvent {
    data class OnSearch(val code: String): SearchEvent()
    data class OnSearchResponse(val response: Response<Place>): SearchEvent()
    data class OnFavorite(val place: Place): SearchEvent()
    data class OnFavoriteResponse(val response: Response<String>): SearchEvent()
    data object OnClear: SearchEvent()
}