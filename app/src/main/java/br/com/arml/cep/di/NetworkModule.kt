package br.com.arml.cep.di

import br.com.arml.cep.model.qualifier.RetrofitMoshi
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    const val BASE_URL = "https://viacep.com.br/ws/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @RetrofitMoshi moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): PlaceRemoteDataSource {
        return retrofit.create(PlaceRemoteDataSource::class.java)
    }
}