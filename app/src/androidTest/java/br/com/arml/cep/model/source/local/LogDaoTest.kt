package br.com.arml.cep.model.source.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.relation.PlaceWithLog
import br.com.arml.cep.model.mock.mockPlacesWithLog
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

    private suspend fun populateDatabase(
        places: List<PlaceEntity>,
        logsPerPlace: Int
    ): List<PlaceWithLog> {
        val result = mutableListOf<PlaceWithLog>()

        places.forEach { place ->
            val placeId = cacheDao.insertPlaceEntity(place)

            repeat(logsPerPlace) {
                val log = LogEntity(
                    placeId = placeId,
                    timestamp = System.currentTimeMillis()
                )

                val logId = logDao.insertLogEntity(log)

                val placeWithLog = PlaceWithLog(
                    place = place.copy(id = placeId),
                    log = log.copy(id = logId)
                )

                result.add(placeWithLog)
            }

        }

        return result
    }

    /** CREATE **/
    @Test
    fun insertLogEntity_shouldSaveLog_whenPlaceExists() = runTest {
        val (initialLog, place) = mockPlacesWithLog(1).first()

        val placeId = cacheDao.insertPlaceEntity(place)
        val expectedLog = initialLog.copy(placeId = placeId)
        val logId = logDao.insertLogEntity(expectedLog)

        val actualPlaceWithLogs = logDao.selectLogEntitiesByZipcode("").first()
        assertThat(actualPlaceWithLogs).hasSize(1)

        val actual = actualPlaceWithLogs.first()

        assertThat(actual.place.copy(id = 0, createdAt = 0)).isEqualTo(place.copy(id = 0, createdAt = 0))
        assertThat(actual.log).isEqualTo(expectedLog.copy(id = logId))
    }

    @Test
    fun insertLogEntity_shouldThrowException_whenPlaceDoesNotExist() = runTest {
        val logWithInvalidZipcode = LogEntity(placeId = 9999999, timestamp = 0L)
        assertFailsWith<SQLiteConstraintException> { logDao.insertLogEntity(logWithInvalidZipcode) }
    }


    /** READ **/
    @Test
    fun selectLogEntitiesByZipcode_shouldReturnFilteredLogs() = runTest {
        val places = mockUnfavoritePlaceEntities.map { it.place }
        val query = places.first().zipcode.substring(0, 5)
        val database = populateDatabase(places, 2)
        val expectedLogs = database.filter { it.place.zipcode.contains(query) }

        val actualLogs = logDao.selectLogEntitiesByZipcode(query).first()

        assertThat(actualLogs).containsExactlyElementsIn(expectedLogs)
    }

    @Test
    fun selectLogEntitiesByZipcode_shouldReturnEmptyList_whenZipcodeDoesNotExist() = runTest {
        val result = logDao.selectLogEntitiesByZipcode("123").first()

        assertThat(result).isEmpty()
    }

    @Test
    fun selectLogEntitiesByPeriod_shouldReturnLogsWithinPeriod() = runTest {
        val place = mockUnfavoritePlaceEntities.first().place
        val id = cacheDao.insertPlaceEntity(place)
        val timestamps = listOf(100L, 200L, 300L, 400L)
        timestamps.forEach {
            logDao.insertLogEntity(LogEntity(placeId = id, timestamp = it))
        }

        val actualLogs = logDao
            .selectLogEntitiesByPeriod(start = 150L, end = 350L)
            .first()
            .map { it.log.timestamp }
            .sorted()

        assertThat(actualLogs).hasSize(2)
        assertThat(actualLogs).containsExactly(200L, 300L).inOrder()
    }

    @Test
    fun selectLogEntitiesByPeriod_shouldReturnEmptyList_whenNoLogsMatchPeriod() = runTest {
        val place = mockUnfavoritePlaceEntities.first().place
        val id = cacheDao.insertPlaceEntity(place)
        val timestamps = listOf(100L, 200L, 300L, 400L)
        timestamps.forEach {
            logDao.insertLogEntity(LogEntity(placeId = id, timestamp = it))
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

        logDao.deleteLogEntity(logToDelete.log)
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