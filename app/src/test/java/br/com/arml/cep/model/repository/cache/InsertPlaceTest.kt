package br.com.arml.cep.model.repository.cache

import android.database.sqlite.SQLiteConstraintException
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

class InsertPlaceTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(CacheRepository(mockCacheDao))
    }

    /*
     * Function: insertPlace(place: Place)
     * Dependencies:
     *  - From CacheDao: insertPlaceEntity
     */

    @Test
    fun `insertPlace should emit Success on successful DAO insertion`() = runTest {
        val expectedPlace = mockUnfavoritePlaces.first()
        val expectedPlaceEntity = expectedPlace.toEntity()
        val previousId = expectedPlaceEntity.id
        val expectedId = previousId + 1

        coEvery { mockCacheDao.insertPlaceEntity(any()) } answers { expectedId }

        val responses = repository.insertPlace(expectedPlace).toList()

        responses.assertFlowSuccess { actualId ->
            assertThat(actualId).isNotEqualTo(previousId)
        }
        coVerify(exactly = 1) {
            mockCacheDao.insertPlaceEntity(
                match { it.isContentEquals(expectedPlaceEntity) }
            )
        }
    }

    @Test
    fun `insertPlace should emit Failure when DAO throws exception`() = runTest {
        val expectedPlace = mockUnfavoritePlaces.first()
        val exception = SQLiteConstraintException("Primary key conflict")

        coEvery { mockCacheDao.insertPlaceEntity(any()) } throws exception

        val responses = repository.insertPlace(expectedPlace).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockCacheDao.insertPlaceEntity(any()) }
    }
}