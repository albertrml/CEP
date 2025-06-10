package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.arml.cep.model.entity.PlaceEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(placeEntry: PlaceEntry)

    @Query("SELECT * FROM place_table WHERE cep =  :cep")
    suspend fun read(cep: String): PlaceEntry?

    @Query("SELECT * FROM place_table")
    fun readAll(): Flow<List<PlaceEntry>>

    @Query("SELECT * FROM place_table WHERE favorite_status = 1")
    fun readFavorites(): Flow<List<PlaceEntry>>

    @Update
    suspend fun update(placeEntry: PlaceEntry)

    @Delete
    suspend fun delete(placeEntry: PlaceEntry)

    @Query("DELETE FROM place_table WHERE favorite_status = 0")
    suspend fun deleteAllNotFavorite()

    @Query("SELECT * FROM place_table WHERE cep LIKE '%' || :query || '%' ")
    fun filterByCep(query: String): Flow<List<PlaceEntry>>

    @Query("SELECT * FROM place_table WHERE cep LIKE '%' || :query || '%' AND favorite_status = 1")
    fun filterByCepAndFavorite(query: String): Flow<List<PlaceEntry>>

    @Query("SELECT * FROM place_table WHERE cep LIKE '%' || :query || '%' AND favorite_status = 0")
    fun filterByCepAndUnwanted(query: String): Flow<List<PlaceEntry>>

}