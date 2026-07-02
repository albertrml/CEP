package br.com.arml.cep.model.repository.cache

import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockAnswer
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AutoCleanCacheTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup() {
        repository = CacheRepository(mockCacheDao)
    }

    private fun mockCacheAutoClean(
        timeCutoff: Long = 30L * 24 * 60 * 60 * 1000,
        exception: Exception? = null
    ) = mockAnswer(
        { mockCacheDao.autoCleanCache(timeCutoff) },
        Unit,
        exception
    )

    /*
     * Function: autoCleanCache(timeCutoff: Long)
     * Dependencies:
     *  - From CacheDao: autoCleanCache
     */

    @Test
    fun `autoCleanCache should emit Success when DAO autoCleanCache removes entries`() = runTest{
        val timeCutoff = 30L * 24 * 60 * 60 * 1000
        mockCacheAutoClean(timeCutoff)

        val responses = repository.autoCleanCache(timeCutoff).toList()
        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }

        coVerify(exactly = 1) { mockCacheDao.autoCleanCache(timeCutoff) }
    }

    @Test
    fun `autoCleanCache should emit Failure when DAO autoCleanCache throws exception`() = runTest{
        val timeCutoff = 30L * 24 * 60 * 60 * 1000
        val exception = Exception("Auto clean failed")

        mockCacheAutoClean(timeCutoff = timeCutoff, exception = exception)

        val responses = repository.autoCleanCache(timeCutoff).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(exception.javaClass)
        }
        coVerify(exactly = 1) { mockCacheDao.autoCleanCache(timeCutoff) }
    }
}