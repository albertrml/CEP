package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.exception.AdapterException.InputDoesNotAttendNoteRequirementsException
import br.com.arml.cep.model.mock.mockNotes
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class NoteAdapterTest {
    @Test
    fun `toJson should return valid NoteJson from Note`(){
        val note = mockNotes.first()
        val expectedNoteJson = with(note) {
             NoteJson(id = id, title = title, content = content)
        }

        val actualNoteJson = NoteJsonAdapter().toJson(note)

        assertThat(expectedNoteJson).isEqualTo(actualNoteJson)
    }

    @Test
    fun `fromJson should return valid Note from NoteJson`(){
        val expectedNote = mockNotes.first()
        val noteJson = with(expectedNote) {
            NoteJson(id = id, title = title, content = content)
        }

        val actualNote = NoteJsonAdapter().fromJson(noteJson)

        assertThat(expectedNote).isEqualTo(actualNote)
    }

    @Test
    fun `fromJson should throws NoteAdapterException when NoteJson is does not attend the requirements`(){
        val invalidNoteJson = listOf(
            NoteJson(id = 0L, title = "", content = ""),
            NoteJson(id = 0L, title = "a".repeat(MIN_TITLE_LENGTH - 1), content = ""),
            NoteJson(
                id = 0L,
                title = "a".repeat(MIN_TITLE_LENGTH - 1),
                content = "a".repeat(MAX_CONTENT_LENGTH + 1)
            )
        )

        invalidNoteJson.forEach { input ->
            assertThrows(InputDoesNotAttendNoteRequirementsException::class.java){
                NoteJsonAdapter().fromJson(input)
            }
        }
    }
}