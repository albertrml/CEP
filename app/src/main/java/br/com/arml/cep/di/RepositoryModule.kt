package br.com.arml.cep.di

import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.repository.LogRepository
import br.com.arml.cep.model.repository.SearchRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.model.source.local.LogDao
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
    fun provideCacheRepository(cacheDao: CacheDao): CacheRepository {
        return CacheRepository(cacheDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepository(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideLogsRepository(logDao: LogDao): LogRepository {
        return LogRepository(logDao)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchService: PlaceRemoteDataSource,
        cacheDao: CacheDao,
        logDao: LogDao
    ) : SearchRepository {
        return SearchRepository(
            searchService = searchService,
            cacheDao = cacheDao,
            logDao = logDao
        )
    }
}