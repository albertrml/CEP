package br.com.arml.cep.model.repository

import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.exception.BackupException
import br.com.arml.cep.model.exception.CepDatabaseException.IllegalNoteQuantity
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.mock.mockPlaceWithNotes
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryTest {
    private val favoriteDao: FavoriteDao = mockk()
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        favoriteRepository = FavoriteRepository(favoriteDao)
    }

    // region Insert tests
    @Test
    fun `addToFavorite should Emit Success when dao call is successful`() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()
        val note = notes.first()
        coJustRun { favoriteDao.insertNoteEntityToFavorite(any(), any()) }

        val responses = favoriteRepository.addToFavorite(place.zipcode, note).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { favoriteDao.insertNoteEntityToFavorite(place.zipcode, note) }
    }

    @Test
    fun `addToFavorite should Emit Failure when dao throws Exception`() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()
        val note = notes.first()
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.insertNoteEntityToFavorite(any(), any()) } throws expectedException

        val responses = favoriteRepository.addToFavorite(place.zipcode, note).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.insertNoteEntityToFavorite(place.zipcode, note) }
    }
    // endregion

    // region Fetch tests
    @Test
    fun `getFavoritesByTitle should Emit Success with places when dao returns a map`() = runTest {
        val data = mockPlaceWithNotes(5)
        val mapData = data.associate { it.place to it.notes }
        val expected = data.map { it.toModel() }
        coEvery { favoriteDao.selectFavoritesByTitle(any()) } returns flowOf(mapData)

        val responses = favoriteRepository.getFavoritesByTitle("").toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expected) }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByTitle("") }
    }

    @Test
    fun`getFavoritesByTitle should Emit Success with empty list when dao returns an empty map`() = runTest {
        coEvery { favoriteDao.selectFavoritesByTitle(any()) } returns flowOf(emptyMap())

        val responses = favoriteRepository.getFavoritesByTitle("").toList()

        responses.assertFlowSuccess { assertThat(it).isEmpty() }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByTitle("") }
    }

    @Test
    fun `getFavoritesByTitle should Emit Failure when dao throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.selectFavoritesByTitle(any()) } returns flow { throw expectedException }

        val responses = favoriteRepository.getFavoritesByTitle("").toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByTitle("") }
    }

    @Test
    fun `getFavoritesByZipcode should Emit Success with places when dao returns a list`() = runTest {
        val data = mockPlaceWithNotes(5)
        val expected = data.map { it.toModel() }
        coEvery { favoriteDao.selectFavoritesByZipcode(any()) } returns flowOf(data)

        val responses = favoriteRepository.getFavoritesByZipcode("").toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expected) }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByZipcode("") }
    }

    @Test
    fun `getFavoritesByZipcode should Emit Success with empty list when dao returns an empty list`() = runTest {
        coEvery { favoriteDao.selectFavoritesByZipcode(any()) } returns flowOf(emptyList())

        val responses = favoriteRepository.getFavoritesByZipcode("").toList()

        responses.assertFlowSuccess { assertThat(it).isEmpty() }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByZipcode("") }
    }

    @Test
    fun `getFavoritesByZipcode should Emit Failure when dao throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.selectFavoritesByZipcode(any()) } returns flow { throw expectedException }

        val responses = favoriteRepository.getFavoritesByZipcode("").toList()

        responses.assertFlowFailure { assertThat(it).isEqualTo(expectedException) }
        coVerify(exactly = 1) { favoriteDao.selectFavoritesByZipcode("") }
    }
    // endregion

    // region Update tests
    @Test
    fun `updateNoteFromFavorite should Emit Success when dao call is successful`() = runTest {
        val note = mockNotes.first().toEntity()
        coJustRun { favoriteDao.updateNote(note) }

        val responses = favoriteRepository.updateNoteFromFavorite(note).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { favoriteDao.updateNote(note) }
    }

    @Test
    fun `updateNoteFromFavorite should Emit Failure when dao throws Exception`() = runTest {
        val note = mockNotes.first().toEntity()
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.updateNote(any()) } throws expectedException

        val responses = favoriteRepository.updateNoteFromFavorite(note).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.updateNote(note) }
    }
    // endregion

    // region Delete tests
    @Test
    fun `deleteFromFavorite should Emit Success when dao call is successful`() = runTest {
        val place = mockPlaceWithNotes(1).first().place
        coJustRun { favoriteDao.deleteFromFavorite(any()) }

        val responses = favoriteRepository.deleteFromFavorite(place.zipcode).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { favoriteDao.deleteFromFavorite(place.zipcode) }
    }

    @Test
    fun `deleteFromFavorite should Emit Failure when dao throws Exception`() = runTest {
        val place = mockPlaceWithNotes(1).first().place
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.deleteFromFavorite(any()) } throws expectedException

        val responses = favoriteRepository.deleteFromFavorite(place.zipcode).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.deleteFromFavorite(place.zipcode) }
    }

    @Test
    fun `deleteNoteFromFavorite should Emit Success when note count is greater than one`() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()
        coEvery { favoriteDao.countNotesEntitiesFromFavorite(any()) } returns 2
        coJustRun { favoriteDao.deleteNote(any()) }

        val responses = favoriteRepository.deleteNoteFromFavorite(place.zipcode, notes.first()).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { favoriteDao.deleteNote(notes.first()) }
    }

    @Test
    fun `deleteNoteFromFavorite should Emit Failure when dao throws Exception`() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.countNotesEntitiesFromFavorite(any()) } returns 2
        coEvery { favoriteDao.deleteNote(any()) } throws expectedException

        val responses = favoriteRepository.deleteNoteFromFavorite(place.zipcode, notes.first()).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.deleteNote(notes.first()) }
    }

    @Test
    fun `deleteNoteFromFavorite should Emit Failure when there is only one note`() = runTest {
        val (place, notes) = mockPlaceWithNotes(1).first()
        coEvery { favoriteDao.countNotesEntitiesFromFavorite(any()) } returns 1

        val responses = favoriteRepository.deleteNoteFromFavorite(place.zipcode, notes.first()).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(IllegalNoteQuantity::class.java) }
        coVerify(exactly = 0) { favoriteDao.deleteNote(any()) } // Important: verify delete is never called
    }
    // endregion

    // region Export tests
    @Test
    fun `exportFavorites should Emit Success with data when dao returns a list`() = runTest {
        val data = mockPlaceWithNotes(5)
        val expected = data.map { it.toModel() }
        coEvery { favoriteDao.exportFavorites() } returns flowOf(data)

        val responses = favoriteRepository.exportFavorites().toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expected) }
        coVerify(exactly = 1) { favoriteDao.exportFavorites() }
    }

    @Test
    fun `exportFavorites should Emit Failure when dao throws Exception`() = runTest {
        val expectedException = SQLiteException("Test DB Error")
        coEvery { favoriteDao.exportFavorites() } returns flow { throw expectedException }

        val responses = favoriteRepository.exportFavorites().toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.exportFavorites() }
    }
    // endregion

    // region Import tests
    @Test
    fun `importFavorites should insert all data when they do not exist`() = runTest {
        val data = mockFavoritePlaces
        coEvery { favoriteDao.doesPlaceEntityExist(any()) } returns false
        coEvery { favoriteDao.doesNoteEntityExist(any(), any(), any()) } returns false
        coJustRun { favoriteDao.insertPlaceEntity(any()) }
        coJustRun { favoriteDao.insertNoteEntityToFavorite(any(), any()) }

        val responses = favoriteRepository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = data.size) { favoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = data.sumOf { it.notes.size }) { favoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should insert all data when places exists`() = runTest {
        val data = mockFavoritePlaces
        coEvery { favoriteDao.doesPlaceEntityExist(any()) } returns true
        coEvery { favoriteDao.doesNoteEntityExist(any(), any(), any()) } returns false
        coJustRun { favoriteDao.insertPlaceEntity(any()) }
        coJustRun { favoriteDao.insertNoteEntityToFavorite(any(), any()) }

        val responses = favoriteRepository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 0) { favoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = data.sumOf { it.notes.size }) { favoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should not insert anything when all data exists`() = runTest {
        val data = mockFavoritePlaces
        coEvery { favoriteDao.doesPlaceEntityExist(any()) } returns true
        coEvery { favoriteDao.doesNoteEntityExist(any(), any(), any()) } returns true

        val responses = favoriteRepository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 0) { favoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 0) { favoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should Emit Failure when data is empty`() = runTest {
        val responses = favoriteRepository.importFavorites(emptyList()).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(BackupException.ImportEmptyFavoriteException::class.java) }
    }

    @Test
    fun `importFavorites should Emit Failure when insertPlace throws Exception`() = runTest {
        val data = mockFavoritePlaces
        val expectedException = SQLiteException("Insert Place Error")
        coEvery { favoriteDao.doesPlaceEntityExist(any()) } returns false
        coEvery { favoriteDao.insertPlaceEntity(any()) } throws expectedException

        val responses = favoriteRepository.importFavorites(data).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { favoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 0) { favoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should Emit Failure when insertNote throws Exception`() = runTest {
        val data = mockFavoritePlaces
        val expectedException = SQLiteException("Insert Note Error")
        coEvery { favoriteDao.doesPlaceEntityExist(any()) } returns false
        coEvery { favoriteDao.doesNoteEntityExist(any(), any(), any()) } returns false
        coJustRun { favoriteDao.insertPlaceEntity(any()) }
        coEvery { favoriteDao.insertNoteEntityToFavorite(any(), any()) } throws expectedException

        val responses = favoriteRepository.importFavorites(data).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(atLeast = 1) { favoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 1) { favoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }
    // endregion
}