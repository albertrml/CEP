package br.com.arml.cep.ui.screen.cep

sealed class CepEvent{
    data class SearchCep(val code: String): CepEvent()
    object ClearCep: CepEvent()
}