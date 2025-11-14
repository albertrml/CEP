package br.com.arml.cep.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.DEFAULT_COUNTRY
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place

@Entity(
    tableName = "places",
    indices = [
        Index(
            value = [
                "street",
                "district",
                "city",
                "state"
            ]
        )
    ]
)
data class PlaceEntity(
    @PrimaryKey()
    val zipcode: String,
    val street: String,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val uf: String,
    val region: String,
    val country: String = DEFAULT_COUNTRY,
    val ddd: String
)

fun PlaceEntity.toModel(notes: List<Note> = emptyList()): Place {
    val isFavorite = !notes.isEmpty()
    return Place(
        cep = Cep.build(zipcode),
        address = Address(
            zipCode = zipcode,
            street = street,
            complement = complement,
            district = district,
            city = city,
            state = state,
            uf = uf,
            region = region,
            country = country,
            ddd = ddd,
        ),
        isFavorite = Favorite(isFavorite),
        notes = notes
    )
}