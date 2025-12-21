package br.com.arml.cep.domain

import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoriteUseCaseTest {
    private val repository: FavoriteRepository = mockk()
    private lateinit var useCase: FavoriteUseCase
    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        useCase = FavoriteUseCase(moshi, repository)
    }

    // region Mock Helper Functions
    private fun List<Place>.filterByTitle(query: String): List<Place> {
        return map { place ->
            val filteredNotes = place.notes.filter { it.title.contains(query, ignoreCase = true) }
            place.copy(notes = filteredNotes)
        }.filter { it.notes.isNotEmpty() }
    }
    // endregion

    // region Create tests
    @Test
    fun `addNoteToFavorite should emits loading and success when repository is successful`() = runTest {
        // Arrange
        val (cep, note) = mockFavoritePlaces.first().let { it.cep to it.notes.first() }
        coEvery {
            repository.addToFavorite(any(),any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.addNoteToFavorite(cep, note).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify { repository.addToFavorite(cep.text, note.toEntity()) }
    }

    @Test
    fun `addNoteToFavorite should emits loading and failure when repository is failure`() = runTest {
        // Arrange
        val (cep, note) = mockFavoritePlaces.first().let { it.cep to it.notes.first() }
        val expectedException = Exception("Repository Error")
        coEvery {
            repository.addToFavorite(any(),any())
        } returns flowOf(Loading, Failure(expectedException))

        // Act
        val responses = useCase.addNoteToFavorite(cep, note).toList()

        // Assert
        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify { repository.addToFavorite(cep.text, note.toEntity()) }
    }
    // endregion

    // region Read tests
    @Test
    fun `fetchFavorites should emit success when repository is successful`() = runTest {
        // Arrange
        val expectedPlaces = mockFavoritePlaces
        coEvery {
            repository.getFavoritesByTitle(any())
        } returns flowOf(Loading, Success(expectedPlaces))

        // Act
        val responses = useCase.fetchFavorites().toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).containsExactlyElementsIn(expectedPlaces) }
        coVerify(exactly = 1) { repository.getFavoritesByTitle("") }
    }

    @Test
    fun `fetchFavorites should emit failure when repository is failure`() = runTest {
        // Arrange
        val expectedException = SQLiteException("DB error")
        coEvery {
            repository.getFavoritesByTitle(any())
        } returns flowOf(Loading, Failure(expectedException))

        // Act
        val responses = useCase.fetchFavorites().toList()

        // Assert
        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { repository.getFavoritesByTitle("") }
    }

    @Test
    fun `filterByTitle should emit success when repository is successful`() = runTest {
        // Arrange
        val query = mockFavoritePlaces.first().notes.first().title
        val expectedPlaces = mockFavoritePlaces.filterByTitle(query)
        coEvery {
            repository.getFavoritesByTitle(any())
        } returns flowOf(Loading, Success(expectedPlaces))

        // Act
        val responses = useCase.filterByTitle(query).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).containsExactlyElementsIn(expectedPlaces) }
        coVerify(exactly = 1) { repository.getFavoritesByTitle(query) }
    }

    @Test
    fun `filterByTitle should emit failure when repository is failure`() = runTest {
        // Arrange
        val query = "Title 1"
        val expectedException = SQLiteException("DB error")
        coEvery {
            repository.getFavoritesByTitle(any())
        } returns flowOf(Loading, Failure(expectedException))

        // Act
        val responses = useCase.filterByTitle(query).toList()

        // Assert
        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { repository.getFavoritesByTitle(query) }
    }
    // endregion

    // region Update tests
    @Test
    fun `updateNote should emit success when repository is successful`() = runTest {
        // Arrange
        val note = mockNotes.first()
        coEvery{
            repository.updateNoteFromFavorite(any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.updateNote(note).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { repository.updateNoteFromFavorite(note.toEntity()) }
    }

    @Test
    fun `updateNote should emit failure when repository is failure`() = runTest {
        // Arrange
        val note = mockNotes.first()
        val expectedException = SQLiteException("Repository Error")
        coEvery{
            repository.updateNoteFromFavorite(any())
        } returns flowOf(Loading, Failure(expectedException))

        // Act
        val responses = useCase.updateNote(note).toList()

        // Assert
        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { repository.updateNoteFromFavorite(note.toEntity()) }
    }
    // endregion

    // region Delete tests
    @Test
    fun `removeFromFavorite should emit success when repository is successful`() = runTest {
        // Arrange
        val (place, zipcode) = mockFavoritePlaces.first().let { it to it.cep.text }
        coEvery {
            repository.deleteFromFavorite(any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.removeFromFavorite(place).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { repository.deleteFromFavorite(zipcode) }
    }

    @Test
    fun `removeFromFavorite should emit failure when repository is failure`() = runTest {
        val (place, zipcode) = mockFavoritePlaces.first().let { it to it.cep.text }
        val expectedException = SQLiteException("DB error")
        coEvery {
            repository.deleteFromFavorite(any())
        } returns flowOf(Loading, Failure(expectedException))

        val responses = useCase.removeFromFavorite(place).toList()

        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { repository.deleteFromFavorite(zipcode) }
    }

    @Test
    fun `deleteNote should emit success when repository is successful`() = runTest {
        // Arrange
        val place = mockFavoritePlaces.first()
        val (zipcode, note) = place.let { it.cep.text to it.notes.first() }
        coEvery {
            repository.deleteNoteFromFavorite(any(),any())
        } returns flowOf(Loading, Success(zipcode))

        // Act
        val responses = useCase.deleteNote(place.cep, note).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(zipcode) }
        coVerify(exactly = 1) { repository.deleteNoteFromFavorite(zipcode, note.toEntity()) }
    }

    @Test
    fun `deleteNote should emit failure when repository is failure`() = runTest {
        // Arrange
        val place = mockFavoritePlaces.first()
        val (zipcode, note) = place.let { it.cep.text to it.notes.first() }
        val expectedException = SQLiteException("DB error")
        coEvery {
            repository.deleteNoteFromFavorite(any(),any())
        } returns flowOf(Loading, Failure(expectedException))

        // Act
        val responses = useCase.deleteNote(place.cep, note).toList()

        // Assert
        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { repository.deleteNoteFromFavorite(zipcode, note.toEntity()) }
    }
    // endregion

    // region Export/Import tests
    @Test
    fun `exportFavorites should emit success with JSON string`() = runTest {
        // Arrange
        val data = mockFavoritePlaces
        val expectedJson = moshi.adapter<List<Place>>(List::class.java).toJson(data)
        coEvery {
            repository.exportFavorites()
        } returns flowOf(Loading, Success(data))

        // Act
        val responses = useCase.exportFavorites().toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedJson) }
        coVerify(exactly = 1) { repository.exportFavorites() }
    }

    @Test
    fun `importFavorites should emit success when repository is successful`() = runTest {
        // Arrange
        val data = mockFavoritePlaces
        val json = moshi.adapter<List<Place>>(List::class.java).toJson(data)
        coEvery {
            repository.importFavorites(any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.importFavorites(json).toList()

        // Assert
        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { repository.importFavorites(data) }
    }
    // endregion
}