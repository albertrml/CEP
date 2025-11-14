package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheUseCaseSearchTest {
    private val cacheRepository = mockk<CacheRepository>()
    private val favoriteRepository = mockk<FavoriteRepository>()
    private lateinit var useCase: CacheUseCase

    @Before
    fun setup() {
        useCase = CacheUseCase(
            cacheRepository,
            favoriteRepository
        )
    }

    /** Add Place to Favorite **/
    @Test
    fun addToFavorite_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val success = Success(Unit)
        coEvery {
            cacheRepository.insertPlace(any())
        } coAnswers { flow { emit(success) } }

        // Act
        val responses = useCase.addToFavorite(place).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )

        coVerify(exactly = 1) { cacheRepository.insertPlace(place) }
    }

    @Test
    fun addToFavorite_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val fail = Failure(Exception("Error"))
        coEvery {
            cacheRepository.insertPlace(any())
        } coAnswers { flow { emit(fail) } }

        // Act
        val responses = useCase.addToFavorite(place).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Failure",
            responses[1] is Failure
        )

        coVerify(exactly = 1) { cacheRepository.insertPlace(place) }
    }

    /** Delete All **/
    @Test
    fun clearCache_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val success = Success(Unit)
        coEvery {
            cacheRepository.deleteAllUnwanted()
        } answers { flow { emit(success) } }

        // Act
        val responses = useCase.clearCache().toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )

        coVerify(exactly = 1) { cacheRepository.deleteAllUnwanted() }
    }

    @Test
    fun clearCache_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val fail = Failure(Exception("Error"))
        coEvery {
            cacheRepository.deleteAllUnwanted()
        } returns flow {
            emit(fail)
        }

        // Act
        val responses = useCase.clearCache().toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Failure",
            responses[1] is Failure
        )

        coVerify(exactly = 1) { cacheRepository.deleteAllUnwanted() }
    }

    /** Delete Entry **/
    @Test
    fun removePlaceFromCache_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val success = Success(Unit)
        coEvery {
            cacheRepository.deletePlace(any())
        } answers { flow { emit(success) } }

        // Act
        val responses = useCase.removePlaceFromCache(place).toList()

        // Assert
        assertEquals(2,responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )

        coVerify(exactly = 1) { cacheRepository.deletePlace(place) }
    }


    @Test
    fun removePlaceFromCache_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val fail = Failure(Exception("Error"))
        coEvery {
            cacheRepository.deletePlace(any())
        } answers { flow { emit(fail) } }

        // Act
        val responses = useCase.removePlaceFromCache(place).toList()

        // Assert
        assertEquals(2,responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Failure",
            responses[1] is Failure
        )

        coVerify(exactly = 1) { cacheRepository.deletePlace(place) }
    }

    /** Fetch Cache **/
    @Test
    fun fetchCache_Item_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val success = Success(mockUnfavoritePlaces)
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } answers { flow { emit(success) } }

        // Act
        val responses = useCase.findCachedPlacesByCep().toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )
        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode("") }
    }

    @Test
    fun fetchCache_Item_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val fail = Failure(Exception("Error"))
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } answers { flow { emit(fail) } }

        // Act
        val responses = useCase.findCachedPlacesByCep().toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Failure",
            responses[1] is Failure
        )
        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode("") }
    }

    /** Filter By Cep **/
    @Test
    fun findCachedPlacesByCep_shouldReturnSuccessAndNotEmpty_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val query = mockUnfavoritePlaces.first().cep.text
        val expectedResult = mockUnfavoritePlaces.filter {
            it.cep.equals(query)
        }
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } answers {
            flow {
                val result = mockUnfavoritePlaces.filter { it.cep.equals(args[0].toString()) }
                emit(Success(result))
            }
        }

        // Act
        val responses = useCase.findCachedPlacesByCep(query).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )
        val result = (responses[1] as Success).result
        assertTrue(
            "The $result should be equal to $expectedResult",
            result.containsAll(expectedResult)
        )

        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode(query = query) }
    }

    @Test
    fun filterByCep_shouldReturnSuccessAndEmpty_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val query = "123456"
        val expectedResult = mockUnfavoritePlaces.filter {
            it.cep.equals(query)
        }
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } answers {
            flow {
                val result = mockUnfavoritePlaces.filter { it.cep.equals(args[0].toString()) }
                emit(Success(result))
            }
        }

        // Act
        val responses = useCase.findCachedPlacesByCep(query).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )
        val result = (responses[1] as Success).result
        assertTrue(
            "The $result should be equal to $expectedResult",
            result.isEmpty()
        )

        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode(query = query) }
    }

    @Test
    fun filterByCep_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val query = "01234567"
        val fail = Failure(Exception("Error"))
        coEvery {
            cacheRepository.getPlacesByZipcode(any())
        } answers {
            flow { emit(fail) }
        }

        // Act
        val responses = useCase.findCachedPlacesByCep(query).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        assertTrue(
            "The second response is expected to be Failure",
            responses[1] is Failure
        )

        coVerify(exactly = 1) { cacheRepository.getPlacesByZipcode(query = query) }
    }
}