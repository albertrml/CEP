package br.com.arml.cep.model

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.model.source.local.CepRoomDatabase
import br.com.arml.cep.model.source.local.PlaceDao
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CepSearchEntryTest {

    private lateinit var db: CepRoomDatabase
    private lateinit var dao: PlaceDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = CepRoomDatabase::class.java
        ).build()
        dao = db.placeDao()
    }

    @Before
    fun setupTestData() = runTest { db.clearAllTables() }

    @After
    fun closeDb() {
        db.close()
    }

    /*** Insert and check if the entry exists: create(), read(), readAll(), and readFavorites() ***/
    @Test
    fun testInsertAndRetrieveCepSearchEntry() = runTest {
        dao.create(mockPlaceEntries[0])
        val retrievedEntry = dao.read(mockPlaceEntries[0].cep.text)
        TestCase.assertEquals(mockPlaceEntries[0], retrievedEntry)
    }

    @Test
    fun shouldReturnsOnlyFavoriteCepSearchEntries_whenTryingToRetrieveFavorites() = runTest {
        mockPlaceEntries.forEach { dao.create(it) }
        val retrievedEntries = dao.readFavorites().first()
        retrievedEntries.forEach {
            assertTrue(
                "${it.cep.text} should be a favorite",
                it.isFavorite.value
            )
        }
    }

    @Test
    fun shouldThrowsException_whenTryingToInsertAnEntryWithTheSameCep() = runTest {
        dao.create(mockPlaceEntries[0])
        assertThrows(SQLiteConstraintException::class.java) {
            runBlocking {
                dao.create(mockPlaceEntries[0])
            }
        }
    }

    /*** Update an entry ***/
    @Test
    fun shouldChangeEntry_whenUpdateWithDifferentValues() = runTest {
        val oldEntry = mockPlaceEntries[0]
        val updateEntry = mockPlaceEntries[1].copy(cep = oldEntry.cep)
        dao.create(oldEntry)
        dao.update(updateEntry)
        dao.read(updateEntry.cep.text)?.let {
            assertEquals(updateEntry, it)
        }
    }

    @Test
    fun shouldNotInsertEntry_whenUpdatingAnEntryWithNewPrimaryKey() = runTest {
        val oldEntry = mockPlaceEntries[0]
        dao.update(oldEntry)
        val updateEntry = dao.read(oldEntry.cep.text)
        assertNull(updateEntry)
    }

    /*** Delete an entry ***/
    @Test
    fun shouldDeleteEntry_whenTryingToDelete() = runTest {
        val entry = mockPlaceEntries[0]
        dao.create(entry)
        dao.delete(entry)
        val retrievedEntry = dao.read(entry.cep.text)
        assertNull(retrievedEntry)
    }

    @Test
    fun shouldNotDeleteFavoriteEntries_whenTryingToDeleteAllNotFavorite() = runTest {
        mockPlaceEntries.forEach { dao.create(it) }
        dao.deleteAllNotFavorite()
        val retrievedEntries = dao.readAll().first()
        retrievedEntries.forEach {
            assertTrue(
                "${it.cep.text} is a favorite entry",
                it.isFavorite.value
            )
        }
    }

    /*** Filter entries by cep ***/
    @Test
    fun shouldFilterEntriesByCep_whenTryingToFilterByCep() = runTest {
        mockPlaceEntries.forEach { dao.create(it) }
        IntArray(10).forEach {
            val retrievedEntries = dao.filterByCep("$it".repeat(3)).first()
            assertEquals(1, retrievedEntries.size)
        }
    }

    @Test
    fun shouldReturnsEmptyList_whenTryingToFilterByCepAndNotExists() = runTest {
        val retrievedEntries = dao.filterByCep("111").first()
        assertEquals(0, retrievedEntries.size)
    }

    /*** Filter entries by cep and favorite ***/
    @Test
    fun shouldReturnsOnlyFavoriteEntriesByCep_whenTryingToFilterByCepAndFavorite() = runTest {
        mockPlaceEntries.forEach { dao.create(it) }
        IntArray(10).forEach {
            val retrievedEntries = dao.filterByCepAndFavorite("$it".repeat(3)).first()
            assertEquals(
                if (it > 4) 1 else 0,
                retrievedEntries.size
            )
        }
    }

    @Test
    fun shouldReturnsEmptyList_whenTryingToFilterByCepAndFavoriteNotExists() = runTest {
        mockPlaceEntries.forEach { dao.create(it) }
        IntArray(5).forEach {
            val retrievedEntries = dao.filterByCepAndFavorite("$it".repeat(3)).first()
            assertEquals(0, retrievedEntries.size)
        }
    }
}
