package br.com.arml.cep.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.arml.cep.model.converter.CepConverter
import br.com.arml.cep.model.converter.FavoriteConverter
import br.com.arml.cep.model.converter.NoteConverter
import br.com.arml.cep.model.converter.TimestampConverter
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import java.sql.Timestamp

@Entity(tableName = "cep_search_entries")
@TypeConverters(
    CepConverter::class,
    TimestampConverter::class,
    FavoriteConverter::class,
    NoteConverter::class
)
data class CepSearchEntry(
    @PrimaryKey
    val cep: Cep,

    @Embedded(prefix = "address_")
    val address: Address,

    @ColumnInfo(name= "favorite_status")
    val isFavorite: Favorite = Favorite(),

    @ColumnInfo(name = "search_timestamp")
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis()),

    @ColumnInfo(name = "note_data")
    val note: Note? = null
)