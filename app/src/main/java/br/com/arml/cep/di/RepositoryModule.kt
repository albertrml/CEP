package br.com.arml.cep.di

import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.model.repository.PlaceRepository
import br.com.arml.cep.model.source.local.LogLocalDataSource
import br.com.arml.cep.model.source.local.PlaceLocalDataSource
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
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
        placeRemoteDataSource: PlaceRemoteDataSource,
        placeLocalDataSource: PlaceLocalDataSource,
        logLocalDataSource: LogLocalDataSource
    ): PlaceRepository {
        return PlaceRepository(placeRemoteDataSource, placeLocalDataSource, logLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideLogRepository(logLocalDataSource: LogLocalDataSource): LogRepository {
        return LogRepository(logLocalDataSource)
    }

}