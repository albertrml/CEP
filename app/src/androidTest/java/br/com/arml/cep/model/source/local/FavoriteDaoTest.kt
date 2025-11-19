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
import br.com.arml.cep.model.mock.mockFavoritePlaceEntities
import br.com.arml.cep.model.mock.mockNotes
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith
import kotlin.test.fail

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

    private suspend fun populateDatabase(data: List<PlaceWithNotes> = mockFavoritePlaceEntities) {
        data.forEach { (place, notes) ->
            cacheDao.insertPlaceEntity(place)
            notes.forEach { note ->
                // Reset ID to 0 to let Room auto-generate it, simulating a new insertion.
                favoriteDao.createFavorite(place.zipcode, note.copy(id = 0L))
            }
        }
    }

    // region Create tests
    @Test
    fun createFavorite_shouldInsertCorrectly() = runTest {
        val (expectedPlace, expectedNotes) = mockFavoritePlaceEntities.first()

        cacheDao.insertPlaceEntity(expectedPlace)
        expectedNotes.forEach { note -> favoriteDao.createFavorite(expectedPlace.zipcode, note) }

        val actualFavorite = favoriteDao.readAFavoriteWithNotes(expectedPlace.zipcode)

        assertThat(actualFavorite).isNotNull()
        actualFavorite?.run {
            assertThat(place).isEqualTo(expectedPlace)
            // Compare notes by ignoring the auto-generated ID
            assertThat(notes.map { it.copy(id = 0) })
                .containsExactlyElementsIn(expectedNotes.map { it.copy(id = 0) })
        } ?: fail("Favorite was not found!")
    }

    @Test
    fun createFavorite_shouldThrowException_whenPlaceDoesNotExist() = runTest {
        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.createFavorite("00000-000", mockNotes.first().toEntity())
        }
    }

    @Test
    fun createFavorite_shouldThrowException_whenNoteTitleIsNotUnique() = runTest {
        val (place, notes) = mockFavoritePlaceEntities.first()

        cacheDao.insertPlaceEntity(place)
        favoriteDao.createNote(notes.first()) // Insert note once

        // Try to insert it again via createFavorite, which will fail due to UNIQUE constraint
        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.createFavorite(place.zipcode, notes.first())
        }
    }
    // endregion

    // region Read tests
    @Test
    fun readFavoritesByTitle_shouldReturnCorrectlyFilteredMap() = runTest {
        populateDatabase()
        val query = "Title 3"
        val expectedData = mockFavoritePlaceEntities
            .asSequence()
            .map { it.copy(notes = it.notes.filter { note -> note.title.contains(query) }) }
            .filter { it.notes.isNotEmpty() }
            .associate { it.place to it.notes }

        val actualData = favoriteDao.readFavoritesByTitle(query).first()

        assertThat(actualData.keys).isEqualTo(expectedData.keys)
        actualData.forEach { (place, notes) ->
            val expectedNotes = expectedData[place]
            assertThat(expectedNotes).isNotNull()
            // Compare ignoring auto-generated IDs
            assertThat(notes.map { it.copy(id = 0) })
                .containsExactlyElementsIn(expectedNotes!!.map { it.copy(id = 0) })
        }
    }

    @Test
    fun readFavoritesByTitle_shouldReturnEmptyMap_whenQueryDoesNotMatch() = runTest {
        populateDatabase()
        val result = favoriteDao.readFavoritesByTitle("NonExistentQuery").first()
        assertThat(result).isEmpty()
    }

    @Test
    fun readAFavoriteWithNotes_shouldReturnCorrectItem() = runTest {
        val expectedFavorite = mockFavoritePlaceEntities.first()
        populateDatabase(listOf(expectedFavorite))

        val actualFavorite = favoriteDao.readAFavoriteWithNotes(expectedFavorite.place.zipcode)
        
        assertThat(actualFavorite).isNotNull()
        assertThat(actualFavorite?.place).isEqualTo(expectedFavorite.place)
        // Compare ignoring auto-generated IDs
        assertThat(actualFavorite?.notes?.map { it.copy(id = 0) })
            .containsExactlyElementsIn(expectedFavorite.notes.map { it.copy(id = 0) })
    }

    @Test
    fun readAFavoriteWithNotes_shouldReturnNull_whenZipcodeDoesNotExist() = runTest {
        val result = favoriteDao.readAFavoriteWithNotes("00000000")
        assertThat(result).isNull()
    }
    // endregion

    // region Update tests
    @Test
    fun updateNote_shouldUpdateNoteCorrectly() = runTest {
        populateDatabase(listOf(mockFavoritePlaceEntities.first()))
        val zipcode = mockFavoritePlaceEntities.first().place.zipcode
        val originalNote = favoriteDao.readAFavoriteWithNotes(zipcode)!!.notes.first()
        val updatedNote = originalNote.copy(title = "<<Updated Title>>", content = "<<Updated Content>>")

        favoriteDao.updateNote(updatedNote)

        val favoriteAfterUpdate = favoriteDao.readAFavoriteWithNotes(zipcode)!!
        
        assertThat(favoriteAfterUpdate.notes).contains(updatedNote)
        assertThat(favoriteAfterUpdate.notes).doesNotContain(originalNote)
    }

    @Test
    fun updateNote_shouldDoNothing_whenNoteDoesNotExist() = runTest {
        populateDatabase()
        val nonExistentNote = NoteEntity(id = 9999L, title = "a", content = "b")
        val stateBeforeUpdate = favoriteDao.readFavoritesByZipcode("").first()

        favoriteDao.updateNote(nonExistentNote)

        val stateAfterUpdate = favoriteDao.readFavoritesByZipcode("").first()
        assertThat(stateAfterUpdate).isEqualTo(stateBeforeUpdate)
    }
    // endregion

    // region Delete tests
    @Test
    fun deleteFromFavorite_shouldRemoveNotesAndFavoriteLinkButKeepPlace() = runTest {
        populateDatabase(listOf(mockFavoritePlaceEntities.first()))
        val zipcode = mockFavoritePlaceEntities.first().place.zipcode

        favoriteDao.deleteFromFavorite(zipcode)

        val favoriteResult = favoriteDao.readAFavoriteWithNotes(zipcode)
        val placeResult = cacheDao.selectCachedPlaceEntityByZipcode(zipcode)

        assertThat(favoriteResult).isNull()
        assertThat(placeResult).isNotNull()
    }
    // endregion

    // region Export tests
    @Test
    fun exportFavorites_shouldReturnAllFavorites_whenDatabaseIsNotEmpty() = runTest {
        val expectedData = mockFavoritePlaceEntities
        populateDatabase(expectedData)

        val actualData = favoriteDao.exportFavorites().first()

        // Compare ignoring the auto-generated IDs
        assertThat(actualData.size).isEqualTo(expectedData.size)
        assertThat(actualData.map { it.place }).containsExactlyElementsIn(expectedData.map { it.place })
    }

    @Test
    fun exportFavorites_shouldReturnEmptyList_whenDatabaseIsEmpty() = runTest {
        val result = favoriteDao.exportFavorites().first()
        assertThat(result).isEmpty()
    }
    // endregion
}