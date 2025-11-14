package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Cep

fun mockCep(index: Int): Cep{
    return Cep.build("$index".mockFormat(8))
}