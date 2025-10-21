package br.com.arml.cep.model.converter

import androidx.room.TypeConverter
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.exception.NoteException

object NoteConverter {
    private const val DELIMITER = "||<NOTE_SEP>||"

    @TypeConverter
    fun fromNoteToString(note: Note?): String? {
        return note?.let { "${it.title}$DELIMITER${it.content}" }
    }

    @TypeConverter
    fun fromStringToNote(value: String?): Note? {
        return value?.let {
            val parts = it.split(DELIMITER, limit = 2)
            if (parts.size == 2) {
                Note.build(parts[0], parts[1])
            } else {
                throw NoteException.ConversionRoomNoteException(
                    value = value,
                    errorMsg = "String de Note mal estruturada"
                )
            }
        }
    }
}