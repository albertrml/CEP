package br.com.arml.cep.ui.screen.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CacheUseCase
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.entity.PlaceEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val repository: CacheUseCase
) : ViewModel() {
    val _state = MutableStateFlow(CacheState())
    val state = _state.asStateFlow()

    init { fetchCache() }

    fun onEvent(event: CacheEvent) {
        when (event) {
            is CacheEvent.OnFetchCache -> fetchCache()
            is CacheEvent.OnFilterByCep -> filterByCep(event.cep)
            is CacheEvent.OnFilterNone -> filterNone()
            is CacheEvent.OnDeleteAll -> deleteAll()
            is CacheEvent.OnDelete -> deleteEntry(event.place)
            is CacheEvent.OnUpdate -> updateEntry(event.place)
            is CacheEvent.OnSelectEntryForDetails -> selectEntryForDetails(event.place)
        }
    }

    private fun fetchCache() {
        viewModelScope.launch {
            repository.fetchCache().collect { response ->
                _state.update {
                    it.copy(
                        wasFiltered = false,
                        fetchEntries = response
                    )
                }
            }
        }
    }

    private fun filterByCep(query: String) {
        viewModelScope.launch {
            repository.filterByCep(query).collect { response ->
                _state.update {
                    it.copy(
                        wasFiltered = true,
                        fetchEntries = response
                    )
                }
            }
        }
    }

    private fun filterNone() {
        if (state.value.wasFiltered) {
            fetchCache()
        }
    }

    private fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll().collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }


    private fun deleteEntry(entry: PlaceEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry).collect { response ->
                _state.update { it.copy(deleteEntry = response) }
            }
        }
    }

    private fun updateEntry(entry: PlaceEntry) {
        viewModelScope.launch {
            if (!entry.isFavorite.value) {
                val updatedEntry = entry.copy(
                    note = Note.build(
                        title = entry.cep.text,
                        content = ""
                    ),
                    isFavorite = Favorite(true)
                )
                repository.updateEntry(updatedEntry).collect { response ->
                    _state.update { it.copy(deleteEntry = response) }
                }
            }
        }
    }

    private fun selectEntryForDetails(entry: PlaceEntry?) {
        _state.update { it.copy(placeForDetails = entry) }
    }

}