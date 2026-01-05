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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheUseCase: CacheUseCase,
    initialState: CacheState,
    reducer: CacheReducer
) : BaseViewModel<CacheState, CacheEvent, CacheEffect>(
    initialState = initialState,
    reducer = reducer
) {
    private sealed class CacheFilter {
        data object None : CacheFilter()
        data class ByCep(val zipcode: String) : CacheFilter()
    }

    private val _filter = MutableStateFlow<CacheFilter>(CacheFilter.None)

    init { fetchCache() }

    fun onEvent(event: CacheEvent) {
        when (event) {
            is OnDeleteAll -> deleteAllCache()
            is OnDelete -> deleteCachePlace(event.place)
            is OnFetchCache -> fetchCache()
            is OnFilterByCep -> _filter.update { CacheFilter.ByCep(event.query) }
            is OnFilterNone -> _filter.update { CacheFilter.None }
            is OnAddToFavorite -> addNoteToFavorite(event.place)
            else -> sendEventForEffect(event)
        }
    }

    private fun addNoteToFavorite(place: Place) {
        collectAction(
            flow = cacheUseCase.addToFavorite(place),
            onResponse = { OnAddToFavoriteResponse(it, place.cep.text) }
        )
    }

    private fun deleteAllCache() {
        collectAction(
            flow = cacheUseCase.clearCache(),
            onResponse = { OnDeleteAllResponse(it) }
        )
    }

    private fun deleteCachePlace(place: Place) {
        collectAction(
            flow = cacheUseCase.removePlaceFromCache(place),
            onResponse = { OnDeleteResponse(it, place.cep.text) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchCache() {
        _filter
            .flatMapLatest { filter ->
                when (filter) {
                    is CacheFilter.None -> cacheUseCase.findCachedPlacesByCep()
                    is CacheFilter.ByCep -> cacheUseCase.findCachedPlacesByCep(filter.zipcode)
                }
            }
            .onEach { response -> sendEventForEffect(OnFetchCacheResponse(response)) }
            .launchIn(viewModelScope)
    }
}
