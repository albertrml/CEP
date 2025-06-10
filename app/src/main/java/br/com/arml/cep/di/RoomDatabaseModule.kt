package br.com.arml.cep.di

import android.content.Context
import br.com.arml.cep.model.source.local.CepRoomDatabase
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.local.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideCepRoomDatabase(@ApplicationContext ctx: Context): CepRoomDatabase {
        return CepRoomDatabase.getDatabase(ctx)
    }

    @Provides
    @Singleton
    fun providePlaceDao(database: CepRoomDatabase): PlaceDao {
        return database.placeDao()
    }

    @Provides
    @Singleton
    fun provideLogDao(database: CepRoomDatabase): LogDao {
        return database.logDao()
    }

}