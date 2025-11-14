package br.com.arml.cep.model.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CacheDaoTest {

    private lateinit var db: CepRoomDatabase
    /* All tables are necessary to build the CepRoomDatabase, but this test is only using the
       cache table for testing. */
    private lateinit var cacheDao: CacheDao
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var logDao: LogDao


    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CepRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        cacheDao = db.cacheDao()
        favoriteDao = db.favoriteDao()
        logDao = db.logDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetByZipcode_shouldReturnCorrectPlace_whenPlaceIsStoredAndFetched() = runTest {
        val expectedPlaces = mockUnfavoritePlaces.map{ it.toEntity() }
        expectedPlaces.forEach{ entity -> cacheDao.insert(entity) }

        cacheDao.getByZipcode("").collectLatest { result ->
            assertTrue(
                """The fetched places should have the same size as the inserted ones and not be empty""",
                result.size == expectedPlaces.size && result.isNotEmpty()
            )
            assertTrue(
                "The fetched places should be the same as the inserted ones.",
                expectedPlaces.containsAll(result)
            )
        }
    }

    @Test
    fun deleteAllUnwanted_shouldDeleteOnlyNonFavoritePlaces() = runTest {
        val unfavoritePlaceEntity = mockUnfavoritePlaces[0].toEntity()
        val favoritePlace = mockFavoritePlaces[0]
        val favoritePlaceEntity = favoritePlace.toEntity()
        val noteEntity = favoritePlace.notes.first().toEntity()
        cacheDao.insert(unfavoritePlaceEntity)
        cacheDao.insert(favoritePlaceEntity)
        favoriteDao.createFavorite(favoritePlaceEntity.zipcode, noteEntity)
        cacheDao.deleteAllUnwanted()
        val allPlaces = cacheDao.getByZipcode("").first()
        assertThat(allPlaces).hasSize(1)
        assertThat(allPlaces).contains(favoritePlaceEntity)
    }

    @Test
    fun deleteIfUnfavorite_doesNotDeleteAFavoritePlace() = runTest {
        // Arrange: Insere um lugar e o torna favorito
        val expectedPlace = mockFavoritePlaces[0].toEntity()
        val noteEntity = mockFavoritePlaces[0].notes.first().toEntity()
        cacheDao.insert(expectedPlace)
        favoriteDao.createFavorite(expectedPlace.zipcode, noteEntity)

        // Act: Tenta deletar o lugar favorito com o método condicional
        cacheDao.deleteIfUnfavorite(expectedPlace.zipcode)

        // Assert: Verifica se o lugar ainda existe na tabela
        val allPlaces = cacheDao.getByZipcode(expectedPlace.zipcode).first()
        assertThat(allPlaces).hasSize(1)
        assertThat(allPlaces).contains(expectedPlace)
    }

    @Test
    fun update_shouldUpdateCorrectly_whenPlaceIsUpdated() = runTest {
        val oldPlace = mockUnfavoritePlaces[0].toEntity()
        val expectedPlace = mockUnfavoritePlaces[1].toEntity().copy(zipcode = oldPlace.zipcode)
        cacheDao.insert(expectedPlace)
        cacheDao.update(expectedPlace)
        cacheDao.getByZipcode(expectedPlace.zipcode).collectLatest { result ->
            assertTrue(
                """The fetched places should have the same size as the inserted ones and not be empty""",
                result.size == 1
            )
            assertTrue(
                "The fetched places should be the same as the inserted ones.",
                result.contains(expectedPlace)
            )
            assertFalse(
                "The fetched places should not be the same as the old ones.",
                result.contains(oldPlace)
            )
        }
    }
}