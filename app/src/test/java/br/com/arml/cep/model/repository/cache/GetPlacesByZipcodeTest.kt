package br.com.arml.cep.model.repository.cache

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.source.local.CacheDao
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

class GetPlacesByZipcodeTest {

    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(CacheRepository(mockCacheDao))
    }

    private fun mockCacheSearch(
        query: String,
        expectedData: List<PlaceEntity>,
        exception: Exception? = null
    ) = mockFlowAnswer(
        { mockCacheDao.selectCachedPlaceEntitiesByZipcode(query) },
        expectedData,
        exception
    )

    /*
     * Function: getPlacesByZipcode(query: String)
     * Dependencies:
     *  - From CacheDao: selectCachedPlaceEntitiesByZipcode
     */

    @Test
    fun `getPlacesByZipcode should emit Success with data`() = runTest {
        val query = "111"
        val mockExpectedPlaceEntities = mockUnfavoritePlaceEntities
            .map { it.place }
            .filter { it.zipcode.contains(query) }
        val expectedPlaces = mockExpectedPlaceEntities.map { it.toModel() }

        mockCacheSearch(query, mockExpectedPlaceEntities)

        val responses = repository.getPlacesByZipcode(query).toList()

        responses.assertFlowSuccess { actualPlace ->
            assertThat(actualPlace).containsExactlyElementsIn(expectedPlaces)
        }
        coVerify(exactly = 1) { mockCacheDao.selectCachedPlaceEntitiesByZipcode(query) }
    }

    @Test
    fun `getPlacesByZipcode should emit Failure when DAO flow throws exception`() = runTest {
        val query = "111"
        val exception = SQLiteException("Database read error")

        mockCacheSearch(query, emptyList(), exception)

        val responses = repository.getPlacesByZipcode(query).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockCacheDao.selectCachedPlaceEntitiesByZipcode(query) }
    }
}