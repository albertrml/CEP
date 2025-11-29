package br.com.arml.cep.ui.screen.favorite

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.FavoriteUseCase
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwantedResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmExport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmImport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnDeleteNoteFromFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnDeleteNoteFromFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFetchFavoritesResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByTitle
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterNone
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnUpdateNoteFromFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnUpdateNoteFromFavoriteResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
): BaseViewModel<FavoriteState, FavoriteEvent, FavoriteEffect>(
    initialState = FavoriteState(),
    reducer = FavoriteReducer()
) {

    init { fetchFavorites() }

    fun onEvent(event: FavoriteEvent){
        when(event){
            is OnAddNoteToFavorite -> addNoteToFavorite(event.cep, event.note)
            is OnConfirmFavoriteToUnwanted -> changeFavoriteToUnwanted(event.place)
            is OnDeleteNoteFromFavorite -> deleteNoteFromFavorite(event.noteWithCep)
            is OnUpdateNoteFromFavorite -> editNoteFromFavorite(event.note)
            is OnFilterByCep -> filterByCep(event.cep)
            is OnFilterByTitle -> filterByTitle(event.title)
            is OnFilterNone -> fetchFavorites()
            is OnConfirmImport -> importFavorites(event.json)
            is OnConfirmExport -> exportFavorites()
            else -> sendEventForEffect(event)
        }
    }

    private fun addNoteToFavorite(cep: Cep, note: Note){
        viewModelScope.launch {
            favoriteUseCase.addNoteToFavorite(cep, note).collect { response ->
                sendEventForEffect(OnAddNoteToFavoriteResponse(response))
            }
        }
    }

    private fun changeFavoriteToUnwanted(place: Place) {
        viewModelScope.launch {
            favoriteUseCase.removeFromFavorite(place).collect { response ->
                sendEventForEffect(OnConfirmFavoriteToUnwantedResponse(response))
            }
        }
    }

    private fun deleteNoteFromFavorite(noteWithCep: Pair<Cep, Note>) {
        val (cep, note) = noteWithCep
        viewModelScope.launch {
            favoriteUseCase.deleteNote(cep,note).collect { response ->
                sendEventForEffect(OnDeleteNoteFromFavoriteResponse(response))
            }
        }
    }

    private fun editNoteFromFavorite(note: Note) {
        viewModelScope.launch {
            favoriteUseCase.updateNote(note).collect{ response ->
                sendEventForEffect(OnUpdateNoteFromFavoriteResponse(response))
            }
        }
    }

    private fun exportFavorites(){
        viewModelScope.launch {
            favoriteUseCase.exportFavorites().collect { response ->
                sendEventForEffect(FavoriteEvent.OnConfirmExportResponse(response))
            }
        }
    }

    private fun fetchFavorites(){
        viewModelScope.launch {
            favoriteUseCase.fetchFavorites().collectLatest { response ->
                sendEvent(OnFetchFavoritesResponse(response))
            }
        }
    }

    private fun filterByCep(cep: String) {
        viewModelScope.launch {
            favoriteUseCase.filterByCep(cep).collectLatest { response ->
                sendEvent(OnFetchFavoritesResponse(response))
            }
        }
    }

    private fun filterByTitle(title: String) {
        viewModelScope.launch {
            favoriteUseCase.filterByTitle(title).collectLatest { response ->
                sendEvent(OnFetchFavoritesResponse(response))
            }
        }
    }

    private fun importFavorites(json: String){
        viewModelScope.launch {
            favoriteUseCase.importFavorites(json).collect { response ->
                sendEventForEffect(FavoriteEvent.OnConfirmImportResponse(response))
            }
        }
    }
}