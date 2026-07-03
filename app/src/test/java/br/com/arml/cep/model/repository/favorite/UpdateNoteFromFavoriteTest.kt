package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockAnswer
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateNoteFromFavoriteTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun mockFavoriteUpdateNote(
        noteEntity: NoteEntity,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.updateNote(noteEntity) },
        Unit,
        exception
    )

    /*
    * Function: updateNoteFromFavorite(note: Note)
    * Dependencies:
    *  - From FavoriteDao: updateNote
    */

    @Test
    fun `updateNoteFromFavorite should Emit Success when dao call is successful`() = runTest {
        val expectedNote = mockNotes.first()
        val expectedNoteEntity = expectedNote.toEntity()

        mockFavoriteUpdateNote(expectedNoteEntity)

        val responses = repository.updateNoteFromFavorite(expectedNote).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { mockFavoriteDao.updateNote(expectedNoteEntity) }
    }

    @Test
    fun `updateNoteFromFavorite should Emit Failure when dao throws Exception`() = runTest {
        val expectedNote = mockNotes.first()
        val expectedNoteEntity = expectedNote.toEntity()
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteUpdateNote(expectedNoteEntity, expectedException)

        val responses = repository.updateNoteFromFavorite(expectedNote).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { mockFavoriteDao.updateNote(expectedNoteEntity) }
    }
}