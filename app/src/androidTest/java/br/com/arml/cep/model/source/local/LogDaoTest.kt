package br.com.arml.cep.model.source.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

@RunWith(AndroidJUnit4::class)
class LogDaoTest {
    private lateinit var db: CepRoomDatabase
    private lateinit var logDao: LogDao
    private lateinit var cacheDao: CacheDao

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, CepRoomDatabase::class.java)
            .allowMainThreadQueries().build()
        logDao = db.logDao()
        cacheDao = db.cacheDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private suspend fun populateDatabase(places: List<PlaceEntity>, logsPerPlace: Int): List<LogEntity> {
        val allLogs = mutableListOf<LogEntity>()
        places.forEach { place ->
            cacheDao.insertPlaceEntity(place)
            repeat(logsPerPlace) {
                val log = LogEntity(zipcodePlace = place.zipcode, timestamp = System.nanoTime())
                val id = logDao.insertLogEntity(log)
                allLogs.add(log.copy(id = id))
            }
        }
        return allLogs
    }

    /** CREATE **/
    @Test
    fun insertLogEntity_shouldSaveLog_whenPlaceExists() = runTest {
        val place = mockUnfavoritePlaceEntities.first().place
        cacheDao.insertPlaceEntity(place)
        val expectedLog = LogEntity(zipcodePlace = place.zipcode, timestamp = System.currentTimeMillis())

        logDao.insertLogEntity(expectedLog)
        val actualLogs = logDao.selectLogEntitiesByZipcode("").first().map { it.copy(id = 0L) }

        assertThat(actualLogs).hasSize(1)
        assertThat(actualLogs.first()).isEqualTo(expectedLog)
    }

    @Test
    fun insertLogEntity_shouldThrowException_whenPlaceDoesNotExist() = runTest {
        val logWithInvalidZipcode = LogEntity(zipcodePlace = "99999-999", timestamp = 0L)

        assertFailsWith<SQLiteConstraintException> { logDao.insertLogEntity(logWithInvalidZipcode) }
    }

    /** READ **/
    @Test
    fun selectLogEntitiesByZipcode_shouldReturnFilteredLogs() = runTest {
        val places = mockUnfavoritePlaceEntities.map { it.place }
        val storedLogs = populateDatabase(places, 2).sortedBy { it.timestamp }
        val query = places.first().zipcode.take(3)
        val expectedLogs = storedLogs.filter { it.zipcodePlace.contains(query) }

        val actualLogs = logDao.selectLogEntitiesByZipcode(query).first().sortedBy { it.timestamp }

        assertThat(actualLogs).containsExactlyElementsIn(expectedLogs).inOrder()
    }

    @Test
    fun selectLogEntitiesByZipcode_shouldReturnEmptyList_whenZipcodeDoesNotExist() = runTest {
        val result = logDao.selectLogEntitiesByZipcode("123").first()

        assertThat(result).isEmpty()
    }

    @Test
    fun selectLogEntitiesByPeriod_shouldReturnLogsWithinPeriod() = runTest {
        val place = mockUnfavoritePlaceEntities.first().place
        cacheDao.insertPlaceEntity(place)
        val timestamps = listOf(100L, 200L, 300L, 400L)
        timestamps.forEach {
            logDao.insertLogEntity(LogEntity(zipcodePlace = place.zipcode, timestamp = it))
        }

        val actualLogs = logDao.selectLogEntitiesByPeriod(start = 150L, end = 350L)
            .first()
            .sortedBy { it.timestamp }

        assertThat(actualLogs).hasSize(2)
        assertThat(actualLogs.map { it.timestamp }).containsExactly(200L, 300L).inOrder()
    }

    @Test
    fun selectLogEntitiesByPeriod_shouldReturnEmptyList_whenNoLogsMatchPeriod() = runTest {
        val place = mockUnfavoritePlaceEntities.first().place
        cacheDao.insertPlaceEntity(place)
        val timestamps = listOf(100L, 200L, 300L, 400L)
        timestamps.forEach {
            logDao.insertLogEntity(LogEntity(zipcodePlace = place.zipcode, timestamp = it))
        }

        val result = logDao.selectLogEntitiesByPeriod(start = 0L, end = 90L).first()

        assertThat(result).isEmpty()
    }

    /** DELETE **/
    @Test
    fun deleteLogEntity_shouldDeleteCorrectLog() = runTest {
        val places = mockUnfavoritePlaceEntities.map { it.place }
        val allLogs = populateDatabase(places, 2)
        val logToDelete = allLogs.first()

        logDao.deleteLogEntity(logToDelete)
        val logsAfterDelete = logDao.selectLogEntitiesByZipcode("").first()

        assertThat(logsAfterDelete).hasSize(allLogs.size - 1)
        assertThat(logsAfterDelete).doesNotContain(logToDelete)
    }

    @Test
    fun deleteAllLogEntities_shouldClearTable() = runTest {
        val places = mockUnfavoritePlaceEntities.map { it.place }
        populateDatabase(places, 3)

        logDao.deleteAllLogEntities()
        val actualLogs = logDao.selectLogEntitiesByZipcode("").first()

        assertThat(actualLogs).isEmpty()
    }
}