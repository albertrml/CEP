package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.arml.cep.model.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    /** Create **/
    @Insert
    suspend fun insert(log: LogEntity)

    /** Read **/
    // Search for logs which match the query in the zipcode_place column
    @Query(
        """
        SELECT * FROM logs
        WHERE zipcode_place LIKE '%'||:query||'%'
        ORDER BY timestamp DESC
    """
    )
    fun getLogByZipcode(query: String = ""): Flow<List<LogEntity>>

    // Search for logs which are in the given period of time
    @Query("""
        SELECT * FROM logs
        WHERE timestamp >= :start AND timestamp <= :end
        ORDER BY timestamp DESC
    """)
    fun getLogByPeriod(
        start: Long = 0L,
        end : Long = System.currentTimeMillis()
    ): Flow<List<LogEntity>>

    /** Delete **/

    /** Update **/
    @Delete
    suspend fun delete(log: LogEntity)

    @Query("DELETE FROM logs")
    suspend fun deleteAll()

}