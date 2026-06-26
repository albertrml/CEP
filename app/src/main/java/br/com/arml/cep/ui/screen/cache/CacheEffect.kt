package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.utils.UiText

sealed class CacheEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: UiText): CacheEffect()
}