package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.entity.PlaceEntry

sealed class FavoriteEvent {

    data class OnClickToUnwanted(val placeEntry: PlaceEntry) : FavoriteEvent()
    data class OnFilterByCep(val cep: String) : FavoriteEvent()
    data class OnFilterByTitle(val title: String) : FavoriteEvent()
    data class OnSelectEntryToEdit(val placeEntry: PlaceEntry?) : FavoriteEvent()
    data class OnSelectEntryToUnwanted(val placeEntry: PlaceEntry?) : FavoriteEvent()
    data class OnUpdateFavorite(val placeEntry: PlaceEntry) : FavoriteEvent()
    data object OnExportFavorites : FavoriteEvent()
    data object OnExportHide : FavoriteEvent()
    data object OnExportShow : FavoriteEvent()
    data object OnFetchFavorites : FavoriteEvent()
    data object OnFilterNone : FavoriteEvent()
    data class OnImportBackup(val json: String) : FavoriteEvent()
    data object OnImportHide : FavoriteEvent()
    data object OnImportShow : FavoriteEvent()

}



