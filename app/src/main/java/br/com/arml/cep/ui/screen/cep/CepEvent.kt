package br.com.arml.cep.ui.screen.cep

import br.com.arml.cep.model.entity.PlaceEntry

sealed class CepEvent{
    data class SearchCep(val code: String): CepEvent()
    data class FavoriteCep(val placeEntry: PlaceEntry): CepEvent()
    object ClearCep: CepEvent()
}