package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.exception.CepDatabaseException.IllegalNoteQuantity
import br.com.arml.cep.model.mock.generateMockNotes
import br.com.arml.cep.model.mock.mockPlaceWithNotes
import br.com.arml.cep.model.mock.mockPlaces
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

class DeleteNoteFromFavoriteTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun mockFavoriteCountNotes(
        zipcode: String,
        quantity: Int = 0,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.countNotesEntitiesFromFavorite(zipcode) },
        quantity,
        exception
    )

    private fun mockFavoriteDeleteNote(
        noteEntity: NoteEntity,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.deleteNote(noteEntity) },
        Unit,
        exception
    )

    /*
     * Function: deleteNoteFromFavorite(zipcode: String, note: Note)
     * Dependencies:
     *  - From FavoriteDao: countNotesEntitiesFromFavorite, deleteNote
     */

    @Test
    fun `deleteNoteFromFavorite should emits Success when note count is greater than one`() = runTest {
        val placeWithNotes = mockPlaceWithNotes(5).last()
        val noteQuantity = placeWithNotes.notes.size
        val expectedNoteEntity = placeWithNotes.notes.first()
        val expectedNote = expectedNoteEntity.toModel()
        val zipcode = placeWithNotes.place.zipcode

        mockFavoriteCountNotes(zipcode, noteQuantity)
        mockFavoriteDeleteNote(expectedNoteEntity)

        val responses = repository.deleteNoteFromFavorite(zipcode, expectedNote).toList()

        responses.assertFlowSuccess { zipcode ->
            assertThat(zipcode).isEqualTo(zipcode)
        }
        coVerify(exactly = 1) { mockFavoriteDao.countNotesEntitiesFromFavorite(zipcode) }
        coVerify(exactly = 1) { mockFavoriteDao.deleteNote(expectedNoteEntity) }
    }

    @Test
    fun `deleteNoteFromFavorite should emits Failure when there is only one note`() = runTest {
        val placeWithNotes = PlaceWithNotes(
            place = mockPlaces(1, true).first().toEntity(),
            notes = generateMockNotes(1,1).map { it.toEntity() }
        )
        val noteQuantity = placeWithNotes.notes.size
        val expectedNoteEntity = placeWithNotes.notes.first()
        val expectedNote = expectedNoteEntity.toModel()
        val zipcode = placeWithNotes.place.zipcode
        val expectedException = IllegalNoteQuantity()

        mockFavoriteCountNotes(zipcode, noteQuantity)
        mockFavoriteDeleteNote(expectedNoteEntity)

        val responses = repository.deleteNoteFromFavorite(zipcode, expectedNote).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { mockFavoriteDao.countNotesEntitiesFromFavorite(zipcode) }
        coVerify(exactly = 0) { mockFavoriteDao.deleteNote(expectedNoteEntity) }
    }

    @Test
    fun `deleteNoteFromFavorite should emit Failure when countNotesEntitiesFromFavorite throws Exception`() = runTest {
        val placeWithNotes = mockPlaceWithNotes(5).last()
        val noteQuantity = placeWithNotes.notes.size
        val expectedNoteEntity = placeWithNotes.notes.first()
        val expectedNote = expectedNoteEntity.toModel()
        val zipcode = placeWithNotes.place.zipcode
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteCountNotes(zipcode, noteQuantity, expectedException)
        mockFavoriteDeleteNote(expectedNoteEntity)

        val responses = repository.deleteNoteFromFavorite(zipcode, expectedNote).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException.javaClass)
        }
        coVerify(exactly = 1) { mockFavoriteDao.countNotesEntitiesFromFavorite(zipcode) }
        coVerify(exactly = 0) { mockFavoriteDao.deleteNote(expectedNoteEntity) }
    }

    @Test
    fun `deleteNoteFromFavorite should emit Failure when deleteNote throws Exception`() = runTest {
        val placeWithNotes = mockPlaceWithNotes(5).last()
        val noteQuantity = placeWithNotes.notes.size
        val expectedNoteEntity = placeWithNotes.notes.first()
        val expectedNote = expectedNoteEntity.toModel()
        val zipcode = placeWithNotes.place.zipcode
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteCountNotes(zipcode, noteQuantity)
        mockFavoriteDeleteNote(expectedNoteEntity, expectedException)

        val responses = repository.deleteNoteFromFavorite(zipcode, expectedNote).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException.javaClass)
        }
        coVerify(exactly = 1) { mockFavoriteDao.countNotesEntitiesFromFavorite(zipcode) }
        coVerify(exactly = 1) { mockFavoriteDao.deleteNote(expectedNoteEntity) }
    }
}