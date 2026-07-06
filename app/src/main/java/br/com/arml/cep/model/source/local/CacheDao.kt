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
    suspend fun insertPlaceEntity(entry: PlaceEntity): Long

    /** Read **/
    @Query(
        """
        SELECT p.* 
        FROM Places p
        WHERE NOT EXISTS (
            SELECT 1
            FROM Favorites f
            WHERE f.id_place = p.id
        )
        AND p.zipcode LIKE '%' || :query || '%'
        ORDER BY p.zipcode ASC
    """
    )
    fun selectCachedPlaceEntitiesByZipcode(query: String): Flow<List<PlaceEntity>>

    @Query(
        """
            SELECT p.* 
            FROM Places p
            WHERE NOT EXISTS (
                SELECT 1
                FROM Favorites f
                WHERE f.id_place = p.id
            )
            AND zipcode = :zipcode
        """
    )
    suspend fun selectCachedPlaceEntityByZipcode(zipcode: String): PlaceEntity?

    @Query(
        """
            SELECT p.* 
            FROM Places p
            WHERE zipcode = :zipcode
        """
    )
    suspend fun selectPlaceEntityByZipcode(zipcode: String): PlaceEntity?


    @Transaction
    @Query(
        """
        SELECT * FROM Places
        WHERE zipcode = :zipcode
    """
    )
    suspend fun selectPlaceWithNotesByZipcode(zipcode: String): PlaceWithNotes?

    @Transaction
    @Query(
        """
        SELECT * FROM Places
        WHERE uf = :uf 
            AND city_search LIKE '%' || :city || '%' 
            AND street_search LIKE '%' || :street || '%'
        ORDER BY zipcode ASC
    """
    )
    fun selectPlacesWithNotes(uf: String, city: String, street: String): Flow<List<PlaceWithNotes>>

    @Transaction
    @Query(
        """
        SELECT EXISTS (
            SELECT 1 FROM Places
            WHERE uf = :uf 
                AND city_search LIKE '%' || :city || '%' 
                AND street_search LIKE '%' || :street || '%'
        )
    """
    )
    suspend fun arePlacesExist(uf: String, city: String, street: String): Boolean

    @Query(
        """
            SELECT EXISTS (
                SELECT 1 FROM Places
                WHERE zipcode = :zipcode
            )
        """
    )
    suspend fun isPlaceExist(zipcode: String): Boolean


    /** Update **/
    @Update
    suspend fun updatePlaceEntity(entry: PlaceEntity)

    /** Delete **/
    @Query(
        """
        DELETE FROM Places
        WHERE zipcode = :zipcode
        AND id NOT IN (SELECT DISTINCT id_place FROM Favorites)
    """
    )
    suspend fun deleteCachedPlaceEntity(zipcode: String)

    @Query(
        value = """
            DELETE FROM Places
            WHERE id NOT IN (SELECT DISTINCT id_place FROM Favorites)
        """
    )
    suspend fun deleteAllCachedPlaceEntities()

    @Query(
        """
            DELETE FROM Places
            WHERE id NOT IN (SELECT DISTINCT id_place FROM Favorites)
            AND created_at < :timeCutoff
        """
    )
    suspend fun autoCleanCache(timeCutoff: Long)
}
