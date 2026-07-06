package br.com.arml.cep.model.converter

import br.com.arml.cep.model.domain.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteConverterTest {
    @Test
    fun `should convert Note to String and back`(){
        val expectedNote = Note.build(title = "Title", content = "Content")
        val expectedNoteString = "Title||<NOTE_SEP>||Content"
        val actualNoteString = NoteConverter.fromNoteToString(expectedNote)
        val actualNote = NoteConverter.fromStringToNote(expectedNoteString)
        val actualNullNote = NoteConverter.fromStringToNote(null)
        val actualNullNoteString = NoteConverter.fromNoteToString(null)
        assertEquals(expectedNoteString, actualNoteString)
        assertEquals(expectedNote, actualNote)
        assertEquals(null, actualNullNote)
        assertEquals(null, actualNullNoteString)
    }
}