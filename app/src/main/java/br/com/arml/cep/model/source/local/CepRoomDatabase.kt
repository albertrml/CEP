package br.com.arml.cep.model.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.arml.cep.model.entity.FavoriteEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.PlaceEntity


/*@Database(
    entities = [
        PlaceEntry::class,
        LogEntry::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CepRoomDatabase() : RoomDatabase() {
    abstract fun placeDao(): PlaceLocalDataSource
    abstract fun logDao(): LogLocalDataSource
}*/


@Database(
    entities = [
        FavoriteEntity::class,
        LogEntity::class,
        NoteEntity::class,
        PlaceEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class CepRoomDatabase() : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun cacheDao(): CacheDao
    abstract fun favoriteDao(): FavoriteDao
}