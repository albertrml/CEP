package br.com.arml.cep.domain

import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.utils.OperationOnFailure
import br.com.arml.cep.utils.OperationOnSuccess
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    private fun <T> createSuccessFlow(result: T): Flow<Response<T>> =
        flowOf(Response.Loading, Response.Success(result))

    private fun <T> createFailureFlow(exception: Exception): Flow<Response<T>> =
        flowOf(Response.Loading, Response.Failure(exception))

    private fun <T> mockFavoriteRepositoryOnSuccess(operation: OperationOnSuccess<T>) {
        when (operation) {
            is OperationOnSuccess.AddNoteToFavorite -> coEvery {
                repository.addToFavorite(any(), any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.GetFavoritesByZipcode -> coEvery {
                repository.getFavoritesByZipcode(any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.GetFavoritesByTitle -> coEvery {
                repository.getFavoritesByTitle(any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.UpdateNoteFromFavorite -> coEvery {
                repository.updateNoteFromFavorite(any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.DeleteFromFavorite -> coEvery {
                repository.deleteFromFavorite(any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.DeleteNoteFromFavorite -> coEvery {
                repository.deleteNoteFromFavorite(any(),any())
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.ExportFavorites -> coEvery {
                repository.exportFavorites()
            } returns createSuccessFlow(operation.result)

            is OperationOnSuccess.ImportFavorites -> coEvery {
                repository.importFavorites(any())
            } returns createSuccessFlow(operation.result)
        }
    }

    private fun mockFavoriteRepositoryOnFailure(operation: OperationOnFailure) {
        when (operation) {
            is OperationOnFailure.AddNoteToFavorite -> coEvery {
                repository.addToFavorite(any(), any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.GetFavoritesByZipcode -> coEvery {
                repository.getFavoritesByZipcode(any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.GetFavoritesByTitle -> coEvery {
                repository.getFavoritesByTitle(any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.UpdateNoteFromFavorite -> coEvery {
                repository.updateNoteFromFavorite(any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.DeleteFromFavorite -> coEvery {
                repository.deleteFromFavorite(any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.DeleteNoteFromFavorite -> coEvery {
                repository.deleteNoteFromFavorite(any(),any())
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.ExportFavorites -> coEvery {
                repository.exportFavorites()
            } returns createFailureFlow(operation.exception)

            is OperationOnFailure.ImportFavorites -> coEvery {
                repository.importFavorites(any())
            } returns createFailureFlow(operation.exception)
        }
    }
    private fun List<Place>.filterByTitle(query: String): List<Place> {
        return map { place ->
            val filteredNotes = place.notes.filter { it.title.contains(query, ignoreCase = true) }
            place.copy(notes = filteredNotes)
        }.filter { it.notes.isNotEmpty() }
    }
    // endregion

    // region Create tests
    @Test
    fun `addNoteToFavorite should emit success when repository is successful`() = runTest {
        val place = mockFavoritePlaces.first()
        val note = place.notes.first()
        val zipcode = place.cep.text
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.AddNoteToFavorite())

        val responses = useCase.addNoteToFavorite(place.cep, note).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { repository.addToFavorite(zipcode, note.toEntity()) }
    }

    @Test
    fun `addNoteToFavorite should emit failure when repository is failure`() = runTest {
        val place = mockFavoritePlaces.first()
        val note = place.notes.first()
        val zipcode = place.cep.text
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.AddNoteToFavorite(exception))

        val responses = useCase.addNoteToFavorite(place.cep, note).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.addToFavorite(zipcode, note.toEntity()) }
    }
    // endregion

    // region Read tests
    @Test
    fun `fetchFavorites should emit success when repository is successful`() = runTest {
        val expected = mockFavoritePlaces
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.GetFavoritesByZipcode(expected))

        val responses = useCase.fetchFavorites().toList()

        responses.assertFlowSuccess { assertEquals(expected, it) }
        coVerify(exactly = 1) { repository.getFavoritesByZipcode("") }
    }

    @Test
    fun `fetchFavorites should emit failure when repository is failure`() = runTest {
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.GetFavoritesByZipcode(exception))

        val responses = useCase.fetchFavorites().toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.getFavoritesByZipcode("") }
    }

    @Test
    fun `filterByTitle should emit success when repository is successful`() = runTest {
        val query = "Title 1"
        val expected = mockFavoritePlaces.filterByTitle(query)
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.GetFavoritesByTitle(expected))

        val responses = useCase.filterByTitle(query).toList()

        responses.assertFlowSuccess { assertEquals(expected, it) }
        coVerify(exactly = 1) { repository.getFavoritesByTitle(query) }
    }

    @Test
    fun `filterByTitle should emit failure when repository is failure`() = runTest {
        val query = "Title 1"
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.GetFavoritesByTitle(exception))

        val responses = useCase.filterByTitle(query).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.getFavoritesByTitle(query) }
    }
    // endregion

    // region Update tests
    @Test
    fun `updateNote should emit success when repository is successful`() = runTest {
        val note = mockNotes.first()
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.UpdateNoteFromFavorite())

        val responses = useCase.updateNote(note).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { repository.updateNoteFromFavorite(note.toEntity()) }
    }

    @Test
    fun `updateNote should emit failure when repository is failure`() = runTest {
        val note = mockNotes.first()
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.UpdateNoteFromFavorite(exception))

        val responses = useCase.updateNote(note).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.updateNoteFromFavorite(note.toEntity()) }
    }
    // endregion

    // region Delete tests
    @Test
    fun `removeFromFavorite should emit success when repository is successful`() = runTest {
        val place = mockFavoritePlaces.first()
        val zipcode = place.cep.text
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.DeleteFromFavorite())

        val responses = useCase.removeFromFavorite(place).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { repository.deleteFromFavorite(zipcode) }
    }

    @Test
    fun `removeFromFavorite should emit failure when repository is failure`() = runTest {
        val place = mockFavoritePlaces.first()
        val zipcode = place.cep.text
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.DeleteFromFavorite(exception))

        val responses = useCase.removeFromFavorite(place).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.deleteFromFavorite(zipcode) }
    }

    @Test
    fun `deleteNote should emit success when repository is successful`() = runTest {
        val place = mockFavoritePlaces.first()
        val zipcode = place.cep.text
        val note = place.notes.first()
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.DeleteNoteFromFavorite())

        val responses = useCase.deleteNote(place.cep, note).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { repository.deleteNoteFromFavorite(zipcode, note.toEntity()) }
    }

    @Test
    fun `deleteNote should emit failure when repository is failure`() = runTest {
        val place = mockFavoritePlaces.first()
        val zipcode = place.cep.text
        val note = place.notes.first()
        val exception = SQLiteException("DB error")
        mockFavoriteRepositoryOnFailure(OperationOnFailure.DeleteNoteFromFavorite(exception))

        val responses = useCase.deleteNote(place.cep, note).toList()

        responses.assertFlowFailure { assertEquals(exception, it) }
        coVerify(exactly = 1) { repository.deleteNoteFromFavorite(zipcode, note.toEntity()) }
    }
    // endregion

    // region Export/Import tests
    @Test
    fun `exportFavorites should emit success with JSON string`() = runTest {
        val data = mockFavoritePlaces
        val expectedJson = moshi.adapter<List<Place>>(List::class.java).toJson(data)
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.ExportFavorites(data))

        val responses = useCase.exportFavorites().toList()

        responses.assertFlowSuccess { assertEquals(expectedJson, it) }
        coVerify(exactly = 1) { repository.exportFavorites() }
    }

    @Test
    fun `importFavorites should emit success when repository is successful`() = runTest {
        val data = mockFavoritePlaces
        val json = moshi.adapter<List<Place>>(List::class.java).toJson(data)
        mockFavoriteRepositoryOnSuccess(OperationOnSuccess.ImportFavorites())

        val responses = useCase.importFavorites(json).toList()

        responses.assertFlowSuccess { assertEquals(Unit, it) }
        coVerify(exactly = 1) { repository.importFavorites(data) }
    }
    // endregion
}