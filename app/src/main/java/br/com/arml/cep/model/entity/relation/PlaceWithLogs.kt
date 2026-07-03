package br.com.arml.cep.model.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.arml.cep.model.domain.Cep.Companion.build
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.PlaceEntity
import java.sql.Timestamp

data class PlaceWithLog(
    @Embedded val log: LogEntity,
    @Relation(
        parentColumn = "id_place",
        entityColumn = "id"
    )
    val place: PlaceEntity
)

fun PlaceWithLog.toModel() = Log(
    id = log.id,
    placeId = log.placeId,
    cep = build(place.zipcode),
    timestamp = Timestamp(log.timestamp)
)
