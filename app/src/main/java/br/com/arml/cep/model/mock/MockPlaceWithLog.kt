package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.relation.PlaceWithLog

fun mockPlacesWithLog(size: Int): List<PlaceWithLog> = List(size){ i ->
    val id = (i + 1).toLong()
    PlaceWithLog(
        place = Place(
            cep = mockCep(id.toInt()),
            address = mockAddress(id.toInt()),
            isFavorite = Favorite(false),
            notes = emptyList()
        ).toEntity().copy(id = id),
        log = LogEntity(
            id = id,
            placeId = id,
            timestamp = getMockDate(id.toInt()).time
        )
    )
}