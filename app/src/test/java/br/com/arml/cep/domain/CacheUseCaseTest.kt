package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.repository.FavoriteRepository
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

class CacheUseCaseTest {
    private val cacheRepository = mockk<CacheRepository>()
    private val favoriteRepository = mockk<FavoriteRepository>()
    private lateinit var useCase: CacheUseCase

    @Before
    fun setup() {
        useCase = CacheUseCase(cacheRepository, favoriteRepository)
    }

    /** Add Place to Favorite **/
    @Test
    fun `addToFavorite should return Loading and Success when repository performs successfully`() =
        runTest {
            val place = mockUnfavoritePlaces.first()
            val zipcode = place.cep.text
            val note = NoteEntity(title = zipcode, content = "")
            coEvery {
                favoriteRepository.addToFavorite(any(), any())
            } returns flowOf(Loading, Success(Unit))

            val responses = useCase.addToFavorite(place).toList()

            responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
            coVerify(exactly = 1) { favoriteRepository.addToFavorite(zipcode, note) }
        }

    @Test
    fun `addToFavorite should return Loading and Failure when repository performs unsuccessfully`() =
        runTest {
            val place = mockUnfavoritePlaces.first()
            val expectedException = Exception("Repository Error")
            coEvery {
                favoriteRepository.addToFavorite(any(), any())
            } returns flowOf(Loading, Failure(expectedException))

            val responses = useCase.addToFavorite(place).toList()

            responses.assertFlowFailure { assertThat(it).isEqualTo(expectedException) }
            coVerify(exactly = 1) { favoriteRepository.addToFavorite(any(), any()) }
        }

    /** Delete All **/
    @Test
    fun `clearCache should return Loading and Success when repository performs successfully`() =
        runTest {
            coEvery {
                cacheRepository.deleteAllUnwanted()
            } returns flowOf(Loading, Success(Unit))

            val responses = useCase.clearCache().toList()

            responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
            coVerify(exactly = 1) { cacheRepository.deleteAllUnwanted() }
        }

    @Test
    fun `clearCache should return Loading and Failure when repository performs unsuccessfully`() =
        runTest {
            val expectedException = Exception("Repository Error")
            coEvery {
                cacheRepository.deleteAllUnwanted()
            } returns flowOf(Loading, Failure(expectedException))

            val responses = useCase.clearCache().toList()

            responses.assertFlowFailure { assertThat(it).isEqualTo(expectedException) }
            coVerify(exactly = 1) { cacheRepository.deleteAllUnwanted() }
        }

    /** Delete Entry **/
    @Test
    fun `removePlaceFromCache should return Loading and Success when repository performs successfully`() =
        runTest {
            val place = mockUnfavoritePlaces.first()
            coEvery {
                cacheRepository.deletePlace(any())
            } returns flowOf(Loading, Success(Unit))

            val responses = useCase.removePlaceFromCache(place).toList()

            responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
            coVerify(exactly = 1) { cacheRepository.deletePlace(place) }
        }

    @Test
    fun `removePlaceFromCache should return Loading and Failure when repository performs unsuccessfully`() =
        runTest {
            val place = mockUnfavoritePlaces.first()
            val expectedException = Exception("Repository Error")
            coEvery {
                cacheRepository.deletePlace(any())
            } returns flowOf(Loading, Failure(expectedException))

            val responses = useCase.removePlaceFromCache(place).toList()

            responses.assertFlowFailure { assertThat(it).isEqualTo(expectedException) }
            coVerify(exactly = 1) { cacheRepository.deletePlace(place) }
        }

    /** Fetch Cache **/
    @Test
    fun `findCachedPlacesByCep should return all places when query is empty`() = runTest {
        val expectedPlaces = mockUnfavoritePlaces
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } returns flowOf(Loading, Success(expectedPlaces))

        val responses = useCase.findCachedPlacesByCep().toList()

        responses.assertFlowSuccess { assertThat(it).containsExactlyElementsIn(expectedPlaces) }
        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode("") }
    }

    @Test
    fun `findCachedPlacesByCep should return filtered places when query is provided`() = runTest {
        val query = mockUnfavoritePlaces.first().cep.text.substring(4, 7)
        val expectedPlaces = mockUnfavoritePlaces.filter { it.cep.text.contains(query) }
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } returns flowOf(Loading, Success(expectedPlaces))

        val responses = useCase.findCachedPlacesByCep(query).toList()

        responses.assertFlowSuccess { assertThat(it).containsExactlyElementsIn(expectedPlaces) }
        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode(query) }
    }

    @Test
    fun `findCachedPlacesByCep should return empty list when repository returns empty`() = runTest {
        val query = "12345"
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } returns flowOf(Loading, Success(emptyList()))

        val responses = useCase.findCachedPlacesByCep(query).toList()

        responses.assertFlowSuccess { assertThat(it).isEmpty() }
        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode(query) }
    }

    @Test
    fun `findCachedPlacesByCep should return Loading and Failure when repository performs unsuccessfully`() =
        runTest {
            val expectedException = Exception("Repository Error")
            coEvery {
                cacheRepository.getPlacesByZipcode(any())
            } returns flowOf(Loading, Failure(expectedException))

            val responses = useCase.findCachedPlacesByCep().toList()

            responses.assertFlowFailure { assertThat(it).isEqualTo(expectedException) }
            coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode("") }
        }
}