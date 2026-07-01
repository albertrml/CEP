package br.com.arml.cep.model.repository.cache

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.isContentEquals
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdatePlaceTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(CacheRepository(mockCacheDao))
    }

    /*
     * Function: updatePlace(place: Place)
     * Dependencies:
     *  - From CacheDao: updatePlaceEntity
     */

    @Test
    fun `updatePlace should emit Success on successful DAO update`() = runTest {
        val placeToUpdate = mockUnfavoritePlaces.first()
        val expectedPlaceEntity = placeToUpdate.toEntity()

        coEvery { mockCacheDao.updatePlaceEntity(any()) } answers { }

        val responses = repository.updatePlace(placeToUpdate).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) {
            mockCacheDao.updatePlaceEntity(
                match { it.isContentEquals(expectedPlaceEntity) }
            )
        }
    }

    @Test
    fun `updatePlace should emit Failure when DAO throws exception`() = runTest {
        val placeToUpdate = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Update failed")

        coEvery { mockCacheDao.updatePlaceEntity(any()) } throws exception

        val responses = repository.updatePlace(placeToUpdate).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockCacheDao.updatePlaceEntity(any()) }
    }
}