package br.com.arml.cep.domain

import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CacheUseCaseTest {
    private val mockCacheRepository = mockk<CacheRepository>()
    private val mockFavoriteRepository = mockk<FavoriteRepository>()
    private lateinit var useCase: CacheUseCase

    @Before
    fun setup(){
        useCase = CacheUseCase(
            mockCacheRepository,
            mockFavoriteRepository
        )
    }

    /** addToFavorite tests **/
    @Test
    fun addToFavorite_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val zipcode = place.cep.text
        val note = NoteEntity(title = zipcode, content = "")
        coEvery {
            mockFavoriteRepository.addToFavorite(any(),any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.addToFavorite(place).toList()

        // Assert
        assertEquals(2, responses.size)
        Assert.assertTrue(
            "The first response is expected to be Loading",
            responses[0] is Loading
        )
        Assert.assertTrue(
            "The second response is expected to be Success",
            responses[1] is Success
        )

        coVerify(exactly = 1) { mockFavoriteRepository.addToFavorite(zipcode,note) }
    }

    @Test
    fun addToFavorite_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        // Arrange
        val place = mockUnfavoritePlaces.first()
        val exception = Exception("Error")
        coEvery {
            mockFavoriteRepository.addToFavorite(any(),any())
        } returns flowOf(Loading, Failure(exception))

        // Act
        val responses = useCase.addToFavorite(place).toList()

        // Assert
        assertEquals(2, responses.size)
        assertTrue("First response should be Loading",responses[0] is Loading)
        assertTrue("Second response should be Failure", responses[1] is Failure)

        coVerify(exactly = 1) { mockCacheRepository.insertPlace(place) }
    }

    /** clearCache Tests **/
    @Test
    fun `clearCache should emit Loading and then Success on successful repository call`() = runTest {
        // Arrange
        coEvery {
            mockCacheRepository.deleteAllUnwanted()
        } returns flowOf(Loading,Success(Unit))

        // Act
        val responses = useCase.clearCache().toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Success", responses[1] is Success)
        coVerify(exactly = 1) { mockCacheRepository.deleteAllUnwanted() }
    }

    @Test
    fun `clearCache should emit Loading and then Failure when repository throws exception`() = runTest {
        // Arrange
        val exception = SQLiteException("Deletion failed")
        coEvery {
            mockCacheRepository.deleteAllUnwanted()
        } returns flowOf(Loading, Failure(exception))

        // Act
        val responses = useCase.clearCache().toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Failure", responses[1] is Failure)
        coVerify(exactly = 1) { mockCacheRepository.deleteAllUnwanted() }
    }

    /** removePlaceFromCache Tests **/
    @Test
    fun `removePlaceFromCache should emit Loading and then Success on successful repository call`() = runTest {
        // Arrange
        val placeToDelete = mockUnfavoritePlaces.first()
        coEvery {
            mockCacheRepository.deletePlace(any())
        } returns flowOf(Loading, Success(Unit))

        // Act
        val responses = useCase.removePlaceFromCache(placeToDelete).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Success", responses[1] is Success)
        coVerify(exactly = 1) { mockCacheRepository.deletePlace(placeToDelete) }
    }

    @Test
    fun `removePlaceFromCache should emit Loading and then Failure when repository throws exception`() = runTest {
        // Arrange
        val placeToDelete = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Delete failed")
        coEvery {
            mockCacheRepository.deletePlace(any())
        } returns flowOf(Loading, Failure(exception))

        // Act
        val responses = useCase.removePlaceFromCache(placeToDelete).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Failure", responses[1] is Failure)
        coVerify(exactly = 1) { mockCacheRepository.deletePlace(placeToDelete) }
    }

    /** findCachedPlacesByCep Tests **/
    @Test
    fun `findCachedPlacesByCep should emit Loading and then Success on successful repository call`() = runTest {
        // Arrange
        val mockData = mockUnfavoritePlaces.first()
        val query = mockData.cep.text
        coEvery {
            mockCacheRepository.getPlacesByZipcode(any())
        } returns flowOf(Loading, Success(listOf(mockData)))

        // Act
        val responses = useCase.findCachedPlacesByCep(query).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Success", responses[1] is Success)
        coVerify { mockCacheRepository.getPlacesByZipcode(query) }
    }

    @Test
    fun `findCachedPlacesByCep should emit Loading and then Failure when repository throws exception`() = runTest {
        // Arrange
        val query = "123"
        val exception = SQLiteException("Read failed")
        coEvery {
            mockCacheRepository.getPlacesByZipcode(any())
        } returns flowOf( Loading, Failure(exception))

        // Act
        val responses = useCase.findCachedPlacesByCep(query).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Loading)
        assertTrue("Second response should be Failure", responses[1] is Failure)
        coVerify { mockCacheRepository.getPlacesByZipcode(query) }
    }
}
