package br.com.arml.cep.ui.screen.favorite

import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.FavoriteUseCase
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.common.BaseViewModel
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmExport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwantedResponse
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    initialState: FavoriteState,
    reducer: FavoriteReducer
) : BaseViewModel<FavoriteState, FavoriteEvent, FavoriteEffect>(
    initialState = initialState,
    reducer = reducer
) {
    private sealed class FavoriteFilter {
        data object None : FavoriteFilter()
        data class ByCep(val cep: String) : FavoriteFilter()
        data class ByTitle(val title: String) : FavoriteFilter()
    }

    private val _filter = MutableStateFlow<FavoriteFilter>(FavoriteFilter.None)

    init { fetchFavorites() }

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is OnAddNoteToFavorite -> addNoteToFavorite(event.cep, event.note)
            is OnConfirmFavoriteToUnwanted -> changeFavoriteToUnwanted(event.place)
            is OnDeleteNoteFromFavorite -> deleteNoteFromFavorite(event.noteWithCep)
            is OnUpdateNoteFromFavorite -> editNoteFromFavorite(event.note)
            is OnFilterByCep -> _filter.update { FavoriteFilter.ByCep(event.cep) }
            is OnFilterByTitle -> _filter.update { FavoriteFilter.ByTitle(event.title) }
            is OnFilterNone -> _filter.update { FavoriteFilter.None }
            is OnConfirmImport -> importFavorites(event.json)
            is OnConfirmExport -> exportFavorites()
            else -> sendEventForEffect(event)
        }
    }

    private fun addNoteToFavorite(cep: Cep, note: Note) {
        collectAction(
            flow = favoriteUseCase.addNoteToFavorite(cep, note),
            onResponse = { response -> OnAddNoteToFavoriteResponse(response, cep.text) }
        )
    }

    private fun changeFavoriteToUnwanted(place: Place) {
        collectAction(
            flow = favoriteUseCase.removeFromFavorite(place),
            onResponse = { response -> OnConfirmFavoriteToUnwantedResponse(response) }
        )
    }

    private fun deleteNoteFromFavorite(noteWithCep: Pair<Cep, Note>) {
        val (cep, note) = noteWithCep
        collectAction(
            flow = favoriteUseCase.deleteNote(cep, note),
            onResponse = { response -> OnDeleteNoteFromFavoriteResponse(response) }
        )
    }

    private fun editNoteFromFavorite(note: Note) {
        collectAction(
            flow = favoriteUseCase.updateNote(note),
            onResponse = { response -> OnUpdateNoteFromFavoriteResponse(response) }
        )
    }

    private fun exportFavorites() {
        collectAction(
            flow = favoriteUseCase.exportFavorites(),
            onResponse = { response -> FavoriteEvent.OnConfirmExportResponse(response) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchFavorites() {
        _filter
            .flatMapLatest { filter ->
                when (filter) {
                    is FavoriteFilter.None -> favoriteUseCase.fetchFavorites()
                    is FavoriteFilter.ByCep -> favoriteUseCase.filterByCep(filter.cep)
                    is FavoriteFilter.ByTitle -> favoriteUseCase.filterByTitle(filter.title)
                }
            }
            .onEach { response -> sendEventForEffect(OnFetchFavoritesResponse(response)) }
            .launchIn(viewModelScope)
    }

    private fun importFavorites(json: String) {
        collectAction(
            flow = favoriteUseCase.importFavorites(json),
            onResponse = { FavoriteEvent.OnConfirmImportResponse(it) }
        )
    }
}