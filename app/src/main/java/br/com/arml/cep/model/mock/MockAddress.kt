package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address

val mockAddress = Address(
    zipCode = "89042299",
    street = "Rua Johann Ohf",
    complement = "até 1761 - lado ímpar",
    district = "Água Verde",
    city = "Blumenau",
    state = "Santa Catarina",
    region = "Sul",
    uf = "SC",
    ddd = "47"
)

fun mockAddress(index: Int = 0): Address {
    return Address(
        zipCode = "${index}".mockFormat(5) + "-" + "${index}".mockFormat(3),
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