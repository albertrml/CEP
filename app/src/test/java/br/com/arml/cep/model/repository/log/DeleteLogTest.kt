package br.com.arml.cep.model.repository.log

import android.util.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.mock.mockLogEntries
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

class DeleteLogTest {
    private val logDao = mockk<LogDao>()
    private lateinit var repository: LogRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(LogRepository(logDao))
    }

    private fun mockLogDelete(
        entry: LogEntity,
        exception: Exception? = null
    ) = mockAnswer(
        { logDao.deleteLogEntity(entry) },
        Unit,
        exception
    )

    /*
     * Function: deleteLog(entry: LogEntity)
     * Dependencies:
     *  - From logDao: deleteLogEntity
     */

    @Test
    fun `deleteLog should emits Loading and Success when database delete a single log`() = runTest {
        val expectedLogEntity = mockLogEntries.first().toEntity()

        mockLogDelete(expectedLogEntity)

        val responses = repository.deleteLog(expectedLogEntity).toList()

        responses.assertFlowSuccess {
            assertThat(it).isEqualTo(Unit)
        }
        coVerify(exactly = 1) { logDao.deleteLogEntity(any()) }
    }

    @Test
    fun `deleteLog should emits Loading and Failure when database throws exception`() = runTest {
        val expectedLogEntity = mockLogEntries.first().toEntity()
        val exception = Exception("Database error")

        mockLogDelete(
            expectedLogEntity,
            exception
        )

        val responses = repository.deleteLog(expectedLogEntity).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { logDao.deleteLogEntity(any()) }
    }
}