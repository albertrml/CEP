package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "Favorites",
    primaryKeys = ["zipcode_place", "id_note"],
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["zipcode"],
            childColumns = ["zipcode_place"],
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
        Index("zipcode_place"),
        Index("id_note")
    ]
)
data class FavoriteEntity(
    @ColumnInfo(name = "zipcode_place")
    val zipcodePlace: String,
    @ColumnInfo(name = "id_note")
    val idNote: Long
)