package br.com.arml.cep.worker

import androidx.work.ListenableWorker.Result
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.repository.CacheRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CacheCleanupWorkerTest {
    private val mockCacheRepository =  mockk<CacheRepository>()
    private lateinit var worker: CacheCleanupWorker

    @Before
    fun setup(){
        worker = CacheCleanupWorker(mockk(), mockk(), mockCacheRepository)
    }

    private fun mockCacheRepoCleanup(
        response: Response<Unit>
    ) {
        every {
            mockCacheRepository.autoCleanCache(any())
        } returns flowOf(response)
    }

    @Test
    fun `cacheCleanupWorker should ignore loading states`() = runTest {
        every {
            mockCacheRepository.autoCleanCache(any())
        } returns flowOf(
            Response.Loading,
            Response.Loading,
            Response.Success(Unit)
        )

        val result = worker.doWork()

        assertThat(result).isEqualTo(Result.success())
    }

    @Test
    fun `cacheCleanupWorker should return success when repository deletes old entries`() = runTest {
        mockCacheRepoCleanup(Response.Success(Unit))

        val result = worker.doWork()

        assertThat(result).isEqualTo(Result.success())

        verify(exactly = 1) {
            mockCacheRepository.autoCleanCache(any())
        }
    }

    @Test
    fun `cacheCleanupWorker should return failure when repository returns failure`() = runTest {
        mockCacheRepoCleanup(
            Response.Failure(Exception("Auto clean failed"))
        )

        val result = worker.doWork()

        assertThat(result).isEqualTo(Result.failure())

        verify(exactly = 1) {
            mockCacheRepository.autoCleanCache(any())
        }
    }
}