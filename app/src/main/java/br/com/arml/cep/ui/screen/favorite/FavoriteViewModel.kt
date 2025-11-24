package br.com.arml.cep.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.FavoriteUseCase
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
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
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteState())
    val state = _state.asStateFlow()
    private var fetchEntriesJob: Job? = null

    init { fetchFavorites() }
    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnClickToUnwanted -> changeFavoriteToUnwanted(event.place)
            is FavoriteEvent.OnExportFavorites -> exportFavorites()
            is FavoriteEvent.OnExportHide -> exportHide()
            is FavoriteEvent.OnExportShow -> exportShow()
            is FavoriteEvent.OnFetchFavorites -> fetchFavorites()
            is FavoriteEvent.OnFilterByCep -> filterByCep(event.cep)
            is FavoriteEvent.OnFilterByTitle -> filterByTitle(event.title)
            is FavoriteEvent.OnFilterNone -> filterByNone()
            is FavoriteEvent.OnImportBackup -> importBackup(event.json)
            is FavoriteEvent.OnImportHide -> importHide()
            is FavoriteEvent.OnImportShow -> importShow()
            is FavoriteEvent.OnSelectFavoriteEntry -> selectEntryToEdit(event.favorite)
            is FavoriteEvent.OnSelectEntryToUnwanted -> selectEntryToUnwanted(event.place)
            is FavoriteEvent.OnSelectNoteEntry -> selectNoteEntry(event.note)
            is FavoriteEvent.OnAddNoteToFavorite -> addNoteToFavorite(event.cep, event.note)
            is FavoriteEvent.OnDeleteNoteFromFavorite -> deleteNoteFromFavorite(event.noteWithCep)
            is FavoriteEvent.OnEditNoteFromFavorite -> editNoteFromFavorite(event.note)
        }
    }

    fun selectNoteEntry(note: Note?){ _state.update { it.copy(noteForEdit = note) } }

    fun addNoteToFavorite(cep: Cep, note: Note){
        viewModelScope.launch {
            favoriteUseCase.addNoteToFavorite(cep, note).collect { response ->
                _state.update { state -> state.copy(addNoteEntry = response) }
            }
        }
    }

    fun editNoteFromFavorite(note: Note) {
        viewModelScope.launch {
            favoriteUseCase.updateNote(note).collect { response ->
                _state.update { state ->  state.copy(updateNoteEntry = response) }
            }
        }
    }

    fun deleteNoteFromFavorite(noteWithCep: Pair<Cep,Note>) {
        viewModelScope.launch {
            with(noteWithCep) {
                favoriteUseCase.deleteNote(first,second).collect { response ->
                    _state.update { state -> state.copy(deleteNoteEntry = response) }
                }
            }
        }
    }

    private fun changeFavoriteToUnwanted(place: Place) {
        viewModelScope.launch {
            favoriteUseCase.removeFromFavorite(place).collect { response ->
                _state.update {
                    when (response) {
                        is Response.Success -> it.copy(
                            placeForUnwanted = null,
                            updateNoteEntry = response
                        )
                        else -> it.copy(updateNoteEntry = response)
                    }
                }
            }
        }
    }

    private fun exportFavorites() {
        viewModelScope.launch {
            favoriteUseCase.exportFavorites().collect { response ->
                _state.update { state ->
                    when (response) {
                        is Response.Loading -> state.copy(exportBackup = response)
                        else -> state.copy(
                            exportBackup = response,
                            exportAlert = false
                        )
                    }
                }
            }
        }
    }

    private fun exportHide() {
        _state.update {
            it.copy(
                exportBackup = Response.Loading,
                exportAlert = false
            )
        }
    }

    private fun exportShow() {
        _state.update {
            it.copy(
                exportBackup = Response.Loading,
                exportAlert = true
            )
        }
    }

    private fun fetchFavorites() {
        launchFetchEntriesFlow(favoriteUseCase.fetchFavorites(), PlaceFilterOption.None)
    }

    private fun filterByCep(query: String) {
        launchFetchEntriesFlow(favoriteUseCase.filterByCep(query), PlaceFilterOption.ByCep)
    }

    private fun filterByTitle(query: String) {
        launchFetchEntriesFlow(favoriteUseCase.filterByTitle(query), PlaceFilterOption.ByTitle)
    }

    private fun filterByNone() {
        if (state.value.filterOperation !is PlaceFilterOption.None) {
            launchFetchEntriesFlow(favoriteUseCase.fetchFavorites(), PlaceFilterOption.None)
        }
    }

    private fun importBackup(json: String) {
        viewModelScope.launch {
            favoriteUseCase.importFavorites(json).collect { response ->
                _state.update {
                    when (response) {
                        is Response.Loading -> it.copy(importBackup = response)
                        else -> it.copy(
                            importBackup = response,
                            importAlert = false
                        )
                    }
                }
            }
        }
    }

    private fun importHide() {
        _state.update {
            it.copy(
                importBackup = Response.Loading,
                importAlert = false
            )
        }
    }

    private fun importShow() {
        _state.update {
            it.copy(
                importBackup = Response.Loading,
                importAlert = true
            )
        }
    }

    private fun selectEntryToEdit(favorite: Pair<Address,Note?>?) {
        _state.update { it.copy(favoriteEntry = favorite) }
    }

    private fun selectEntryToUnwanted(place: Place?) {
        _state.update { it.copy(placeForUnwanted = place) }
    }

    private fun launchFetchEntriesFlow(
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
}