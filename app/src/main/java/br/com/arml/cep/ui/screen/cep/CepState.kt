package br.com.arml.cep.ui.screen.cep

import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.domain.Response

data class CepState(
    val entry: Response<PlaceEntry> = Response.Loading
)