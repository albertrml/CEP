package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

sealed class CacheEvent: Reducer.ViewEvent {
    /** Events associated with fetching and filtering cached places **/
    data class OnFetchCacheResponse(val response: Response<List<Place>>) : CacheEvent()
    data object OnFetchCache : CacheEvent()
    data class OnFilterByCep(val query: String) : CacheEvent()
    data object OnFilterNone : CacheEvent()
    /** End events associated with fetching all cached places **/


    /** Events associated with deleting all cached places **/
    data class OnDeleteAllResponse(val response: Response<Unit>): CacheEvent()
    data object OnDeleteAll : CacheEvent()
    /** End events associated with deleting all cached places **/

    /** Events associated with deleting a single cached place **/
    data class OnDeleteResponse(val response: Response<Unit>, val zipcode: String): CacheEvent()
    data class OnDelete(val place: Place) : CacheEvent()
    /** End events associated with deleting a single cached place **/

    /** Events associated with adding a cached place to favorites **/
    data class OnAddToFavoriteResponse(val response: Response<Unit>, val zipcode: String) : CacheEvent()
    data class OnAddToFavorite(val place: Place) : CacheEvent()
    /** End events associated with adding a cached place to favorites **/

    /** Events associated with navigating between panes **/
    data class OnNavigateToDetailPane(val place: Place?) : CacheEvent()
    data object OnNavigateToListPane: CacheEvent()
    /** End events associated with navigating between panes **/
}