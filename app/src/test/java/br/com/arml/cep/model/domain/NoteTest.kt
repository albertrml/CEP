package br.com.arml.cep.model.domain

import br.com.arml.cep.model.exception.NoteException
import org.junit.Assert.assertThrows
import org.junit.Test

class NoteTest {
    @Test
    fun `should throw EmptyTitleException when title is empty`() {
        assertThrows(NoteException.EmptyTitleException::class.java) {
            Note.build("", "Content")
        }
    }

    @Test
    fun `should throw TitleTooLongException when title is too long`() {
        assertThrows(NoteException.TitleTooLongException::class.java) {
            Note.build("A".repeat(MAX_TITLE_LENGTH + 1), "Content")
        }
    }

    @Test
    fun `should throw TitleTooShortException when title is too short`() {
        assertThrows(NoteException.TitleTooShortException::class.java) {
            Note.build("N", "Content")
        }
        assertThrows(NoteException.TitleTooShortException::class.java) {
            Note.build("No", "Content")
        }
    }

    @Test
    fun `should throw ContentTooLongException when content is too long`(){
        assertThrows(NoteException.ContentTooLongException::class.java) {
            Note.build("Title", "A".repeat(MAX_CONTENT_LENGTH+1))
        }
    }
}