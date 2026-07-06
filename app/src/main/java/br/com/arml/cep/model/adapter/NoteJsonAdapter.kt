package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.exception.AdapterException
import br.com.arml.cep.model.exception.CONTENT_TOO_LONG
import br.com.arml.cep.model.exception.NoteException
import br.com.arml.cep.model.exception.TITLE_TOO_LONG
import br.com.arml.cep.model.exception.TITLE_TOO_SHORT
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class NoteJson(val id: Long = 0L, val title: String, val content: String)

class NoteJsonAdapter {
    @ToJson
    fun toJson(note: Note): NoteJson {
        return NoteJson(note.id, note.title, note.content)
    }

    @FromJson
    fun fromJson(note: NoteJson): Note {
        return try { Note.build(note.id, note.title, note.content) }
        catch (_: NoteException.TitleTooShortException) {
            throw AdapterException.InputDoesNotAttendNoteRequirementsException(TITLE_TOO_SHORT)
        }
        catch (_: NoteException.TitleTooLongException) {
            throw AdapterException.InputDoesNotAttendNoteRequirementsException(TITLE_TOO_LONG)
        }
        catch (_: NoteException.ContentTooLongException) {
            throw AdapterException.InputDoesNotAttendNoteRequirementsException(CONTENT_TOO_LONG)
        }
        catch (e: Exception) { throw AdapterException.UnknownException(e) }
    }
}