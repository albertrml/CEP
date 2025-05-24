package br.com.arml.cep.ui.screen.cep

import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.util.type.Response

data class CepState(
    val address: Response<Address> = Response.Loading
)