package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.cep.ui.common.Reducer

data class SearchState(
    val cepSearchResponse: Response<Place> = Response.Loading,
    val addressSearchResponse: Response<List<Place>> = Response.Loading,
    val selectedPlace: Place? = null
): Reducer.ViewState