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

class ExportFavoritesTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun mockFavoriteExport(
        data: List<PlaceWithNotes> = emptyList(),
        exception: Exception? = null
    ) = mockFlowAnswer(
        { mockFavoriteDao.exportFavorites() },
        data,
        exception
    )

    /*
     * Function: exportFavorites()
     * Dependencies:
     *  - From FavoriteDao: exportFavorites
     */

    @Test
    fun `exportFavorites should emits Success with data when dao returns a list`() = runTest {
        val data = mockPlaceWithNotes(5)
        val expected = data.map { it.toModel() }

        mockFavoriteExport(data)

        val responses = repository.exportFavorites().toList()

        responses.assertFlowSuccess {
            assertThat(it).isEqualTo(expected)
        }
        coVerify(exactly = 1) { mockFavoriteDao.exportFavorites() }
    }

    @Test
    fun `exportFavorites should emits Failure when exportFavorite throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")

        mockFavoriteExport(exception = expectedException)

        val responses = repository.exportFavorites().toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { mockFavoriteDao.exportFavorites() }
    }
}