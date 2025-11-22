package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Cep

fun mockCep(index: Int): Cep{
    val builder: StringBuilder = StringBuilder()

    while(builder.length <= 8) builder.append(index.toString())

    val prefix = builder.substring(0,5)
    val suffix = builder.substring(6,9)
    val cepString = "$prefix-$suffix"

    return Cep.build(cepString)
}