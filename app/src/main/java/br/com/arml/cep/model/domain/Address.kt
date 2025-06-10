package br.com.arml.cep.model.domain

import br.com.arml.cep.model.dto.AddressDTO

const val DEFAULT_COUNTRY = "Brasil"

data class Address (
    val zipCode: String,
    val street: String,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val uf: String,
    val region: String,
    val country: String = DEFAULT_COUNTRY,
    val ddd: String,
) {
    fun toAddressDTO(): AddressDTO {
        return AddressDTO(
            cep = zipCode,
            logradouro = street,
            complemento = complement,
            bairro = district,
            localidade = city,
            uf = uf,
            estado = state,
            regiao = region,
            ddd = ddd
        )
    }
}

