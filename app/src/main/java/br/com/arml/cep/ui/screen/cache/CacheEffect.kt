package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.ui.common.Reducer

sealed class CacheEffect: Reducer.ViewEffect {
    data class ShowSnackbar(val message: String): CacheEffect()
}