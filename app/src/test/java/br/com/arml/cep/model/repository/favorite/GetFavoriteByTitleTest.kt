package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.mock.mockPlaceWithNotes
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockFlowAnswer
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

class GetFavoriteByTitleTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun generateDB(size: Int, query: String = "") = mockPlaceWithNotes(size)
        .filter { placeWithNotes ->
            placeWithNotes.notes.any { note ->
                note.title.contains(query)
            }
        }

    private fun mockFavoriteSearchByTitle(
        query: String,
        placeWithNotes: List<PlaceWithNotes> = emptyList(),
        exception: Exception? = null
    ) = mockFlowAnswer(
        { mockFavoriteDao.selectFavoritesByTitle(query) },
        placeWithNotes,
        exception
    )

    /*
     * Function: getFavoritesByTitle(title: String = "")
     * Dependencies:
     *  - From FavoriteDao: selectFavoritesByTitle
     */

    @Test
    fun `getFavoritesByTitle should Emit Success with places when dao returns list of PlaceWithNotes`() =
        runTest {
            val query = "Title"
            val expectedPlaceWithNotesList = generateDB(10,query)
            val expectedPlaces = expectedPlaceWithNotesList.map { it.toModel() }

            mockFavoriteSearchByTitle(
                query = query,
                placeWithNotes = expectedPlaceWithNotesList
            )

            val responses = repository.getFavoritesByTitle(query).toList()

            responses.assertFlowSuccess { places ->
                assertThat(places).isEqualTo(expectedPlaces)
            }
            coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByTitle(query) }
        }

    @Test
    fun `getFavoritesByTitle should Emit Success with empty list when dao does not find any places`() =
        runTest {
            val query = "INVALID QUERY"
            val expectedPlaceWithNotesList = generateDB(5,query)

            mockFavoriteSearchByTitle(
                query = query,
                placeWithNotes = expectedPlaceWithNotesList
            )

            val responses = repository.getFavoritesByTitle(query).toList()

            responses.assertFlowSuccess { places -> assertThat(places).isEmpty() }
            coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByTitle(query) }
        }

    @Test
    fun `getFavoritesByTitle should Emit Failure when dao throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteSearchByTitle(
            query = "",
            exception = expectedException
        )

        val responses = repository.getFavoritesByTitle("").toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByTitle("") }
    }
}