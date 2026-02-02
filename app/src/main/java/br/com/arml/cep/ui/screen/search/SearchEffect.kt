package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.ui.common.Reducer

sealed class SearchEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: String) : SearchEffect()
}