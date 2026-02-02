package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.ui.common.Reducer

sealed class LogEffect : Reducer.ViewEffect {
    data class ShowSnackbar(val message: String) : LogEffect()
}
