package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.ui.common.Reducer

data class CacheState(
    val selectedPlace: Place? = null,
    val places: Response<List<Place>> = Loading
) : Reducer.ViewState