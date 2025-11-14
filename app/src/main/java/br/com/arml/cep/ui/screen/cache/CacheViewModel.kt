package br.com.arml.cep.ui.screen.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CacheUseCase
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.utils.PlaceFilterOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheUseCase: CacheUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CacheState())
    val state = _state.asStateFlow()
    private var fetchEntriesJob: Job? = null

    init { fetchCacheItems() }

    fun onEvent(event: CacheEvent) {
        when (event) {
            is CacheEvent.OnFetchCache -> fetchCacheItems()
            is CacheEvent.OnFilterByCep -> fetchCacheItems(
                operation = PlaceFilterOption.ByCep,
                query = event.query
            )
            is CacheEvent.OnFilterNone -> fetchCacheItems()
            is CacheEvent.OnDeleteAll -> deleteAllCache()
            is CacheEvent.OnDelete -> deleteCacheItem(event.place)
            is CacheEvent.OnUpdate -> addCacheItemToFavorites(event.place)
            is CacheEvent.OnSelectEntryForDetails -> selectCacheItem(event.place)
        }
    }

    private fun launchFetchingCacheFlow(
        flow: Flow<Response<List<Place>>>,
        operation: PlaceFilterOption
    ) {
        fetchEntriesJob?.cancel()
        fetchEntriesJob = viewModelScope.launch {
            flow.collectLatest { response ->
                _state.update {
                    it.copy(
                        filterOperation = operation,
                        fetchEntries = response
                    )
                }
            }
        }
    }

    private fun fetchCacheItems(operation: PlaceFilterOption = PlaceFilterOption.None, query: String = ""){
        with(cacheUseCase){
            val fetch = when(operation){
                is PlaceFilterOption.ByCep -> findCachedPlacesByCep(query)
                else -> findCachedPlacesByCep()
            }
            launchFetchingCacheFlow(fetch, operation)
        }
    }

    private fun deleteAllCache() {
        viewModelScope.launch {
            cacheUseCase.clearCache().collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun deleteCacheItem(place: Place) {
        viewModelScope.launch {
            cacheUseCase.removePlaceFromCache(place).collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun addCacheItemToFavorites(place: Place) {
        viewModelScope.launch {
            cacheUseCase.addToFavorite(place).collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun selectCacheItem(entry: Place?) {
        _state.update { it.copy(placeForDetails = entry) }
    }

}