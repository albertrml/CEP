package br.com.arml.cep.model.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.entity.PlaceEntry

const val DATABASE_NAME = "cep_database"

@Database(
    entities = [ PlaceEntry::class, LogEntry::class ],
    version = 1,
    exportSchema = true
)
abstract class CepRoomDatabase() : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun logDao(): LogDao

    companion object{
        @Volatile
        private var INSTANCE: CepRoomDatabase? = null

        fun getDatabase(ctx: Context): CepRoomDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    CepRoomDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}