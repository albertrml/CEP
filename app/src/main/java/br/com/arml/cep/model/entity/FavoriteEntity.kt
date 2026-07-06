package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "Favorites",
    primaryKeys = ["id_place", "id_note"],
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_place"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_note"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_place"),
        Index("id_note")
    ]
)
data class FavoriteEntity(
    @ColumnInfo(name = "id_place")
    val placeId: Long,
    @ColumnInfo(name = "id_note")
    val idNote: Long
)