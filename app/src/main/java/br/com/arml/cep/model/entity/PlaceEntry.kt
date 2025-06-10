package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.arml.cep.model.converter.CepConverter
import br.com.arml.cep.model.converter.FavoriteConverter
import br.com.arml.cep.model.converter.NoteConverter
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note

@Entity(tableName = "place_table")
@TypeConverters(
    CepConverter::class,
    FavoriteConverter::class,
    NoteConverter::class
)
data class PlaceEntry(
    @PrimaryKey
    val cep: Cep,
    @Embedded(prefix = "address_")
    val address: Address,
    @ColumnInfo(name = "favorite_status")
    val isFavorite: Favorite = Favorite(),
    val note: Note? = null
)
