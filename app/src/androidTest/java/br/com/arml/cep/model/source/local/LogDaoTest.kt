package br.com.arml.cep.model.source.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
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
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            ctx,
            CepRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        logDao = db.logDao()
        cacheDao = db.cacheDao()
        favoriteDao = db.favoriteDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    /** CREATE **/
    @Test
    fun insert_shouldInsertALogIntoTheDatabase() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first().toEntity()
        cacheDao.insert(place)
        val log = LogEntity(
            zipcodePlace = place.zipcode,
            timestamp = System.currentTimeMillis()
        )

        // Act
        logDao.insert(log)

        // Assert
        val allLogs = logDao.getLogByZipcode("").first()
        assertThat(allLogs).hasSize(1)
        assertThat(allLogs[0].zipcodePlace).isEqualTo(place.zipcode)
    }

    @Test
    fun insert_shouldThrowSQLiteConstraintException_whenPlaceDoesNotExist() = runTest {
        // Arrange
        val logWithInvalidZipcode = LogEntity(
            zipcodePlace = "99999999",
            timestamp = 0L
        )

        // Act & Assert
        assertFailsWith<SQLiteConstraintException> { logDao.insert(logWithInvalidZipcode) }
    }

    /** READ **/
    @Test
    fun getLogByZipcode_shouldReturnOnlyLogsWithMatchingZipcode() = runTest {
        // Arrange
        val query = mockUnfavoritePlaces.first().cep.text.substring(0,3)
        val expectedElements = mockUnfavoritePlaces
            .map { it.toEntity() }
            .filter { it.zipcode.contains(query) }

        mockUnfavoritePlaces.forEachIndexed { index, it ->
            cacheDao.insert(it.toEntity())
            val logEntity = LogEntity(
                zipcodePlace = it.cep.text,
                timestamp = index.toLong()
            )
            logDao.insert(logEntity)
        }

        // Act
        val result = logDao.getLogByZipcode(query).first()

        // Assert
        assertThat(result).hasSize(expectedElements.size)
        assertThat(result).isEqualTo(expectedElements)
    }

    @Test
    fun getLogByZipcode_shouldReturnEmptyList_whenZipcodeDoesNotExist() = runTest {
        // Act
        val result = logDao.getLogByZipcode("123").first()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun getLogByPeriod_shouldReturnOnlyLogsWithinTheGivenPeriod() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first().toEntity()
        cacheDao.insert(place)
        logDao.insert(LogEntity(zipcodePlace = place.zipcode, timestamp = 100L))
        logDao.insert(LogEntity(zipcodePlace = place.zipcode, timestamp = 200L))
        logDao.insert(LogEntity(zipcodePlace = place.zipcode, timestamp = 300L))
        logDao.insert(LogEntity(zipcodePlace = place.zipcode, timestamp = 400L))

        // Act: Busca por logs entre 150 e 350
        val result = logDao.getLogByPeriod(start = 150L, end = 350L).first()

        // Assert
        assertThat(result).hasSize(2)
        assertThat(result.all { it.timestamp in 150L..350L }).isTrue()
    }

    @Test
    fun getLogByPeriod_shouldReturnEmptyList_whenNoLogsMatchThePeriod() = runTest {
        // Act
        val result = logDao.getLogByPeriod(start = 100L, end = 200L).first()

        // Assert
        assertThat(result).isEmpty()
    }
}