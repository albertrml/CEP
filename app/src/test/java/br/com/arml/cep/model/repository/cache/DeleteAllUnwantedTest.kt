package br.com.arml.cep.model.repository.cache

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.source.local.CacheDao
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

class DeleteAllUnwantedTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(CacheRepository(mockCacheDao))
    }

    private fun mockCacheDelete(
        exception: Exception? = null
    ) = mockAnswer(
        { mockCacheDao.deleteAllCachedPlaceEntities() },
        Unit,
        exception
    )

    /*
     * Function: deleteAllUnwanted()
     * Dependencies:
     *  - From CacheDao: deleteAllCachedPlaceEntities
     */

    @Test
    fun `deleteAllUnwanted should emit Success on successful DAO deletion`() = runTest {
        mockCacheDelete()

        val responses = repository.deleteAllUnwanted().toList()

        responses.assertFlowSuccess { assertThat(true).isTrue() }
        coVerify(exactly = 1) { mockCacheDao.deleteAllCachedPlaceEntities() }
    }

    @Test
    fun `deleteAllUnwanted should emit Failure when DAO throws exception`() = runTest {
        val exception = SQLiteException("Delete all failed")
        mockCacheDelete(exception)

        val responses = repository.deleteAllUnwanted().toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockCacheDao.deleteAllCachedPlaceEntities() }
    }
}