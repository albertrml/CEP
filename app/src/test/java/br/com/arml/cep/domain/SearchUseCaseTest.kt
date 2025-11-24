package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.repository.SearchRepository
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchUseCaseTest {
    private val searchRepository = mockk<SearchRepository>()
    private val favoriteRepository = mockk<FavoriteRepository>()
    private lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        searchUseCase = SearchUseCase(searchRepository, favoriteRepository)
    }

    // region Mock Helper Functions
    private fun mockSearchSuccess(place: Place) {
        coEvery {
            searchRepository.getPlace(any())
        } returns flowOf(Response.Loading, Response.Success(place))
    }

    private fun mockSearchFailure(exception: Exception) {
        coEvery {
            searchRepository.getPlace(any())
        } returns flowOf(Response.Loading, Response.Failure(exception))
    }

    private fun mockAddFavoriteSuccess() {
        coEvery {
            favoriteRepository.addToFavorite(any(), any())
        } answers  {
            val capturedZipcode = invocation.args[0] as String
            flowOf(Response.Loading, Response.Success(capturedZipcode))
        }
    }

    private fun mockAddFavoriteFailure(exception: Exception) {
        coEvery {
            favoriteRepository.addToFavorite(any(), any())
        } returns flowOf(Response.Loading, Response.Failure(exception))
    }
    // endregion

    // region searchPlace tests
    @Test
    fun `searchPlace should emit Success with a place`() = runTest {
        val expectedPlace = mockUnfavoritePlaces.first()
        val cep = expectedPlace.cep
        val query = expectedPlace.cep.text
        mockSearchSuccess(expectedPlace)

        val responses = searchUseCase.searchPlace(query).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedPlace) }
        coVerify(exactly = 1) { searchRepository.getPlace(cep) }
    }

    @Test
    fun `searchPlace should emit Failure for NotFoundCepException`() = runTest {
        val cep = mockUnfavoritePlaces.first().cep
        val query = cep.text
        val exception = CepException.NotFoundCepException()
        mockSearchFailure(exception)

        val responses = searchUseCase.searchPlace(query).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(exception::class.java) }
        coVerify(exactly = 1) { searchRepository.getPlace(cep) }
    }

    @Test
    fun `searchPlace should emit Failure for other exceptions`() = runTest {
        val cep = mockUnfavoritePlaces.first().cep
        val query = cep.text
        val exception = Exception("General search error")
        mockSearchFailure(exception)

        val responses = searchUseCase.searchPlace(query).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(exception::class.java) }
        coVerify(exactly = 1) { searchRepository.getPlace(cep) }
    }
    // endregion

    // region addToFavorite tests
    @Test
    fun `addToFavorite should emit Success`() = runTest {
        val place = mockUnfavoritePlaces.first()
        val expectedZipcode = place.cep.text
        mockAddFavoriteSuccess()

        val responses = searchUseCase.addToFavorite(place).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedZipcode) }
        coVerify(exactly = 1) { favoriteRepository.addToFavorite(any(), any()) }
    }

    @Test
    fun `addToFavorite should emit Failure`() = runTest {
        val place = mockUnfavoritePlaces.first()
        val exception = Exception("Failed to add to favorites")
        mockAddFavoriteFailure(exception)

        val responses = searchUseCase.addToFavorite(place).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(exception::class.java) }
        coVerify(exactly = 1) { favoriteRepository.addToFavorite(any(), any()) }
    }
    // endregion
}