package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.R
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.cache.CacheEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnAddToFavoriteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteAllResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFetchCacheResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToListPane
import br.com.arml.cep.ui.utils.UiText
import br.com.arml.core.response.Response.Failure
import br.com.arml.core.response.Response.Loading
import br.com.arml.core.response.Response.Success

class CacheReducer: Reducer<CacheState, CacheEvent, CacheEffect> {
    override fun reduce(
        previousState: CacheState,
        event: CacheEvent
    ): Pair<CacheState, CacheEffect?> {
        return when(event){
            is OnAddToFavoriteResponse -> {
                val (response, zipcode) = event
                val effect = when(response){
                    is Loading -> null
                    is Success -> ShowSnackbar(UiText.StringResource(R.string.cache_add_favorite_success, zipcode))
                    is Failure -> ShowSnackbar(UiText.StringResource(R.string.cache_add_favorite_failure))
                }
                previousState to effect
            }

            is OnDeleteAllResponse -> {
                val effect = when(event.response){
                    is Loading -> null
                    is Success -> ShowSnackbar(UiText.StringResource(R.string.cache_delete_all_success))
                    is Failure -> ShowSnackbar(UiText.StringResource(R.string.cache_delete_all_failure))
                }
                previousState to effect
            }

            is OnDeleteResponse -> {
                val (response, zipcode) = event
                val effect = when(response) {
                    is Loading -> null
                    is Success -> ShowSnackbar(UiText.StringResource(R.string.cache_delete_success, zipcode))
                    is Failure -> ShowSnackbar(UiText.StringResource(R.string.cache_delete_failure))
                }
                previousState to effect
            }

            is OnFetchCacheResponse -> {
                when(val response = event.response){
                    is Loading -> previousState to null
                    else -> previousState.copy(places = response) to null
                }
                /*val updatedState = previousState.copy(places = event.response)
                updatedState to null*/
            }

            is OnNavigateToDetailPane -> {
                val updatedState = previousState.copy(selectedPlace = event.place)
                updatedState to null
            }

            is OnNavigateToListPane -> {
                val updatedState = previousState.copy(selectedPlace = null)
                updatedState to null
            }

            else -> previousState to null
        }
    }
}