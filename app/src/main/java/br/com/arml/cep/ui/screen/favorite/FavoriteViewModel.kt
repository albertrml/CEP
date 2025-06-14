package br.com.arml.cep.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.FavoriteUseCase
import br.com.arml.cep.model.domain.Favorite
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
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {
    val _state = MutableStateFlow(FavoriteState())
    val state = _state.asStateFlow()
    private var fetchEntriesJob: Job? = null

    init {
        fetchFavorites()
    }

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnClickToUnwanted -> changeFavoriteToUnwanted(event.placeEntry)
            is FavoriteEvent.OnFetchFavorites -> fetchFavorites()
            is FavoriteEvent.OnFilterByCep -> filterByCep(event.cep)
            is FavoriteEvent.OnFilterByTitle -> filterByTitle(event.title)
            is FavoriteEvent.OnFilterNone -> filterByNone()
            is FavoriteEvent.OnUpdateFavorite -> updateFavorite(event.placeEntry)
            is FavoriteEvent.OnSelectEntryToUnwanted -> selectEntryToUnwanted(event.placeEntry)
            is FavoriteEvent.OnSelectEntryToEdit -> selectEntryToEdit(event.placeEntry)
        }
    }

    private fun selectEntryToUnwanted(placeEntry: PlaceEntry?) {
        _state.update { it.copy(placeForUnwanted = placeEntry) }
    }

    private fun selectEntryToEdit(placeEntry: PlaceEntry?) {
        _state.update { it.copy(placeForEdit = placeEntry) }
    }

    private fun changeFavoriteToUnwanted(placeEntry: PlaceEntry) {
        viewModelScope.launch {
            if (placeEntry.isFavorite.value) {
                val newEntry = placeEntry.copy(
                    isFavorite = Favorite(false),
                    note = null
                )
                favoriteUseCase.update(newEntry).collect { response ->
                    _state.update { it.copy(updateEntry = response) }
                }
            }
        }
    }

    private fun fetchFavorites() {
        launchFetchEntriesFlow(favoriteUseCase.fetchFavorites(), PlaceFilterOption.None)
    }

    private fun filterByNone() {
        if (state.value.filterOperation !is PlaceFilterOption.None) {
            launchFetchEntriesFlow(favoriteUseCase.fetchFavorites(), PlaceFilterOption.None)
        }
    }

    private fun filterByCep(query: String) {
        launchFetchEntriesFlow(favoriteUseCase.filterByCep(query), PlaceFilterOption.ByCep)
    }

    private fun filterByTitle(query: String) {
        launchFetchEntriesFlow(favoriteUseCase.filterByTitle(query), PlaceFilterOption.ByTitle)
    }

    private fun updateFavorite(entry: PlaceEntry) {
        viewModelScope.launch {
            favoriteUseCase.update(entry).collect { response ->
                _state.update { it.copy(updateEntry = response) }
            }
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

}