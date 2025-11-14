package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.arml.cep.model.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {
    /** Create **/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entry: PlaceEntity)

    /** Read **/
    @Query(
        """
        SELECT * FROM places
        WHERE 
            zipcode NOT IN (SELECT DISTINCT zipcode FROM favorites)
            AND zipcode LIKE '%' || :query || '%'
        ORDER BY zipcode ASC
    """
    )
    fun getByZipcode(query: String): Flow<List<PlaceEntity>>

    @Query(
        """
            SELECT * FROM places
            WHERE
                zipcode NOT IN (SELECT DISTINCT zipcode FROM favorites)
                AND zipcode = :zipcode
        """
    )
    suspend fun findByZipcode(zipcode: String): PlaceEntity?

    /** Update **/
    @Update
    suspend fun update(entry: PlaceEntity)

    /** Delete **/
    @Query("""
        DELETE FROM places
        WHERE zipcode = :zipcode
        AND zipcode NOT IN (SELECT DISTINCT zipcode FROM favorites)
    """)
    suspend fun deleteIfUnfavorite(zipcode: String)

    @Query(
        value = """
            DELETE FROM places
            WHERE zipcode NOT IN (SELECT DISTINCT zipcode FROM favorites)
        """
    )
    suspend fun deleteAllUnwanted()
}
