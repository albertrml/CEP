package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.utils.UiText

sealed class FavoriteEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: UiText) : FavoriteEffect()
    data class OnSuccessExportFavorites(val message: String) : FavoriteEffect()
}