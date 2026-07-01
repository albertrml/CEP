package br.com.arml.cep.model.repository.log

import android.util.Log
import br.com.arml.cep.model.entity.relation.PlaceWithLog
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.mock.mockPlacesWithLog
import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.model.source.local.LogDao
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

class FetchLogByZipcodeTest {
    private val mockLogDao = mockk<LogDao>()
    private lateinit var repository: LogRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(LogRepository(mockLogDao))
    }

    private fun mockLogSearchByZipcode(
        query: String,
        data: List<PlaceWithLog> = emptyList(),
        exception: Exception? = null
    ) = mockFlowAnswer(
        { mockLogDao.selectLogEntitiesByZipcode(query) },
        data,
        exception
    )

    /*
     * Function: fetchLogByZipcode(query: String)
     * Dependencies:
     *  - From logDao: selectLogEntitiesByZipcode
     */


    @Test
    fun `fetchLogByZipcode should emits Loading and Success when database returns all logs`() = runTest {
        val placeWithLogDB = mockPlacesWithLog(10)
        val query = placeWithLogDB
            .first()
            .place.zipcode
            .substring(0,3)
        val expectedPlaceWithLogs = placeWithLogDB
            .filter { it.place.zipcode.contains(query) }
        val expectedLogs = expectedPlaceWithLogs.map { it.toModel() }

        mockLogSearchByZipcode(
            query,
            expectedPlaceWithLogs
        )

        val responses = repository.fetchLogByZipcode(query).toList()

        responses.assertFlowSuccess { actualLogs ->
            assertThat(actualLogs).containsExactlyElementsIn(expectedLogs)
        }
        coVerify(exactly = 1) { mockLogDao.selectLogEntitiesByZipcode(query) }
    }

    @Test
    fun `fetchLogByZipcode should emits Loading and Success when database returns empty list`() = runTest {
        val query = "12345678"

        mockLogSearchByZipcode(query)

        val responses = repository.fetchLogByZipcode(query).toList()

        responses.assertFlowSuccess { actualLogs ->
            assertThat(actualLogs).isEmpty()
        }
        coVerify(exactly = 1) { mockLogDao.selectLogEntitiesByZipcode(query) }
    }

    @Test
    fun `fetchLogByZipcode should emits Loading and Failure when database throws exception`() = runTest {
        val query = "111"
        val exception = Exception("Database error")

        mockLogSearchByZipcode(
            query = query,
            exception = exception
        )

        val responses = repository.fetchLogByZipcode(query).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockLogDao.selectLogEntitiesByZipcode(query) }
    }

}