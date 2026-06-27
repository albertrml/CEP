package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.utils.UiText

sealed class LogEffect : Reducer.ViewEffect {
    data class ShowSnackbar(val message: UiText) : LogEffect()
}
