package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

data class SearchState(
    val entry: Response<Place> = Response.Loading
): Reducer.ViewState