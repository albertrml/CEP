package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.ui.common.Reducer

sealed class FavoriteEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: String) : FavoriteEffect()
}