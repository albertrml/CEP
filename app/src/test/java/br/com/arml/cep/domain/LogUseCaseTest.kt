package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.BASE_TIMESTAMP
import br.com.arml.cep.model.mock.getMockDate
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.sql.Timestamp

class LogUseCaseTest {
    private val logRepository: LogRepository = mockk()
    private lateinit var logUseCase: LogUseCase

    @Before
    fun setup() {
        logUseCase = LogUseCase(logRepository)
    }

    // region Helper Functions for Mocks
    private fun mockFetchByZipcodeSuccess(expectedResult: List<Log>) {
        coEvery {
            logRepository.fetchLogByZipcode(any())
        } returns flowOf(Response.Loading, Response.Success(expectedResult))
    }

    private fun mockFetchByZipcodeFailure(exception: Exception) {
        coEvery {
            logRepository.fetchLogByZipcode(any())
        } returns flowOf(Response.Loading, Response.Failure(exception))
    }

    private fun mockFetchByPeriodSuccess(expectedResult: List<Log>) {
        coEvery {
            logRepository.fetchLogByPeriod(any(), any())
        } returns flowOf(Response.Loading, Response.Success(expectedResult))
    }

    private fun mockFetchByPeriodFailure(exception: Exception) {
        coEvery {
            logRepository.fetchLogByPeriod(any(), any())
        } returns flowOf(Response.Loading, Response.Failure(exception))
    }
    // endregion

    // region fetchAllLogs tests
    @Test
    fun `fetchAllLogs should emit Success with logs`() = runTest {
        val expectedLogs = mockLogEntries.sortedBy { it.timestamp }
        mockFetchByZipcodeSuccess(expectedLogs)

        val responses = logUseCase.fetchAllLogs().toList()

        responses.assertFlowSuccess { assertEquals(expectedLogs, it.sortedBy { log -> log.timestamp }) }
        coVerify(exactly = 1) { logRepository.fetchLogByZipcode("") }
    }

    @Test
    fun `fetchAllLogs should emit Success with empty list`() = runTest {
        mockFetchByZipcodeSuccess(emptyList())

        val responses = logUseCase.fetchAllLogs().toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify(exactly = 1) { logRepository.fetchLogByZipcode("") }
    }

    @Test
    fun `fetchAllLogs should emit Failure`() = runTest {
        val exception = Exception("Error fetching logs")
        mockFetchByZipcodeFailure(exception)

        val responses = logUseCase.fetchAllLogs().toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByZipcode("") }
    }
    // endregion

    // region filterLogsByCep tests
    @Test
    fun `filterLogsByCep should emit Success with filtered logs`() = runTest {
        val query = mockLogEntries.first().cep.text.take(5)
        val expectedLogs = mockLogEntries.filter { it.cep.text.contains(query) }
        mockFetchByZipcodeSuccess(expectedLogs)

        val responses = logUseCase.filterLogsByCep(query).toList()

        responses.assertFlowSuccess { assertEquals(expectedLogs, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByZipcode(query) }
    }

    @Test
    fun `filterLogsByCep should emit Success with empty list`() = runTest {
        val query = "12345"
        mockFetchByZipcodeSuccess(emptyList())

        val responses = logUseCase.filterLogsByCep(query).toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify { logRepository.fetchLogByZipcode(query) }
    }

    @Test
    fun `filterLogsByCep should emit Failure`() = runTest {
        val query = ""
        val exception = Exception("Error filtering by CEP")
        mockFetchByZipcodeFailure(exception)

        val responses = logUseCase.filterLogsByCep(query).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByZipcode(query) }
    }
    // endregion

    // region filterLogsByInitialDate tests
    @Test
    fun `filterLogsByInitialDate should emit Success with filtered logs`() = runTest {
        val initialDate = mockLogEntries[5].timestamp
        val expectedLogs = mockLogEntries.filter { it.timestamp >= initialDate }
        mockFetchByPeriodSuccess(expectedLogs)

        val responses = logUseCase.filterLogsByInitialDate(initialDate.time).toList()

        responses.assertFlowSuccess { assertEquals(expectedLogs, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(startDate = initialDate.time) }
    }

    @Test
    fun `filterLogsByInitialDate should emit Success with empty list`() = runTest {
        val initialDate = getMockDate(mockLogEntries.lastIndex)
        mockFetchByPeriodSuccess(emptyList())

        val responses = logUseCase.filterLogsByInitialDate(initialDate.time).toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(startDate = initialDate.time) }
    }

    @Test
    fun `filterLogsByInitialDate should emit Failure`() = runTest {
        val initialDate = 0L
        val exception = Exception("Error filtering by initial date")
        mockFetchByPeriodFailure(exception)

        val responses = logUseCase.filterLogsByInitialDate(initialDate).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(startDate = initialDate) }
    }
    // endregion

    // region filterLogsByFinalDate tests
    @Test
    fun `filterLogsByFinalDate should emit Success with filtered logs`() = runTest {
        val finalDate = mockLogEntries[5].timestamp
        val expectedLogs = mockLogEntries.filter { it.timestamp <= finalDate }
        mockFetchByPeriodSuccess(expectedLogs)

        val responses = logUseCase.filterLogsByFinalDate(finalDate.time).toList()

        responses.assertFlowSuccess { assertEquals(expectedLogs, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(endDate = finalDate.time) }
    }

    @Test
    fun `filterLogsByFinalDate should emit Success with empty list`() = runTest {
        val finalDate = Timestamp(BASE_TIMESTAMP - 1L)
        mockFetchByPeriodSuccess(emptyList())

        val responses = logUseCase.filterLogsByFinalDate(finalDate.time).toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(endDate = finalDate.time) }
    }

    @Test
    fun `filterLogsByFinalDate should emit Failure`() = runTest {
        val finalDate = 0L
        val exception = Exception("Error filtering by final date")
        mockFetchByPeriodFailure(exception)

        val responses = logUseCase.filterLogsByFinalDate(finalDate).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(endDate = finalDate) }
    }
    // endregion

    // region filterLogsByRangeDate tests
    @Test
    fun `filterLogsByRangeDate should emit Success with filtered logs`() = runTest {
        val initialDate = mockLogEntries[8].timestamp
        val finalDate = mockLogEntries[12].timestamp
        val expectedLogs = mockLogEntries.filter { it.timestamp in initialDate..finalDate }
        mockFetchByPeriodSuccess(expectedLogs)

        val responses = logUseCase.filterLogsByRangeDate(initialDate.time, finalDate.time).toList()

        responses.assertFlowSuccess { assertEquals(expectedLogs, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(initialDate.time, finalDate.time) }
    }

    @Test
    fun `filterLogsByRangeDate should emit Success with empty list`() = runTest {
        val initialDate = Timestamp(0L)
        val finalDate = Timestamp(BASE_TIMESTAMP - 1L)
        mockFetchByPeriodSuccess(emptyList())

        val responses = logUseCase.filterLogsByRangeDate(initialDate.time, finalDate.time).toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(initialDate.time, finalDate.time) }
    }

    @Test
    fun `filterLogsByRangeDate should emit Failure`() = runTest {
        val initialDate = 0L
        val finalDate = 1L
        val exception = Exception("Error filtering by date range")
        mockFetchByPeriodFailure(exception)

        val responses = logUseCase.filterLogsByRangeDate(initialDate, finalDate).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { logRepository.fetchLogByPeriod(initialDate, finalDate) }
    }
    // endregion
}