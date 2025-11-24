package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response

data class SearchState(
    val entry: Response<Place> = Response.Loading,
    val insert: Response<String> = Response.Loading
)