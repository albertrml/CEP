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

class FetchLogByPeriodTest {
    private val logDao = mockk<LogDao>()
    private lateinit var repository: LogRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(LogRepository(logDao))
    }

    private fun mockLogSearchByPeriod(
        startDate: Long,
        endDate: Long,
        data: List<PlaceWithLog> = emptyList(),
        exception: Exception? = null
    ) = mockFlowAnswer(
        { logDao.selectLogEntitiesByPeriod(startDate, endDate) },
        data,
        exception
    )

    /*
     * Function: fetchLogByZipcode(query: String)
     * Dependencies:
     *  - From logDao: selectLogEntitiesByPeriod
     */

    @Test
    fun `fetchLogByPeriod should emits Loading and Success when database returns filtered logs by period`() = runTest {
        val placeWithLogDB = mockPlacesWithLog(10)
        val (start, end) = placeWithLogDB[2].log.timestamp to placeWithLogDB[8].log.timestamp
        val expectedPlaceWithLogs = placeWithLogDB.filter { it.log.timestamp in start..end }
        val expectedLogs = expectedPlaceWithLogs.map { it.toModel() }

        mockLogSearchByPeriod(
            start,
            end,
            expectedPlaceWithLogs
        )

        val responses = repository.fetchLogByPeriod(start, end).toList()

        responses.assertFlowSuccess { actualLogs ->
            assertThat(actualLogs).containsExactlyElementsIn(expectedLogs)
        }
        coVerify(exactly = 1) { logDao.selectLogEntitiesByPeriod(start, end) }
    }

    @Test
    fun `fetchLogByPeriod should emits Loading and Success when database returns all logs by default`() = runTest {
        val placeWithLogDB = mockPlacesWithLog(10)
        val (start, end) = placeWithLogDB
            .map { it.log.timestamp }
            .let { it.first() to it.last() }
        val expectedPlaceWithLogs = placeWithLogDB.filter { it.log.timestamp in start..end }
        val expectedLogs = expectedPlaceWithLogs.map { it.toModel() }

        mockLogSearchByPeriod(
            start,
            end,
            expectedPlaceWithLogs
        )


        val responses = repository.fetchLogByPeriod(start, end).toList()

        responses.assertFlowSuccess { actualLogs ->
            assertThat(actualLogs).containsExactlyElementsIn(expectedLogs)
        }
        coVerify(exactly = 1) { logDao.selectLogEntitiesByPeriod(start, end) }
    }

    @Test
    fun `fetchLogByPeriod should emits Loading and Failure when database throws exception`() = runTest {
        val placeWithLogDB = mockPlacesWithLog(10)
        val (start, end) = placeWithLogDB
            .map { it.log.timestamp }
            .let { it.first() to it.last() }
        val exception = Exception("Database error")

        mockLogSearchByPeriod(
            startDate = start,
            endDate = end,
            exception = exception
        )

        val responses = repository.fetchLogByPeriod(start, end).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { logDao.selectLogEntitiesByPeriod(start, end) }
    }
}