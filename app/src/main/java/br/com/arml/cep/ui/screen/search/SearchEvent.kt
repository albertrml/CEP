package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place

sealed class SearchEvent{
    data class OnSearch(val code: String): SearchEvent()
    data class OnFavorite(val place: Place): SearchEvent()
    data object OnClear: SearchEvent()
}