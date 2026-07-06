package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.utils.UiText

sealed class SearchEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: UiText) : SearchEffect()
}