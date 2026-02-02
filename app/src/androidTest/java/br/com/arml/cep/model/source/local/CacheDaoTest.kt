package br.com.arml.cep.model.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.arml.cep.model.mock.mockFavoritePlaceEntities
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CacheDaoTest {

    private lateinit var db: CepRoomDatabase
    private lateinit var cacheDao: CacheDao
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CepRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        cacheDao = db.cacheDao()
        favoriteDao = db.favoriteDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun selectCachedPlaceEntities_shouldReturnAllNonFavorites_whenDatabaseIsPopulated() = runTest {
        val expectedPlaces = mockUnfavoritePlaceEntities.map { it.place }
        expectedPlaces.forEach { entity -> cacheDao.insertPlaceEntity(entity) }

        val result = cacheDao.selectCachedPlaceEntitiesByZipcode("").first()

        assertThat(result).containsExactlyElementsIn(expectedPlaces).inOrder()
    }

    @Test
    fun selectCachedPlaceEntities_shouldReturnFilteredNonFavorites_whenFilteredByZipcode() = runTest {
        val allPlaces = mockUnfavoritePlaceEntities.map { it.place }
        allPlaces.forEach { entity -> cacheDao.insertPlaceEntity(entity) }

        val query = allPlaces.first().zipcode.substring(3, 5)
        val expectedPlaces = allPlaces.asSequence()
            .filter { it.zipcode.contains(query) }
            .sortedBy { it.zipcode }
            .toList()

        val result = cacheDao.selectCachedPlaceEntitiesByZipcode(query = query).first()

        assertThat(result).containsExactlyElementsIn(expectedPlaces).inOrder()
    }

    @Test
    fun selectCachedPlaceEntities_shouldReturnEmptyList_whenDatabaseIsEmpty() = runTest {
        val result = cacheDao.selectCachedPlaceEntitiesByZipcode("").first()
        assertThat(result).isEmpty()
    }

    @Test
    fun selectCachedPlaceEntities_shouldReturnOnlyNonFavoritePlaces_whenFavoritesExist() = runTest {
        val unfavoritePlace = mockUnfavoritePlaceEntities.first().place
        val (favoritePlace, notes) = mockFavoritePlaceEntities.last()
        cacheDao.insertPlaceEntity(unfavoritePlace)
        cacheDao.insertPlaceEntity(favoritePlace)
        notes.forEach { note ->
            favoriteDao.insertNoteEntityToFavorite(favoritePlace.zipcode, note.copy(id = 0))
        }

        val result = cacheDao.selectCachedPlaceEntitiesByZipcode("").first()

        assertThat(result).hasSize(1)
        assertThat(result).contains(unfavoritePlace)
        assertThat(result).doesNotContain(favoritePlace)
    }

    @Test
    fun deleteAllCachedPlaceEntities_shouldDeleteOnlyNonFavoritePlaces() = runTest {
        val unfavoritePlace = mockUnfavoritePlaceEntities.first().place
        val expectedFavorites = mockFavoritePlaceEntities.last()
        val (favoritePlace, notes) = expectedFavorites
        cacheDao.insertPlaceEntity(unfavoritePlace)
        cacheDao.insertPlaceEntity(favoritePlace)
        notes.forEach { note ->
            favoriteDao.insertNoteEntityToFavorite(favoritePlace.zipcode, note.copy(id = 0))
        }

        cacheDao.deleteAllCachedPlaceEntities()

        val cachedPlaces = cacheDao.selectCachedPlaceEntitiesByZipcode("").first()
        val actualFavorites = favoriteDao.selectFavoritesByZipcode("").first()

        assertThat(cachedPlaces).isEmpty()
        assertThat(actualFavorites).hasSize(1)
        assertThat(expectedFavorites).isEqualTo(actualFavorites.first())
    }

    @Test
    fun deleteCachedPlaceEntity_shouldDeleteNonFavoritePlace() = runTest {
        val unfavoritePlace = mockUnfavoritePlaceEntities.first().place
        cacheDao.insertPlaceEntity(unfavoritePlace)

        cacheDao.deleteCachedPlaceEntity(unfavoritePlace.zipcode)

        val result = cacheDao.selectCachedPlaceEntitiesByZipcode(unfavoritePlace.zipcode).first()
        assertThat(result).isEmpty()
    }

    @Test
    fun deleteCachedPlaceEntity_shouldNotDeleteFavoritePlace() = runTest {
        val expectedFavorite = mockFavoritePlaceEntities.first()
        val (favoritePlace, notes) = expectedFavorite
        cacheDao.insertPlaceEntity(favoritePlace)
        notes.forEach { note -> favoriteDao.insertNoteEntityToFavorite(favoritePlace.zipcode, note) }

        cacheDao.deleteCachedPlaceEntity(favoritePlace.zipcode)

        val actualFavorites = favoriteDao.selectFavorite(favoritePlace.zipcode)
        assertThat(actualFavorites).isNotNull()
        assertThat(actualFavorites).isEqualTo(expectedFavorite)
    }

    @Test
    fun update_shouldUpdatePlaceEntityNonFavoritePlaceCorrectly() = runTest {
        val oldPlace = mockUnfavoritePlaceEntities.first().place
        val newPlace = mockUnfavoritePlaceEntities.last().place.copy(zipcode = oldPlace.zipcode)
        cacheDao.insertPlaceEntity(oldPlace)

        cacheDao.updatePlaceEntity(newPlace)

        val result = cacheDao.selectCachedPlaceEntityByZipcode(oldPlace.zipcode)
        
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(newPlace)
    }
}