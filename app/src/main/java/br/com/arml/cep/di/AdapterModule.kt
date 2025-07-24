package br.com.arml.cep.di

import br.com.arml.cep.model.adapter.CepJsonAdapter
import br.com.arml.cep.model.adapter.FavoriteJsonAdapter
import br.com.arml.cep.model.adapter.NoteJsonAdapter
import br.com.arml.cep.model.qualifier.BackupMoshi
import br.com.arml.cep.model.qualifier.RetrofitMoshi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule{
    @Provides
    @Singleton
    @RetrofitMoshi
    fun provideMoshiToRetrofit(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @BackupMoshi
    fun provideMoshiToBackup(): Moshi {
        return Moshi.Builder()
            .add(CepJsonAdapter())
            .add(FavoriteJsonAdapter())
            .add(NoteJsonAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
}