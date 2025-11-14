package br.com.arml.cep.model.source.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.mock.mockPlaceWithNotes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteDaoTest {
    private lateinit var db: CepRoomDatabase
    private lateinit var cacheDao: CacheDao
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, CepRoomDatabase::class.java)
            .allowMainThreadQueries().build()
        cacheDao = db.cacheDao()
        favoriteDao = db.favoriteDao()
    }

    @After
    fun tearDown() { db.close() }

    private suspend fun populateDatabase(data: List<PlaceWithNotes> = mockPlaceWithNotes(10)) {
        data.forEach { placeWithNotes ->
            cacheDao.insert(placeWithNotes.place)
            placeWithNotes.notes.forEach { note ->
                favoriteDao.createFavorite(placeWithNotes.place.zipcode, note)
            }
        }
    }

    /** Create tests **/
    @Test
    fun createFavorite_shouldInsertAFavorite_whenPlaceAlreadyExists() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()

        cacheDao.insert(place)
        notes.forEach { note -> favoriteDao.createFavorite(place.zipcode, note) }

        val actualFavorite = favoriteDao.readAFavoriteWithNotes(place.zipcode)

        assertNotNull(actualFavorite)
        assertEquals(place, actualFavorite.place)
        assertEquals(notes.size, actualFavorite.notes.size)
        assertTrue(actualFavorite.notes.containsAll(notes))
        assertTrue(notes.containsAll(actualFavorite.notes))
    }

    @Test
    fun createFavorite_shouldThrowException_whenPlaceDoesNotExist() = runTest {
        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.createFavorite("00000000", mockNotes.first().toEntity())
        }
    }

    @Test
    fun createFavorite_shouldThrowException_whenInsertNoteHasFailed() = runTest {
        //val place = mockFavoritePlaces.first().toEntity()
        //val note = mockNotes.first().toEntity()
        val (place, notes) = mockPlaceWithNotes(1).first()

        cacheDao.insert(place)
        favoriteDao.createNote(notes.first())

        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.createFavorite(place.zipcode, notes.first())
        }
    }
    /** End create tests **/

    /** Read tests **/
    @Test
    fun readFavoritesByTitle_shouldReturnAllFavorites_whenQueryIsEmpty() = runTest {
        val favoriteSet = mockPlaceWithNotes(10)
        populateDatabase(favoriteSet)
        val expected = favoriteSet.associate { it.place to it.notes }

        val actual = favoriteDao.readFavoritesByTitle("").first()

        assertEquals(expected, actual)
    }

    @Test
    fun readFavoritesByTitle_shouldReturnFilteredFavorites_whenQueryMatches() = runTest {
        val data = mockPlaceWithNotes(5)
        populateDatabase(data)
        val query = "Title 3"
        val expected = data
            .asSequence()
            .map { it.copy(notes = it.notes.filter { note -> note.title.contains(query) }) }
            .filter { it.notes.isNotEmpty() }
            .associate { it.place to it.notes }

        val actual = favoriteDao.readFavoritesByTitle(query).first()

        assertEquals(expected, actual)
    }

    @Test
    fun readFavoritesByTitle_shouldReturnEmptyMap_whenQueryDoesNotMatch() = runTest {
        populateDatabase()
        val actual = favoriteDao.readFavoritesByTitle("NonExistentQuery").first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun readFavoritesByTitle_shouldReturnEmptyMap_whenDbIsEmpty() = runTest {
        val actual = favoriteDao.readFavoritesByTitle("").first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun readAFavoriteWithNotes_shouldReturnItem_whenZipcodeExists() = runTest {
        val data = mockPlaceWithNotes(5)
        populateDatabase(data)
        val expected = data.first()
        val actual = favoriteDao.readAFavoriteWithNotes(expected.place.zipcode)
        assertEquals(expected, actual)
    }

    @Test
    fun readAFavoriteWithNotes_shouldReturnNull_whenZipcodeDoesNotExist() = runTest {
        val actual = favoriteDao.readAFavoriteWithNotes("00000000")
        assertNull(actual)
    }

    /** End read tests **/

    /** Update tests **/
    @Test
    fun updateNote_shouldUpdateNote_whenNoteExists() = runTest {
        val data = mockPlaceWithNotes(5)
        populateDatabase(data)
        val zipcode = data.first().place.zipcode
        val favorite = favoriteDao.readAFavoriteWithNotes(zipcode)
        assertNotNull(favorite)

        val originalNote = favorite.notes.first()
        val updatedNote = originalNote.copy(title = "<<Updated Title>>")

        favoriteDao.updateNote(updatedNote)

        val updatedFavorite = favoriteDao.readAFavoriteWithNotes(favorite.place.zipcode)
        assertNotNull(updatedFavorite)
        assertTrue(updatedFavorite.notes.contains(updatedNote))
    }

    @Test
    fun updateNote_shouldDoNothing_whenNoteDoesNotExist() = runTest {
        populateDatabase()
        val nonExistentNote = NoteEntity(id = 999, title = "a", content = "b")
        val beforeUpdate = favoriteDao.readFavoritesByZipcode("").first()

        favoriteDao.updateNote(nonExistentNote)

        val afterUpdate = favoriteDao.readFavoritesByZipcode("").first()
        assertEquals(beforeUpdate, afterUpdate)
    }
    /** End update tests **/

    /** Delete tests **/
    @Test
    fun deleteFromFavorite_shouldDeleteNotesButKeepPlace() = runTest {
        populateDatabase()
        val zipcode = mockPlaceWithNotes(1).first().place.zipcode
        val before = favoriteDao.readAFavoriteWithNotes(zipcode)
        assertNotNull(before) { "Place should exist" }

        favoriteDao.deleteFromFavorite(zipcode)

        val actual = favoriteDao.readAFavoriteWithNotes(zipcode)
        assertNotNull(actual) {"Place still exists, but it must be deleted"}
        assertTrue("Notes should be deleted") { actual.notes.isEmpty() }
    }
    /** End Delete tests **/

    /** Export tests **/
    @Test
    fun exportFavorites_shouldReturnAllFavorites_whenDatabaseIsNotEmpty() = runTest {
        val expected = mockPlaceWithNotes(5)
        populateDatabase(expected)

        val actual = favoriteDao.exportFavorites().first()

        assertEquals(expected.size, actual.size)
        assertTrue(actual.containsAll(expected))
        assertTrue(expected.containsAll(actual))
    }

    @Test
    fun exportFavorites_shouldReturnEmptyList_whenDatabaseIsEmpty() = runTest {
        val favorites = favoriteDao.exportFavorites().first()
        assertTrue(favorites.isEmpty())
    }
    /** End Export tests **/
}