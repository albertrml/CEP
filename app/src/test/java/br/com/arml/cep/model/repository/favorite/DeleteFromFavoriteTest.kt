package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.mock.mockPlaceWithNotes
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

class DeleteFromFavoriteTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun mockFavoriteDeleteFromFavorite(
        zipcode: String,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.deleteFromFavorite(zipcode) },
        Unit,
        exception
    )

    /*
     * Function: deleteFromFavorite(zipcode: String)
     * Dependencies:
     *  - From FavoriteDao: deleteFromFavorite
     */

    @Test
    fun `deleteFromFavorite should Emit Success when dao call is successful`() = runTest {
        val place = mockPlaceWithNotes(1).first().place
        val expectedZipcode = place.zipcode

        mockFavoriteDeleteFromFavorite(expectedZipcode)

        val responses = repository.deleteFromFavorite(expectedZipcode).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { mockFavoriteDao.deleteFromFavorite(expectedZipcode) }
    }

    @Test
    fun `deleteFromFavorite should Emit Failure when dao throws Exception`() = runTest {
        val place = mockPlaceWithNotes(1).first().place
        val expectedZipcode = place.zipcode
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteDeleteFromFavorite(expectedZipcode, expectedException)

        val responses = repository.deleteFromFavorite(place.zipcode).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException.javaClass)
        }
        coVerify(exactly = 1) { mockFavoriteDao.deleteFromFavorite(expectedZipcode) }
    }
}