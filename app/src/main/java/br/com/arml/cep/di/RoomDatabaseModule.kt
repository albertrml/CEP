package br.com.arml.cep.di

import android.content.Context
import androidx.room.Room
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.CepRoomDatabase
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.local.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    private const val DATABASE_NAME = "cep_database"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): CepRoomDatabase {
        return Room
            .databaseBuilder(
                ctx.applicationContext,
                CepRoomDatabase::class.java,
                DATABASE_NAME
            )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideCacheDao(db: CepRoomDatabase): CacheDao{ return db.cacheDao() }

    @Provides
    @Singleton
    fun provideFavorite(db: CepRoomDatabase): FavoriteDao { return db.favoriteDao() }

    @Provides
    @Singleton
    fun provideLogs(db: CepRoomDatabase): LogDao { return db.logDao() }
}