package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.mock.mockCep
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

class AddToFavoriteTest {
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

    private fun mockFavoriteCheckTitle(
        query: String,
        exists: Boolean = false,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.doesTitleExist(query) },
        exists,
        exception
    )

    private fun mockFavoriteInsertNote(
        zipcode: String,
        note: NoteEntity = NoteEntity(),
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.insertNoteEntityToFavorite(zipcode,note) },
        Unit,
        exception
    )

    /*
     * Function: addToFavorite(zipcode: String, note: Note)
     * Dependencies:
     *  - From FavoriteDao: countNotesEntitiesFromFavorite, insertNoteEntityToFavorite
     */

    @Test
    fun `addToFavorite should Emit Success when dao call is successful`() = runTest {
        val expectedZipcode = mockCep(1).text
        val note = Note.build(
            title = "Test Note",
            content = "Test Content"
        )
        val expectedNoteEntity = note
            .toEntity()
            .copy(title = note.title)

        mockFavoriteCountNotes(expectedZipcode, 0)
        mockFavoriteCheckTitle(note.title, exists = false)
        mockFavoriteInsertNote(expectedZipcode, expectedNoteEntity)

        val responses = repository.addToFavorite(expectedZipcode, note).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        with(mockFavoriteDao){
            coVerify(exactly = 1) { countNotesEntitiesFromFavorite(expectedZipcode) }
            coVerify(exactly = 1) { doesTitleExist(note.title) }
            coVerify(exactly = 1) {
                insertNoteEntityToFavorite(expectedZipcode, expectedNoteEntity)
            }
        }
    }

    @Test
    fun `addToFavorite should treat title conflict and emits Success when title note already exists`() = runTest {
        val expectedZipcode = mockCep(1).text
        val note = Note.build(
            title = "Test Note",
            content = "Test Content"
        )
        val quantity = 5
        val suggestedTitles = List(quantity){
            val id = it + 1
            if (id > 1)
                "Test Note ($id)"
            else
                "Test Note"
        }
        val expectedNoteEntity = note
            .toEntity()
            .copy(title = "${note.title} ($quantity)")

        mockFavoriteCountNotes(expectedZipcode, 1)
        suggestedTitles.forEachIndexed { index, title ->
            if(index == quantity - 1)
                mockFavoriteCheckTitle(title, exists = false)
            else
                mockFavoriteCheckTitle(title, exists = true)
        }
        mockFavoriteInsertNote(expectedZipcode, expectedNoteEntity)

        val responses = repository.addToFavorite(expectedZipcode, note).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        with(mockFavoriteDao){
            coVerify(exactly = 1) { countNotesEntitiesFromFavorite(expectedZipcode) }
            suggestedTitles.forEach { title -> coVerify(exactly = 1) { doesTitleExist(title) } }
            coVerify(exactly = 1) {
                insertNoteEntityToFavorite(expectedZipcode, expectedNoteEntity)
            }
        }
    }

    @Test
    fun `addToFavorite should Emit Failure when CountNotesEntitiesFromFavorite throws Exception`() = runTest {
        val note = mockNotes.first()
        val expectedZipcode = mockCep(1).toString()
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteCountNotes(expectedZipcode, exception = expectedException)

        val responses = repository.addToFavorite(expectedZipcode, note).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        with(mockFavoriteDao){
            coVerify(exactly = 1) { countNotesEntitiesFromFavorite(expectedZipcode) }
            coVerify(exactly = 0) { doesTitleExist(note.title) }
            coVerify(exactly = 0) { insertNoteEntityToFavorite(expectedZipcode, any()) }
        }
    }

    @Test
    fun `addToFavorite should Emit Failure when doesTitleExist throws Exception`() = runTest {
        val note = mockNotes.first()
        val expectedZipcode = mockCep(1).toString()
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteCountNotes(expectedZipcode)
        mockFavoriteCheckTitle(note.title, exception = expectedException)

        val responses = repository.addToFavorite(expectedZipcode, note).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        with(mockFavoriteDao){
            coVerify(exactly = 1) { countNotesEntitiesFromFavorite(expectedZipcode) }
            coVerify(exactly = 1) { doesTitleExist(note.title) }
            coVerify(exactly = 0) { insertNoteEntityToFavorite(expectedZipcode, any()) }
        }
    }

    @Test
    fun `addToFavorite should Emit Failure when insertNoteEntityToFavorite throws Exception`() = runTest {
        val expectedZipcode = mockCep(1).text
        val note = Note.build(
            title = "Test Note",
            content = "Test Content"
        )
        val expectedNoteEntity = note.toEntity()
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteCountNotes(expectedZipcode)
        mockFavoriteCheckTitle(note.title, false)
        mockFavoriteInsertNote(
            zipcode = expectedZipcode,
            note = expectedNoteEntity,
            exception = expectedException
        )

        val responses = repository.addToFavorite(expectedZipcode, note).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        with(mockFavoriteDao){
            coVerify(exactly = 1) { countNotesEntitiesFromFavorite(expectedZipcode) }
            coVerify(exactly = 1) { doesTitleExist(note.title) }
            coVerify(exactly = 1) { insertNoteEntityToFavorite(expectedZipcode, expectedNoteEntity) }
        }
    }
}