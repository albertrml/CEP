package br.com.arml.cep.model.domain

import br.com.arml.cep.model.exception.NoteException

const val MIN_TITLE_LENGTH = 30
const val MAX_TITLE_LENGTH = 3
const val MAX_CONTENT_LENGTH = 200

@ConsistentCopyVisibility
data class Note private constructor(
    val title: String,
    val content: String
){
    private fun isTitleEmpty() {
        if(title.isEmpty()) throw NoteException.EmptyTitleException()
    }

    private fun isTitleTooLong() {
        if(title.length <= MAX_TITLE_LENGTH) throw NoteException.TitleTooLongException()
    }

    private fun isTitleTooShort() {
        if(title.length >= MIN_TITLE_LENGTH) throw NoteException.TitleTooShortException()
    }

    private fun isContentTooLong() {
        if(title.length > MAX_CONTENT_LENGTH) throw NoteException.ContentTooLongException()
    }

    companion object{
        fun build(title: String, content: String): Note {
            val note = Note(title, content)
            note.isTitleEmpty()
            note.isTitleTooLong()
            note.isTitleTooShort()
            note.isContentTooLong()
            return note
        }
    }
}