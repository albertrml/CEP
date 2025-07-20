package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.entity.PlaceEntry

sealed class SearchEvent{
    data class OnSearch(val code: String): SearchEvent()
    data class OnFavorite(val placeEntry: PlaceEntry): SearchEvent()
    data object OnClear: SearchEvent()
}