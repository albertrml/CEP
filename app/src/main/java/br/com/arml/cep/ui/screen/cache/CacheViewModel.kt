package br.com.arml.cep.ui.screen.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CacheUseCase
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
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
    val _state = MutableStateFlow(CacheState())
    val state = _state.asStateFlow()
    private var fetchEntriesJob: Job? = null

    init { fetchCache() }

    fun onEvent(event: CacheEvent) {
        when (event) {
            is CacheEvent.OnFetchCache -> fetchCache()
            is CacheEvent.OnFilterByCep -> filterByCep(event.cep)
            is CacheEvent.OnFilterNone -> filterByNone()
            is CacheEvent.OnDeleteAll -> deleteAll()
            is CacheEvent.OnDelete -> deleteEntry(event.place)
            is CacheEvent.OnUpdate -> updateCache(event.place)
            is CacheEvent.OnSelectEntryForDetails -> selectEntryToEdit(event.place)
        }
    }

    private fun launchFetchEntriesFlow(
        flow: Flow<Response<List<PlaceEntry>>>,
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


    private fun fetchCache() {
        launchFetchEntriesFlow(cacheUseCase.fetchCache(), PlaceFilterOption.None)
    }

    private fun filterByNone(){
        if (state.value.filterOperation !is PlaceFilterOption.None) {
            launchFetchEntriesFlow(cacheUseCase.fetchCache(), PlaceFilterOption.None)
        }
    }

    private fun filterByCep(query: String) {
        launchFetchEntriesFlow(cacheUseCase.filterByCep(query), PlaceFilterOption.ByCep)
    }

    private fun deleteAll() {
        viewModelScope.launch {
            cacheUseCase.deleteAll().collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun deleteEntry(entry: PlaceEntry) {
        viewModelScope.launch {
            cacheUseCase.deleteEntry(entry).collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun updateCache(entry: PlaceEntry) {
        viewModelScope.launch {
            if (!entry.isFavorite.value) {
                val updatedEntry = entry.copy(
                    note = Note.build(
                        title = entry.cep.text,
                        content = ""
                    ),
                    isFavorite = Favorite(true)
                )
                cacheUseCase.updateEntry(updatedEntry).collect { response ->
                    _state.update { it.copy(deleteEntry = response) }
                }
            }
        }
    }

    private fun selectEntryToEdit(entry: PlaceEntry?) {
        _state.update { it.copy(placeForDetails = entry) }
    }

}