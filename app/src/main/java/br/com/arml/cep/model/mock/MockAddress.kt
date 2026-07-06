package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address

const val mockUF = "BR"
const val mockCity = "Cidade"
const val mockStreet = "Rua"
fun mockAddress(index: Int = 0): Address {
    return Address(
        zipCode = mockCep(index).text,
        street = "$mockStreet $index",
        complement = "Complemento $index",
        district = "Bairro $index",
        city = "$mockCity $index",
        state = "Estado $index",
        uf = mockUF,
        region = "Região $index",
        country = "Brasil",
        ddd = "$index".mockFormat(2),
    )
}

val mockAddresses = List(10) { mockAddress(it) }
val mockAddressesDTO = mockAddresses.map{ it.toAddressDTO() }