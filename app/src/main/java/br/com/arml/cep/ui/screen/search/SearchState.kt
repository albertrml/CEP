package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.domain.Response

data class SearchState(
    val entry: Response<PlaceEntry> = Response.Loading,
    val insert: Response<Unit> = Response.Loading
)