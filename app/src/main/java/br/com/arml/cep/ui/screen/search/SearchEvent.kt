package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.cep.ui.common.Reducer

sealed class SearchEvent: Reducer.ViewEvent {
    /** Events associated with searching Places **/
    data class OnAddressSearch(val uf: String, val city: String, val street: String ): SearchEvent()
    data class OnAddressSearchResponse(val response: Response<List<Place>>): SearchEvent()
    /** End events associated with searching a Place **/

    /** Events associated with searching a Place **/
    data class OnCepSearch(val code: String): SearchEvent()
    data class OnCepSearchResponse(val response: Response<Place>): SearchEvent()
    /** End events associated with searching a Place **/

    /** Events associated with adding a Place to favorites **/
    data class OnFavorite(val place: Place): SearchEvent()
    data class OnFavoriteResponse(val response: Response<Unit>, val zipcode: String): SearchEvent()
    /** End events associated with adding a Place to favorites **/

    data class OnNavigateToExtraPane(val place: Place): SearchEvent()
    data object OnNavigateBackToDetailPane: SearchEvent()

    data object OnClear: SearchEvent()
}