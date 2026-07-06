package br.com.arml.cep.model.repository.log

import android.util.Log
import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.model.source.local.LogDao
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

class DeleteAllTest {
    private val mockLogDao = mockk<LogDao>()
    private lateinit var repository: LogRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(LogRepository(mockLogDao))
    }

    private fun mockLogDeleteAll(
        exception: Exception? = null
    ) = mockAnswer(
        { mockLogDao.deleteAllLogEntities() },
        Unit,
        exception
    )

    /*
     * Function: deleteAllLogs()
     * Dependencies:
     *  - From logDao: deleteAllLogEntities
     */

    @Test
    fun `deleteAllLogs should emits Loading and Success when database delete all logs`() = runTest {
        mockLogDeleteAll()

        val responses = repository.deleteAllLogs().toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { mockLogDao.deleteAllLogEntities() }
    }

    @Test
    fun `deleteAllLogs should emits Loading and Failure when database throws exception`() = runTest {
        val exception = Exception("Database error")

        mockLogDeleteAll(exception)

        val responses = repository.deleteAllLogs().toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockLogDao.deleteAllLogEntities() }
    }
}