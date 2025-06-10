package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.arml.cep.model.entity.LogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun create(logEntry: LogEntry)

    @Query("SELECT COUNT(*) FROM log_table")
    fun count(): Flow<Int>

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    fun readAll(): Flow<List<LogEntry>>

    @Delete
    suspend fun delete(logEntry: LogEntry)

    @Query("DELETE FROM log_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM log_table WHERE cep LIKE '%' || :query || '%'")
    fun filterByCep(query: String): Flow<List<LogEntry>>

    @Query("SELECT * FROM log_table WHERE timestamp >= :start ORDER BY timestamp DESC")
    fun filterByInitialTimestamp(start: Long): Flow<List<LogEntry>>

    @Query("SELECT * FROM log_table WHERE timestamp <= :end ORDER BY timestamp DESC")
    fun filterByFinalTimestamp(end: Long): Flow<List<LogEntry>>

    @Query("SELECT * FROM log_table WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun filterByTimestamp(start: Long, end: Long): Flow<List<LogEntry>>
}