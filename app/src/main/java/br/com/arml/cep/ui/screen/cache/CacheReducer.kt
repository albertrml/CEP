package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.DELETE_ALL_CACHE_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.DELETE_ALL_CACHE_SUCCESS_MSG
import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.getAddingFavoriteFailureMessage
import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.getAddingFavoriteSuccessMessage
import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.getDeletingCacheFailureMessage
import br.com.arml.cep.application.EnvironmentVariables.CacheReducerVariables.getDeletingCacheSuccessMessage
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.cache.CacheEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnAddToFavoriteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteAllResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFetchCacheResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToListPane

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
                    is Success -> ShowSnackbar(getAddingFavoriteSuccessMessage(zipcode))
                    is Failure -> ShowSnackbar(getAddingFavoriteFailureMessage(zipcode))
                }
                previousState to effect
            }

            is OnDeleteAllResponse -> {
                val effect = when(event.response){
                    is Loading -> null
                    is Success -> ShowSnackbar(DELETE_ALL_CACHE_SUCCESS_MSG)
                    is Failure -> ShowSnackbar(DELETE_ALL_CACHE_FAILURE_MSG)
                }
                previousState to effect
            }

            is OnDeleteResponse -> {
                val (response, zipcode) = event
                val effect = when(response) {
                    is Loading -> null
                    is Success -> ShowSnackbar(getDeletingCacheSuccessMessage(zipcode))
                    is Failure -> ShowSnackbar(getDeletingCacheFailureMessage(zipcode))
                }
                previousState to effect
            }

            is OnFetchCacheResponse -> {
                val updatedState = previousState.copy(places = event.response)
                updatedState to null
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