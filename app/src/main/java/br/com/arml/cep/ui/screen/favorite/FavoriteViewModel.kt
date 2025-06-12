package br.com.arml.cep.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.FavoriteUseCase
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnClickToUnwanted -> changeFavoriteToUnwanted(event.placeEntry)
            is FavoriteEvent.OnFetchFavorites -> fetchFavorites()
            is FavoriteEvent.OnFilterByCep -> filterByCep(event.cep)
            is FavoriteEvent.OnFilterByTitle -> filterByTitle(event.title)
            is FavoriteEvent.OnFilterNone -> filterNone()
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
        viewModelScope.launch {
            if (state.value.wasFiltered || state.value.fetchEntries is Response.Loading) {
                favoriteUseCase.fetchFavorites().collectLatest { response ->
                    _state.update {
                        it.copy(
                            wasFiltered = false,
                            fetchEntries = response
                        )
                    }
                }
            }
        }
    }

    private fun filterByCep(query: String) {
        viewModelScope.launch {
            favoriteUseCase.filterByCep(query).collect { response ->
                _state.update {
                    it.copy(
                        wasFiltered = true,
                        fetchEntries = response
                    )
                }
            }
        }
    }

    private fun filterByTitle(query: String) {
        viewModelScope.launch {
            favoriteUseCase.filterByTitle(query).collect { response ->
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
        viewModelScope.launch {
            if (state.value.wasFiltered) {
                favoriteUseCase.fetchFavorites().collect { response ->
                    _state.update {
                        it.copy(
                            wasFiltered = false,
                            fetchEntries = response
                        )
                    }
                }
            }
        }
    }

    private fun updateFavorite(entry: PlaceEntry) {
        viewModelScope.launch {
            favoriteUseCase.update(entry).collect { response ->
                _state.update { it.copy(updateEntry = response) }
            }
        }
    }
}