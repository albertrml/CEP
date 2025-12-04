package br.com.arml.cep.model.domain

import androidx.compose.runtime.Composable
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.dto.AddressDTO

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

@Composable
fun Address.mapOfFields() = mapOf(
    R.string.display_zipcode_field to zipCode,
    R.string.display_street_field to street,
    R.string.display_complement_field to complement,
    R.string.display_neighborhood_field to district,
    R.string.display_city_field to city,
    R.string.display_state_field to state,
    R.string.display_uf_field to uf,
    R.string.display_region_field to region,
    R.string.display_country_field to country,
    R.string.display_ddd_field to ddd
)