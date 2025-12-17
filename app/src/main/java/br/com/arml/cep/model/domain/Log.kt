package br.com.arml.cep.model.domain

import br.com.arml.cep.model.entity.LogEntity
import java.sql.Timestamp

data class Log(
    val id: Long = 0,
    val cep: Cep,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)

fun Log.toEntity() = LogEntity(
    id = id,
    zipcodePlace = cep.text,
    timestamp = timestamp.time
)