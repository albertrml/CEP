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

class GetFavoriteByZipcode {
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
            placeWithNotes.place.zipcode.contains(query)
        }

    private fun mockFavoriteSearchByZipcode(
        zipcode: String,
        placeWithNotes: List<PlaceWithNotes> = emptyList(),
        exception: Exception? = null
    ) = mockFlowAnswer(
        { mockFavoriteDao.selectFavoritesByZipcode(zipcode) },
        placeWithNotes,
        exception
    )

    /*
    * Function: getFavoritesByZipcode(zipcode: String)
    * Dependencies:
    *  - From FavoriteDao: selectFavoritesByZipcode
    */

    @Test
    fun `getFavoritesByZipcode should emits Success when query matched with all zipcode`() =
        runTest {
            val query = ""
            val expectedPlaceWithNotes = generateDB(5)
            val expectedPlaces = expectedPlaceWithNotes.map { it.toModel() }

            mockFavoriteSearchByZipcode(
                zipcode = query,
                placeWithNotes = expectedPlaceWithNotes
            )

            val responses = repository.getFavoritesByZipcode(query).toList()

            responses.assertFlowSuccess { places ->
                assertThat(places).isEqualTo(expectedPlaces)
            }
            coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByZipcode(query) }
        }

    @Test
    fun `getFavoritesByZipcode should emits Success with places when query matched with some zipcode`() =
        runTest {
            val query = "111"
            val expectedPlaceWithNotes = generateDB(5)
            val expectedPlaces = expectedPlaceWithNotes.map { it.toModel() }

            mockFavoriteSearchByZipcode(
                zipcode = query,
                placeWithNotes = expectedPlaceWithNotes
            )

            val responses = repository.getFavoritesByZipcode(query).toList()

            responses.assertFlowSuccess { places ->
                assertThat(places).isEqualTo(expectedPlaces)
            }
            coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByZipcode(query) }
        }

    @Test
    fun `getFavoritesByZipcode should Emit Success with empty list when dao returns an empty list`() =
        runTest {
            val query = "INVALID QUERY"
            val expectedPlaceWithNotes = generateDB(5, query)

            mockFavoriteSearchByZipcode(
                zipcode = query,
                placeWithNotes = expectedPlaceWithNotes
            )

            val responses = repository.getFavoritesByZipcode(query).toList()

            responses.assertFlowSuccess { places -> assertThat(places).isEmpty() }
            coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByZipcode(query) }
        }

    @Test
    fun `getFavoritesByZipcode should Emit Failure when dao throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteSearchByZipcode(
            zipcode = "",
            exception = expectedException
        )

        val responses = repository.getFavoritesByZipcode("").toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isEqualTo(expectedException)
        }
        coVerify(exactly = 1) { mockFavoriteDao.selectFavoritesByZipcode("") }
    }
}