package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "Logs",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = [ "id" ],
            childColumns = [ "id_place" ],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_place"]),
        Index(value = ["timestamp"])
    ]
)
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "id_place")
    val placeId: Long,
    val timestamp: Long = System.currentTimeMillis(),
)