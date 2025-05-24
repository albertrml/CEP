package br.com.arml.cep.di

import br.com.arml.cep.model.repository.CepRepository
import br.com.arml.cep.model.source.CepApiService
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
    fun provideCepRepository(service: CepApiService) : CepRepository {
        return CepRepository(service)
    }

}