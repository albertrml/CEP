package br.com.arml.cep.model.domain

import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.exception.NoteException

const val MIN_TITLE_LENGTH = 3
const val MAX_TITLE_LENGTH = 30
const val MAX_CONTENT_LENGTH = 300

@ConsistentCopyVisibility
data class Note private constructor(
    val id: Long = 0L,
    val title: String,
    val content: String
){
    private fun isTitleEmpty() {
        if(title.isEmpty()) throw NoteException.EmptyTitleException()
    }

    private fun isTitleTooLong() {
        if(title.length > MAX_TITLE_LENGTH) throw NoteException.TitleTooLongException()
    }

    private fun isTitleTooShort() {
        if(title.length < MIN_TITLE_LENGTH) throw NoteException.TitleTooShortException()
    }

    private fun isContentTooLong() {
        if(content.length > MAX_CONTENT_LENGTH) throw NoteException.ContentTooLongException()
    }

    companion object{
        fun build(id: Long = 0L, title: String, content: String): Note {
            val note = Note(id, title, content)
            note.isTitleEmpty()
            note.isTitleTooLong()
            note.isTitleTooShort()
            note.isContentTooLong()
            return note
        }
    }
}

fun String.isValidTitleNoteSize(): Boolean {
    return this.length in MIN_TITLE_LENGTH..MAX_TITLE_LENGTH
}

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    content = content
)