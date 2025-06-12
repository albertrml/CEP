package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.entity.PlaceEntry

sealed class FavoriteEvent {
    data object OnFetchFavorites : FavoriteEvent()
    data class OnClickToUnwanted(val placeEntry: PlaceEntry) : FavoriteEvent()
    data class OnFilterByCep(val cep: String) : FavoriteEvent()
    data class OnFilterByTitle(val title: String) : FavoriteEvent()
    data object OnFilterNone : FavoriteEvent()
    data class OnUpdateFavorite(val placeEntry: PlaceEntry) : FavoriteEvent()
    data class OnSelectEntryToUnwanted(val placeEntry: PlaceEntry?) : FavoriteEvent()
    data class OnSelectEntryToEdit(val placeEntry: PlaceEntry?) : FavoriteEvent()
}



