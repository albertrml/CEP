package br.com.arml.cep.di

import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.model.repository.PlaceRepository
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.local.PlaceDao
import br.com.arml.cep.model.source.remote.CepApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCepRepository(
        service: CepApiService,
        placeDao: PlaceDao,
        logDao: LogDao
    ): PlaceRepository {
        return PlaceRepository(service, placeDao, logDao)
    }

    @Provides
    @Singleton
    fun provideLogRepository(logDao: LogDao): LogRepository {
        return LogRepository(logDao)
    }

}