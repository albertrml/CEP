package br.com.arml.cep.model.entity.dto

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.toModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressDTO(
    val cep : String? = null,
    val logradouro: String? = null,
    val complemento: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val estado: String? = null,
    val regiao: String? = null,
    val ddd: String? = null,
    val erro: String? = null
) {
    fun toAddress(): Address {
        return Address(
            zipCode = cep ?: "",
            street = logradouro ?: "",
            complement = complemento ?: "",
            district = bairro ?: "",
            city = localidade ?: "",
            state = estado ?: "",
            region = regiao ?: "",
            ddd = ddd ?: "",
            uf = uf ?: ""
        )
    }

    fun toPlaceEntity(): PlaceEntity {
        return PlaceEntity(
            zipcode = cep ?: "",
            street = logradouro ?: "",
            complement = complemento ?: "",
            district = bairro ?: "",
            city = localidade ?: "",
            state = estado ?: "",
            region = regiao ?: "",
            ddd = ddd ?: "",
            uf = uf ?: ""
        )
    }

    fun toPlace(): Place = toPlaceEntity().toModel()
}