package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LogRepositoryTest {

    private val logDao = mockk<LogDao>()
    private lateinit var logRepository: LogRepository

    @Before
    fun setup() {
        logRepository = LogRepository(logDao)
    }

    private fun mockGetLogByZipcodeSuccess(result: List<LogEntity>) {
        coEvery { logDao.getLogByZipcode(any()) } returns flowOf(result)
    }

    private fun mockGetLogByZipcodeFailure(exception: Exception) {
        coEvery { logDao.getLogByZipcode(any()) } returns flow { throw exception }
    }

    private fun mockGetLogByPeriodSuccess(result: List<LogEntity>) {
        coEvery { logDao.getLogByPeriod(any(), any()) } returns flowOf(result)
    }

    private fun mockGetLogByPeriodFailure(exception: Exception) {
        coEvery { logDao.getLogByPeriod(any(), any()) } returns flow { throw exception }
    }

    // region fetchLogByZipcode tests
    @Test
    fun `fetchLogByZipcode should emit Success with Logs`() = runTest {
        val query = mockLogEntries.first().cep.text.substring(0, 3)
        val expectedLogs = mockLogEntries.filter { it.cep.text.contains(query) }
        mockGetLogByZipcodeSuccess(expectedLogs.map { it.toEntity() })

        val responses = logRepository.fetchLogByZipcode(query).toList()

        responses.assertFlowSuccess { actual ->
            assertTrue(actual.containsAll(expectedLogs))
        }
        coVerify(exactly = 1) { logDao.getLogByZipcode(query) }
    }

    @Test
    fun `fetchLogByZipcode should emit Success with empty list`() = runTest {
        val query = "12345678"
        mockGetLogByZipcodeSuccess(emptyList())

        val responses = logRepository.fetchLogByZipcode(query).toList()

        responses.assertFlowSuccess { assertTrue(it.isEmpty()) }
        coVerify(exactly = 1) { logDao.getLogByZipcode(query) }
    }

    @Test
    fun `fetchLogByZipcode should emit Failure`() = runTest {
        val query = "111"
        val exception = Exception("Database error")
        mockGetLogByZipcodeFailure(exception)

        val responses = logRepository.fetchLogByZipcode(query).toList()

        responses.assertFlowFailure { assertEquals(exception.javaClass, it.javaClass) }
        coVerify(exactly = 1) { logDao.getLogByZipcode(query) }
    }
    // endregion

    // region fetchLogByPeriod tests
    @Test
    fun `fetchLogByPeriod should emit Success with Logs by period`() = runTest {
        val (start, end) = mockLogEntries[2].timestamp.time to mockLogEntries[8].timestamp.time
        val expectedLogs = mockLogEntries.filter { it.timestamp.time in start..end }
        mockGetLogByPeriodSuccess(expectedLogs.map { it.toEntity() })

        val responses = logRepository.fetchLogByPeriod(start, end).toList()

        responses.assertFlowSuccess { assertTrue(it.containsAll(expectedLogs)) }
        coVerify(exactly = 1) { logDao.getLogByPeriod(start, end) }
    }

    @Test
    fun `fetchLogByPeriod should emit Success with all logs by default`() = runTest {
        val expectedLogs = mockLogEntries
        mockGetLogByPeriodSuccess(expectedLogs.map { it.toEntity() })

        val responses = logRepository.fetchLogByPeriod().toList()

        responses.assertFlowSuccess { assertTrue(it.containsAll(expectedLogs)) }
        coVerify(exactly = 1) { logDao.getLogByPeriod(any(), any()) }
    }

    @Test
    fun `fetchLogByPeriod should emit Failure`() = runTest {
        val exception = Exception("Database error")
        mockGetLogByPeriodFailure(exception)

        val responses = logRepository.fetchLogByPeriod().toList()

        responses.assertFlowFailure { assertEquals(exception.javaClass, it.javaClass) }
        coVerify(exactly = 1) { logDao.getLogByPeriod(any(), any()) }
    }
    // endregion

    // region deleteAllLogs tests
    @Test
    fun `deleteAllLogs should emit Success`() = runTest {
        coJustRun { logDao.deleteAll() }

        val responses = logRepository.deleteAllLogs().toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { logDao.deleteAll() }
    }

    @Test
    fun `deleteAllLogs should emit Failure`() = runTest {
        val exception = Exception("Database error")
        coEvery { logDao.deleteAll() } throws exception

        val responses = logRepository.deleteAllLogs().toList()

        responses.assertFlowFailure { assertEquals(exception.javaClass, it.javaClass) }
        coVerify(exactly = 1) { logDao.deleteAll() }
    }
    // endregion

    // region deleteLog tests
    @Test
    fun `deleteLog should emit Success`() = runTest {
        val log = mockLogEntries.first()
        coJustRun { logDao.delete(any()) }

        val responses = logRepository.deleteLog(log.toEntity()).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { logDao.delete(any()) }
    }

    @Test
    fun `deleteLog should emit Failure`() = runTest {
        val log = mockLogEntries.first()
        val exception = Exception("Database error")
        coEvery { logDao.delete(any()) } throws exception

        val responses = logRepository.deleteLog(log.toEntity()).toList()

        responses.assertFlowFailure { assertEquals(exception.javaClass, it.javaClass) }
        coVerify(exactly = 1) { logDao.delete(any()) }
    }
    // endregion
}