package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.getMockDate
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.source.local.LogLocalDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LogRepositoryTest {

    private val logLocalDataSource = mockk<LogLocalDataSource>()
    private val logRepository = LogRepository(logLocalDataSource)

    /*** getAllLogs ***/
    @Test
    fun `should emit success when getAllLogs works properly`() = runTest {
        val expectedLogs = mockLogEntries
        val logFlow = flowOf(expectedLogs)
        coEvery { logLocalDataSource.readAll() } returns logFlow

        val result = logRepository.getAllLogs().toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(expectedLogs), result.last())
        assertEquals(expectedLogs, (result.last() as Response.Success).result)
    }

    @Test
    fun `should emit loading and failure when getAllLogs throws exception`() = runTest {
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.readAll() } returns flow{ throw expectedException }

        val result = logRepository.getAllLogs().toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** filterLogsByCep ***/
    @Test
    fun `should emit loading and success when filterLogsByCep works properly`() = runTest {
        val query = "111"
        val expectedLogs = mockLogEntries.filter { it.cep.text.contains(query) }
        val logFlow = flowOf(expectedLogs)
        coEvery { logLocalDataSource.filterByCep(query) } returns logFlow

        val result = logRepository.filterLogsByCep(query).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(expectedLogs), result.last())
        assertEquals(expectedLogs, (result.last() as Response.Success).result)
    }

    @Test
    fun `should emit loading and failure when filterLogsByCep throws exception`() = runTest {
        val query = "111"
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.filterByCep(query) } returns flow{ throw expectedException }

        val result = logRepository.filterLogsByCep(query).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** filterLogsByInitialDate ***/
    @Test
    fun `should emit loading and success when filterLogsByInitialDate works properly`() = runTest {
        val initialDate = getMockDate(8)
        val expectedLogs = mockLogEntries.filter { it.timestamp.time >= initialDate.time }
        val logFlow = flowOf(expectedLogs)
        coEvery { logLocalDataSource.filterByInitialTimestamp(initialDate.time) } returns logFlow

        val result = logRepository.filterLogsByInitialDate(initialDate.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(expectedLogs), result.last())
        assertEquals(expectedLogs, (result.last() as Response.Success).result)
        assertEquals(
            expectedLogs.size,
            (result.last() as Response.Success).result.size
        )
    }

    @Test
    fun `should emit loading and failure when filterLogsByInitialDate throws exception`() = runTest {
        val initialDate = getMockDate(7)
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.filterByInitialTimestamp(initialDate.time) } returns flow{ throw expectedException }

        val result = logRepository.filterLogsByInitialDate(initialDate.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** filterLogsByFinalDate ***/
    @Test
    fun `should emit loading and success when filterLogsByFinalDate works properly`() = runTest {
        val dateQuery = getMockDate(8)
        val expectedLogs = mockLogEntries.filter { it.timestamp.time <= dateQuery.time }
        val logFlow = flowOf(expectedLogs)
        coEvery { logLocalDataSource.filterByFinalTimestamp(dateQuery.time) } returns logFlow

        val result = logRepository.filterLogsByFinalDate(dateQuery.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(expectedLogs), result.last())
        assertEquals(expectedLogs, (result.last() as Response.Success).result)
    }

    @Test
    fun `should emit loading and failure when filterLogsByFinalDate throws exception`() = runTest {
        val dateQuery = getMockDate(7)
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.filterByFinalTimestamp(dateQuery.time) } returns flow{ throw expectedException }

        val result = logRepository.filterLogsByFinalDate(dateQuery.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** filterLogsByRangeDate ***/
    @Test
    fun `should emit loading and success when filterLogsByRangeDate works properly`() = runTest {
        val initialDate = getMockDate(3)
        val finalDate = getMockDate(8)
        val expectedLogs = mockLogEntries.filter { it.timestamp.time in initialDate.time..finalDate.time }
        val logFlow = flowOf(expectedLogs)
        coEvery { logLocalDataSource.filterByTimestamp(initialDate.time, finalDate.time) } returns logFlow

        val result = logRepository.filterLogsByRangeDate(initialDate.time, finalDate.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(expectedLogs), result.last())
        assertEquals(expectedLogs, (result.last() as Response.Success).result)
    }

    @Test
    fun `should emit loading and failure when filterLogsByRangeDate throws exception`() = runTest {
        val initialDate = getMockDate(3)
        val finalDate = getMockDate(8)
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.filterByTimestamp(initialDate.time, finalDate.time) } returns flow{ throw expectedException }

        val result = logRepository.filterLogsByRangeDate(initialDate.time, finalDate.time).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** deleteAllLogs ***/
    @Test
    fun `should emit loading and success when deleteAllLogs works properly`() = runTest {
        coEvery { logLocalDataSource.deleteAll() } returns Unit
        val result = logRepository.deleteAllLogs().toList()
        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(Unit), result.last())
    }

    @Test
    fun `should emit loading and failure when deleteAllLogs throws exception`() = runTest {
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.deleteAll() } throws expectedException
        val result = logRepository.deleteAllLogs().toList()
        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }

    /*** deleteLog ***/
    @Test
    fun `should emit loading and success when deleteLog works properly`() = runTest {
        val entry = mockLogEntries.first()
        coEvery { logLocalDataSource.delete(entry) } returns Unit

        val result = logRepository.deleteLog(entry).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Success(Unit), result.last())
        assertEquals(Unit, (result.last() as Response.Success).result)
    }

    @Test
    fun `should emit loading and failure when deleteLog throws exception`() = runTest {
        val entry = mockLogEntries.first()
        val expectedException = Exception("Database error")
        coEvery { logLocalDataSource.delete(entry) } throws expectedException

        val result = logRepository.deleteLog(entry).toList()

        assertEquals(2,result.size)
        assertEquals(Response.Loading, result.first())
        assertEquals(Response.Failure(expectedException), result.last())
        assertEquals(expectedException, (result.last() as Response.Failure).exception)
    }
}