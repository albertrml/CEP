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
                favoriteDao.insertNoteEntityToFavorite(place.zipcode, note.copy(id = 0L))
            }
        }
    }

    // region Create tests
    @Test
    fun insertNoteEntityToFavorite_shouldInsertCorrectly() = runTest {
        val (expectedPlace, expectedNotes) = mockFavoritePlaceEntities.first()

        cacheDao.insertPlaceEntity(expectedPlace)
        expectedNotes.forEach { note -> favoriteDao.insertNoteEntityToFavorite(expectedPlace.zipcode, note) }

        val actualFavorite = favoriteDao.selectFavorite(expectedPlace.zipcode)

        assertThat(actualFavorite).isNotNull()
        actualFavorite?.run {
            assertThat(place).isEqualTo(expectedPlace)
            // Compare notes by ignoring the auto-generated ID
            assertThat(notes.map { it.copy(id = 0) })
                .containsExactlyElementsIn(expectedNotes.map { it.copy(id = 0) })
        } ?: fail("Favorite was not found!")
    }

    @Test
    fun insertNoteEntityToFavorite_shouldThrowException_whenPlaceDoesNotExist() = runTest {
        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.insertNoteEntityToFavorite("00000-000", mockNotes.first().toEntity())
        }
    }

    @Test
    fun insertNoteEntityToFavorite_shouldThrowException_whenNoteTitleIsNotUnique() = runTest {
        val (place, notes) = mockFavoritePlaceEntities.first()

        cacheDao.insertPlaceEntity(place)
        favoriteDao.createNote(notes.first()) // Insert note once

        // Try to insert it again via createFavorite, which will fail due to UNIQUE constraint
        assertFailsWith<SQLiteConstraintException> {
            favoriteDao.insertNoteEntityToFavorite(place.zipcode, notes.first())
        }
    }
    // endregion

    // region Read tests
    @Test
    fun selectFavoritesByTitle_shouldReturnCorrectlyFilteredMap() = runTest {
        populateDatabase()
        val query = "Title 3"
        val expectedData = mockFavoritePlaceEntities
            .asSequence()
            .map { it.copy(notes = it.notes.filter { note -> note.title.contains(query) }) }
            .filter { it.notes.isNotEmpty() }
            .associate { it.place to it.notes }

        val actualData = favoriteDao.selectFavoritesByTitle(query).first()

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
    fun selectFavoritesByTitle_shouldReturnEmptyMap_whenQueryDoesNotMatch() = runTest {
        populateDatabase()
        val result = favoriteDao.selectFavoritesByTitle("NonExistentQuery").first()
        assertThat(result).isEmpty()
    }

    @Test
    fun selectFavorite_shouldReturnCorrectItem() = runTest {
        val expectedFavorite = mockFavoritePlaceEntities.first()
        populateDatabase(listOf(expectedFavorite))

        val actualFavorite = favoriteDao.selectFavorite(expectedFavorite.place.zipcode)
        
        assertThat(actualFavorite).isNotNull()
        assertThat(actualFavorite?.place).isEqualTo(expectedFavorite.place)
        // Compare ignoring auto-generated IDs
        assertThat(actualFavorite?.notes?.map { it.copy(id = 0) })
            .containsExactlyElementsIn(expectedFavorite.notes.map { it.copy(id = 0) })
    }

    @Test
    fun selectFavorite_shouldReturnNull_whenZipcodeDoesNotExist() = runTest {
        val result = favoriteDao.selectFavorite("00000000")
        assertThat(result).isNull()
    }
    // endregion

    // region Update tests
    @Test
    fun updateNote_shouldUpdateNoteCorrectly() = runTest {
        populateDatabase(listOf(mockFavoritePlaceEntities.first()))
        val zipcode = mockFavoritePlaceEntities.first().place.zipcode
        val originalNote = favoriteDao.selectFavorite(zipcode)!!.notes.first()
        val updatedNote = originalNote.copy(
            title = "<<Updated Title>>",
            content = "<<Updated Content>>"
        )

        favoriteDao.updateNote(updatedNote)

        val favoriteAfterUpdate = favoriteDao.selectFavorite(zipcode)!!

        assertThat(favoriteAfterUpdate.notes).contains(updatedNote)
        assertThat(favoriteAfterUpdate.notes).doesNotContain(originalNote)
    }

    @Test
    fun updateNote_shouldDoNothing_whenNoteDoesNotExist() = runTest {
        populateDatabase()
        val nonExistentNote = NoteEntity(id = 9999L, title = "a", content = "b")
        val stateBeforeUpdate = favoriteDao.selectFavoritesByZipcode("").first()

        favoriteDao.updateNote(nonExistentNote)

        val stateAfterUpdate = favoriteDao.selectFavoritesByZipcode("").first()
        assertThat(stateAfterUpdate).isEqualTo(stateBeforeUpdate)
    }
    // endregion

    // region Delete tests
    @Test
    fun deleteFromFavorite_shouldRemoveNotesAndFavoriteLinkButKeepPlace() = runTest {
        populateDatabase(listOf(mockFavoritePlaceEntities.first()))
        val zipcode = mockFavoritePlaceEntities.first().place.zipcode

        favoriteDao.deleteFromFavorite(zipcode)

        val favoriteResult = favoriteDao.selectFavorite(zipcode)
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
        assertThat(actualData.map { it.place })
            .containsExactlyElementsIn(expectedData.map { it.place })
    }

    @Test
    fun exportFavorites_shouldReturnEmptyList_whenDatabaseIsEmpty() = runTest {
        val result = favoriteDao.exportFavorites().first()
        assertThat(result).isEmpty()
    }
    // endregion
}