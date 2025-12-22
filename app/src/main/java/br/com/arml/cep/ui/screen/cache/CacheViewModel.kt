package br.com.arml.cep.ui.screen.cache

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CacheUseCase
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnAddToFavorite
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnAddToFavoriteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDelete
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteAll
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteAllResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFetchCache
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFetchCacheResponse
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFilterNone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheUseCase: CacheUseCase,
    initialState: CacheState,
    reducer: CacheReducer
): BaseViewModel<CacheState, CacheEvent, CacheEffect>(
    initialState = initialState,
    reducer = reducer
) {

    init { fetchCache() }

    fun onEvent(event: CacheEvent) {
        when (event) {
            is OnDeleteAll -> deleteAllCache()
            is OnDelete -> deleteCachePlace(event.place)
            is OnFetchCache -> fetchCache()
            is OnFilterByCep -> fetchCache(event.query)
            is OnFilterNone -> fetchCache()
            is OnAddToFavorite -> addNoteToFavorite(event.place)
            else -> sendEventForEffect(event)
        }
    }

    private fun addNoteToFavorite(place: Place) {
        viewModelScope.launch {
            cacheUseCase.addToFavorite(place).collect { response ->
                sendEventForEffect(OnAddToFavoriteResponse(response, place.cep.text))
            }
        }
    }

    private fun deleteAllCache(){
        viewModelScope.launch {
            cacheUseCase.clearCache().collect { response ->
                sendEventForEffect(OnDeleteAllResponse(response))
            }
        }
    }

    private fun deleteCachePlace(place: Place) {
        viewModelScope.launch {
            cacheUseCase.removePlaceFromCache(place).collect { response ->
                sendEventForEffect(OnDeleteResponse(response, place.cep.text))
            }
        }
    }

    private fun fetchCache(query: String = ""){
        viewModelScope.launch {
            cacheUseCase.findCachedPlacesByCep(query).collectLatest { response ->
                sendEventForEffect(OnFetchCacheResponse(response))
            }
        }
    }
}
