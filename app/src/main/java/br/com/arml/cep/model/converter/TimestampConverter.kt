package br.com.arml.cep.model.converter

import androidx.room.TypeConverter
import java.sql.Timestamp

object TimestampConverter {
    @TypeConverter
    fun fromTimestampToLong(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }

    @TypeConverter
    fun fromLongToTimestamp(time: Long?): Timestamp? {
        return time?.let { Timestamp(it) }
    }

}