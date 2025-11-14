package br.com.arml.cep.model.repository

import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.exception.BackupException
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.mock.mockPlaceWithNotes
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryTest {
    private val favoriteDao: FavoriteDao = mockk()
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        favoriteRepository = FavoriteRepository(favoriteDao)
    }

    /** Insert tests **/
    @Test
    fun `addToFavorite should Emits Loading and Success when dao call is successful`() = runTest {
        // Arrange
        val (place, notes) = mockPlaceWithNotes(1).first()
        val note = notes.first()
        coJustRun { favoriteDao.createFavorite(place.zipcode, note) }

        // Act
        val responses = favoriteRepository.addToFavorite(place.zipcode, note).toList()

        // Assert
        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { favoriteDao.createFavorite(place.zipcode, note) }
    }

    @Test
    fun `addToFavorite should Emits Loading and Failure when dao throws Exception`() = runTest {
        // Arrange
        val (place, notes) = mockPlaceWithNotes(1).first()
        val note = notes.first()
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.createFavorite(place.zipcode, note) } throws expectedException

        // Act
        val responses = favoriteRepository.addToFavorite(place.zipcode, note).toList()

        // Assert
        responses.assertFlowFailure {
            assertEquals(expectedException.javaClass, it.javaClass)
        }
        coVerify(exactly = 1) { favoriteDao.createFavorite(place.zipcode, note) }
    }
    /** End Insert tests **/

    /** Fetch tests **/
    @Test
    fun `getFavoritesByTitle should Emits Loading and Failure when readFavoritesByTitle returns a map`() =
        runTest {
            // Arrange
            val data = mockPlaceWithNotes(5)
            val mapPlaceWithNotes = data.associate { it.place to it.notes }
            val expected = data.map { it.toModel() }
            coEvery {
                favoriteDao.readFavoritesByTitle(any())
            } returns flowOf(mapPlaceWithNotes)

            // Act
            val responses = favoriteRepository.getFavoritesByTitle("").toList()

            // Assert
            responses.assertFlowSuccess { actual -> assertEquals(expected, actual) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByTitle("") }
        }

    @Test
    fun `getFavoritesByTitle should Emits Loading and Success when readFavoriteByTitle returns a empty map`() =
        runTest {
            // Arrange
            coEvery {
                favoriteDao.readFavoritesByTitle(any())
            } returns flowOf(emptyMap())

            // Act
            val responses = favoriteRepository.getFavoritesByTitle("").toList()

            // Assert
            responses.assertFlowSuccess { actual -> assertEquals(emptyList<Place>(), actual) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByTitle("") }
        }

    @Test
    fun `getFavoritesByTitle should Emits Loading and Failure when readFavoritesByTitle throws Exception`() =
        runTest {
            // Arrange
            coEvery {
                favoriteDao.readFavoritesByTitle(any())
            } throws SQLiteException("Test DB Error")

            // Act
            val responses = favoriteRepository.getFavoritesByTitle("").toList()

            // Assert
            responses.assertFlowFailure { assert(it is SQLiteException) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByTitle("") }
        }

    @Test
    fun `getFavoritesByZipcode should Emits Loading and Success when readFavoritesByZipcode returns a list`() =
        runTest {
            // Arrange
            val data = mockPlaceWithNotes(5)
            val expected = data.map { it.toModel() }
            coEvery {
                favoriteDao.readFavoritesByZipcode(any())
            } returns flowOf(data)

            // Act
            val responses = favoriteRepository.getFavoritesByZipcode("").toList()

            // Assert
            responses.assertFlowSuccess { actual -> assertEquals(expected, actual) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByZipcode("") }
        }

    @Test
    fun `getFavoritesByZipcode should Emits Loading and Success when readFavoritesByZipcode returns a empty list`() =
        runTest {
            // Arrange
            coEvery {
                favoriteDao.readFavoritesByZipcode(any())
            } returns flowOf(emptyList())

            // Act
            val responses = favoriteRepository.getFavoritesByZipcode("").toList()

            // Assert
            responses.assertFlowSuccess { actual -> assertEquals(emptyList<Place>(), actual) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByZipcode("") }
        }

    @Test
    fun `getFavoritesByZipcode should Emits Loading and Failure when readFavoritesByZipcode throws Exception`() =
        runTest {
            // Arrange
            coEvery {
                favoriteDao.readFavoritesByZipcode(any())
            } throws SQLiteException("Test DB Error")

            // Act
            val responses = favoriteRepository.getFavoritesByZipcode("").toList()

            // Assert
            responses.assertFlowFailure { assert(it is SQLiteException) }
            coVerify(exactly = 1) { favoriteDao.readFavoritesByZipcode("") }
        }
    /** End Fetch tests **/

    /** Update tests **/
    @Test
    fun `updateNoteFromFavorite should Emits Loading and Success when updateNoteFromFavorite returns Unit`() =
        runTest {
            val note = mockNotes.first().toEntity()
            coJustRun { favoriteDao.updateNote(note) }

            val responses = favoriteRepository.updateNoteFromFavorite(note).toList()

            responses.assertFlowSuccess { assertEquals(Unit, it) }
        }

    @Test
    fun `updateNoteFromFavorite should Emits Loading and Failure when updateNoteFromFavorite throws Exception`() =
        runTest {
            val note = mockNotes.first().toEntity()
            coEvery { favoriteDao.updateNote(note) } throws SQLiteException("Test DB Error")

            val responses = favoriteRepository.updateNoteFromFavorite(note).toList()

            responses.assertFlowFailure { assert(it is SQLiteException) }
        }
    /** End Update tests **/

    /** Delete tests **/
    @Test
    fun `deleteFromFavorite should Emits Loading and Success when deleteFavorite returns Unit`() =
        runTest {
            val place = mockPlaceWithNotes(1).first().place
            coJustRun { favoriteDao.deleteFromFavorite(place.zipcode) }

            val responses = favoriteRepository.deleteFromFavorite(place.zipcode).toList()

            responses.assertFlowSuccess { assertEquals(Unit, it) }
        }

    @Test
    fun `deleteFromFavorite should Emits Loading and Failure when deleteFavorite throws Exception`() =
        runTest {
            val place = mockPlaceWithNotes(1).first().place
            coEvery {
                favoriteDao.deleteFromFavorite(place.zipcode)
            } throws SQLiteException("Test DB Error")

            val responses = favoriteRepository.deleteFromFavorite(place.zipcode).toList()

            responses.assertFlowFailure { assert(it is SQLiteException) }
        }

    @Test
    fun `deleteNoteFromFavorite should Emits Loading and Success when deleteNoteFromFavorite returns Unit`() =
        runTest {
            val (place, notes) = mockPlaceWithNotes(1).first()
            val note = notes.first()
            val zipcode = place.zipcode
            coJustRun { favoriteDao.deleteNote(note) }

            val responses = favoriteRepository.deleteNoteFromFavorite(zipcode, note).toList()

            responses.assertFlowSuccess { assertEquals(Unit, it) }
        }

    @Test
    fun `deleteNoteFromFavorite should Emits Loading and Failure when deleteNoteFromFavorite throws Exception`() =
        runTest {
            val (place, notes) = mockPlaceWithNotes(1).first()
            val note = notes.first()
            val zipcode = place.zipcode

            coEvery { favoriteDao.deleteNote(note) } throws SQLiteException("Test DB Error")

            val responses = favoriteRepository.deleteNoteFromFavorite(zipcode, note).toList()

            responses.assertFlowFailure { assert(it is SQLiteException) }
        }
    /** End Delete tests **/

    /** Export tests **/
    @Test
    fun `exportFavorites should Emits Loading and Success when exportFavorites returns list of favorites`() =
        runTest {
            val data = mockPlaceWithNotes(5)
            val expected = data.map { it.toModel() }
            coEvery {
                favoriteDao.exportFavorites()
            } returns flowOf(data)

            val responses = favoriteRepository.exportFavorites().toList()

            responses.assertFlowSuccess { actual -> assertEquals(expected, actual) }
        }

    @Test
    fun `exportFavorites should Emits Loading and Failure when exportFavorites throws Exception`() =
        runTest {
            coEvery {
                favoriteDao.exportFavorites()
            } throws SQLiteException("Test DB Error")

            val responses = favoriteRepository.exportFavorites().toList()

            responses.assertFlowFailure { assert(it is SQLiteException) }
        }
    /** End Export tests **/

    /** Import tests **/
    @Test
    fun `importFavorites should Emits Loading and Success when importFavorites returns Unit`() =
        runTest {
            val data = mockFavoritePlaces
            val expectedPlaceInsert = data.size
            val expectedNoteInsert = data.sumOf { it.notes.size }
            coJustRun { favoriteDao.insertPlace(any()) }
            coJustRun { favoriteDao.createFavorite(any(), any()) }

            val responses = favoriteRepository.importFavorites(data).toList()

            responses.assertFlowSuccess { assertEquals(Unit, it) }
            coVerify(exactly = expectedPlaceInsert) { favoriteDao.insertPlace(any()) }
            coVerify(exactly = expectedNoteInsert) { favoriteDao.createFavorite(any(), any()) }
        }

    @Test
    fun `importFavorites should Emits Loading and Failure when importFavorites throws ImportEmptyFavoriteException`() =
        runTest {
            val expectedException = SQLiteException("Insert Place Error")
            val data = mockFavoritePlaces
            val expectedPlaceInsert = 1
            val expectedNoteInsert = 0
            coEvery {
                favoriteDao.insertPlace(any())
            } answers { throw expectedException }
            coJustRun { favoriteDao.createFavorite(any(), any()) }

            val responses = favoriteRepository.importFavorites(data).toList()

            responses.assertFlowFailure {
                assertEquals(expectedException.javaClass, it.javaClass)
            }
            coVerify(exactly = expectedPlaceInsert) { favoriteDao.insertPlace(any()) }
            coVerify(exactly = expectedNoteInsert) { favoriteDao.createFavorite(any(), any()) }
        }

    @Test
    fun `importFavorites should Emits Loading and Failure when insertPlace throws Exception`() =
        runTest {
            val expectedException = SQLiteException("Insert Note Error")
            val data = mockFavoritePlaces
            val expectedPlaceInsert = 1
            val expectedNoteInsert = 1
            coJustRun { favoriteDao.insertPlace(any()) }
            coEvery {
                favoriteDao.createFavorite(any(), any())
            } answers { throw expectedException }

            val responses = favoriteRepository.importFavorites(data).toList()

            responses.assertFlowFailure {
                assertEquals(expectedException.javaClass, it.javaClass)
            }
            coVerify(exactly = expectedPlaceInsert) { favoriteDao.insertPlace(any()) }
            coVerify(exactly = expectedNoteInsert) { favoriteDao.createFavorite(any(), any()) }
        }

    @Test
    fun `importFavorites should Emits Loading and Failure when throws ImportEmptyFavoriteException`() =
        runTest {
            val expectedException = BackupException.ImportEmptyFavoriteException()
            val data = mockFavoritePlaces
            val expectedPlaceInsert = 1
            val expectedNoteInsert = 1
            coJustRun { favoriteDao.insertPlace(any()) }
            coEvery {
                favoriteDao.createFavorite(any(), any())
            } answers { throw expectedException }

            val responses = favoriteRepository.importFavorites(data).toList()

            responses.assertFlowFailure {
                assertEquals(expectedException.javaClass, it.javaClass)
            }
            coVerify(exactly = expectedPlaceInsert) { favoriteDao.insertPlace(any()) }
            coVerify(exactly = expectedNoteInsert) { favoriteDao.createFavorite(any(), any()) }
        }
    /** End Import tests **/
}