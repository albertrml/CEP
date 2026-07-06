package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.DEFAULT_COUNTRY
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.utils.normalizeForDBSearch

@Entity(
    tableName = "Places",
    indices = [
        Index(
            name = "index_places_zipcode",
            value = ["zipcode"],
            unique = true
        ),
        Index(
            name = "index_places_street_search",
            value = ["street_search"]
        ),
        Index(
            name = "index_places_city_search",
            value = ["city_search"]
        ),
        Index(
            name = "index_places_search",
            value = [
                "street_search",
                "district",
                "city_search",
                "state"
            ]
        )
    ]
)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true, )
    val id: Long = 0,
    val zipcode: String,
    val street: String,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val uf: String,
    val region: String,
    val country: String = DEFAULT_COUNTRY,
    val ddd: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "street_search")
    val streetSearch: String = street.normalizeForDBSearch(),
    @ColumnInfo(name = "city_search")
    val citySearch: String = city.normalizeForDBSearch()
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

fun PlaceEntity.isContentEquals(other: PlaceEntity) =
            zipcode == other.zipcode
            && street == other.street
            && complement == other.complement
            && district == other.district
            && city == other.city
            && state == other.state
            && uf == other.uf
            && region == other.region
            && country == other.country
            && ddd == other.ddd
