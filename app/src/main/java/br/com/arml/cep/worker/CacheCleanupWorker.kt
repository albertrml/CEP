package br.com.arml.cep.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.arml.core.response.Response
import br.com.arml.cep.model.repository.CacheRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CacheCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cacheRepository: CacheRepository
) : CoroutineWorker(appContext, workerParams) {
    
    override suspend fun doWork(): Result {
        val thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000
        val monthAgoTimestamp = System.currentTimeMillis() - thirtyDaysInMillis
        
        return cacheRepository.autoCleanCache(monthAgoTimestamp)
            .first { it !is Response.Loading }
            .let { response ->
                if (response is Response.Success) Result.success() else Result.failure()
            }
    }
}
