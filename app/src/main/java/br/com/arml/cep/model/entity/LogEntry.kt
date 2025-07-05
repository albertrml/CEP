package br.com.arml.cep.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.arml.cep.model.converter.CepConverter
import br.com.arml.cep.model.converter.TimestampConverter
import br.com.arml.cep.model.domain.Cep
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Entity(
    tableName = "log_table",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntry::class,
            parentColumns = ["cep"],
            childColumns = ["cep"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cep"])]
)
@TypeConverters(
    CepConverter::class,
    TimestampConverter::class
)
data class LogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cep: Cep,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)

private fun Timestamp.toFormatted(pattern: String): String{
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    //formatter.timeZone = TimeZone.getTimeZone("UTC")
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(this)
}

fun Timestamp.toFormattedUTC(): String{
    //return this.toFormatted("yyyy-MM-dd HH:mm XXX")
    return this.toFormatted("yyyy-MM-dd HH:mm")
}

fun Long.toFormattedBR(): String{
    return Timestamp(this).toFormatted("dd/MM/yyyy")
}