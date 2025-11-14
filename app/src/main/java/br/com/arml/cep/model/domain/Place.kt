package br.com.arml.cep.model.domain

import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes

data class Place(
    val cep: Cep,
    val address: Address,
    val isFavorite: Favorite = Favorite(),
    val notes: List<Note> = emptyList()
)

fun Place.toEntity() = PlaceEntity(
    zipcode = cep.text,
    street = address.street,
    complement = address.complement,
    district = address.district,
    city = address.city,
    state = address.state,
    uf = address.uf,
    region = address.region,
    country = address.country,
    ddd = address.ddd
)

fun Place.toPlaceWithNotes() = PlaceWithNotes(
    place = toEntity(),
    notes = notes.map { it.toEntity() }
)