package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Log
import java.sql.Timestamp

@Entity(
    tableName = "Logs",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = [ "zipcode" ],
            childColumns = [ "zipcode_place" ],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["zipcode_place"]),
        Index(value = ["timestamp"])
    ]
)
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "zipcode_place")
    val zipcodePlace: String,
    val timestamp: Long = System.currentTimeMillis(),
)

fun LogEntity.toModel() = Log(
    cep = Cep.build(zipcodePlace),
    timestamp = Timestamp(timestamp)
)