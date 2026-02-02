package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

sealed class SearchEvent: Reducer.ViewEvent {
    /** Events associated with searching a Place **/
    data class OnSearch(val code: String): SearchEvent()
    data class OnSearchResponse(val response: Response<Place>): SearchEvent()
    /** End events associated with searching a Place **/

    /** Events associated with adding a Place to favorites **/
    data class OnFavorite(val place: Place): SearchEvent()
    data class OnFavoriteResponse(val response: Response<Unit>, val zipcode: String): SearchEvent()
    /** End events associated with adding a Place to favorites **/

    data object OnClear: SearchEvent()
}