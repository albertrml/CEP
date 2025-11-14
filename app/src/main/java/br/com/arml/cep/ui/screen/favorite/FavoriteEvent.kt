package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place

sealed class FavoriteEvent {
    /** Create **/
    data class OnAddNoteToFavorite(val cep: Cep, val note: Note) : FavoriteEvent()

    /** Read **/
    data object OnFetchFavorites : FavoriteEvent()
    data class OnFilterByCep(val cep: String) : FavoriteEvent()
    data class OnFilterByTitle(val title: String) : FavoriteEvent()
    data object OnFilterNone : FavoriteEvent()

    /** Update **/
    data class OnSelectNoteEntry(val note: Note?) : FavoriteEvent()
    data class OnEditNoteFromFavorite(val note: Note) : FavoriteEvent()
    data class OnSelectFavoriteEntry(val favorite: Pair<Address, Note?>?) : FavoriteEvent()

    /** Delete **/
    data class OnSelectEntryToUnwanted(val place: Place?) : FavoriteEvent()
    data class OnClickToUnwanted(val place: Place) : FavoriteEvent()
    data class OnDeleteNoteFromFavorite(val noteWithCep: Pair<Cep, Note>) : FavoriteEvent()

    /** Export **/
    data object OnExportFavorites : FavoriteEvent()
    data object OnExportHide : FavoriteEvent()
    data object OnExportShow : FavoriteEvent()

    /** Import **/
    data class OnImportBackup(val json: String) : FavoriteEvent()
    data object OnImportHide : FavoriteEvent()
    data object OnImportShow : FavoriteEvent()
}