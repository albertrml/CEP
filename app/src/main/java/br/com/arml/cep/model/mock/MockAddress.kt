package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address

fun mockAddress(index: Int = 0): Address {
    return Address(
        zipCode = mockCep(index).text,
        street = "Rua $index",
        complement = "Complemento $index",
        district = "Bairro $index",
        city = "Cidade $index",
        state = "Estado $index",
        uf = "BR",
        region = "Região $index",
        country = "Brasil",
        ddd = "$index".mockFormat(2),
    )
}