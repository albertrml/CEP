package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {
    /** Create **/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaceEntity(entry: PlaceEntity)

    /** Read **/
    @Query(
        """
        SELECT * FROM Places
        WHERE 
            zipcode NOT IN (SELECT DISTINCT zipcode_place FROM Favorites)
            AND zipcode LIKE '%' || :query || '%'
        ORDER BY zipcode ASC
    """
    )
    fun selectCachedPlaceEntitiesByZipcode(query: String): Flow<List<PlaceEntity>>

    @Query(
        """
            SELECT * FROM Places
            WHERE zipcode NOT IN (SELECT DISTINCT zipcode_place FROM Favorites)
            AND zipcode = :zipcode
        """
    )
    suspend fun selectCachedPlaceEntityByZipcode(zipcode: String): PlaceEntity?

    @Transaction
    @Query(
        """
        SELECT * FROM Places
        WHERE zipcode = :zipcode
    """
    )
    suspend fun selectPlaceWithNotesByZipcode(zipcode: String): PlaceWithNotes?

    /** Update **/
    @Update
    suspend fun updatePlaceEntity(entry: PlaceEntity)

    /** Delete **/
    @Query(
        """
        DELETE FROM Places
        WHERE zipcode = :zipcode
        AND zipcode NOT IN (SELECT DISTINCT zipcode_place FROM Favorites)
    """
    )
    suspend fun deleteCachedPlaceEntity(zipcode: String)

    @Query(
        value = """
            DELETE FROM Places
            WHERE zipcode NOT IN (SELECT DISTINCT zipcode_place FROM Favorites)
        """
    )
    suspend fun deleteAllCachedPlaceEntities()
}
