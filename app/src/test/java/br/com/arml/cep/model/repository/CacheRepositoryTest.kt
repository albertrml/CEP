package br.com.arml.cep.model.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.source.local.CacheDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CacheRepositoryTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup() {
        repository = CacheRepository(mockCacheDao)
    }

    /** CREATE **/
    @Test
    fun `insertPlace should emit Loading and then Success on successful DAO insertion`() = runTest {
        // Arrange
        val placeToInsert = mockUnfavoritePlaces.first()
        coEvery { mockCacheDao.insert(any()) } returns Unit

        // Act
        val responses = repository.insertPlace(placeToInsert).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Success", responses[1] is Response.Success)
        coVerify(exactly = 1) { mockCacheDao.insert(placeToInsert.toEntity()) }
    }

    @Test
    fun `insertPlace should emit Loading and then Failure when DAO throws exception`() = runTest {
        // Arrange
        val placeToInsert = mockUnfavoritePlaces.first()
        val exception = SQLiteConstraintException("Primary key conflict")
        coEvery { mockCacheDao.insert(any()) } throws exception

        // Act
        val responses = repository.insertPlace(placeToInsert).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Failure", responses[1] is Response.Failure)
        coVerify(exactly = 1) { mockCacheDao.insert(placeToInsert.toEntity()) }
    }

    /** READ **/
    @Test
    fun `getPlacesByZipcode should emit Loading and then Success with data`() = runTest {
        // Arrange
        val mockData = mockUnfavoritePlaces.map { it.toEntity() }
        coEvery { mockCacheDao.getByZipcode(any()) } returns flowOf(mockData)

        // Act
        val responses = repository.getPlacesByZipcode("").toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Success", responses[1] is Response.Success)
        coVerify(exactly = 1) { mockCacheDao.getByZipcode("") }
    }

    @Test
    fun `getPlacesByZipcode should emit Loading and then Failure when DAO flow throws exception`() = runTest {
        // Arrange
        val exception = SQLiteException("Database read error")
        coEvery { mockCacheDao.getByZipcode(any()) } returns flow { throw exception }

        // Act
        val responses = repository.getPlacesByZipcode("").toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Failure", responses[1] is Response.Failure)
        coVerify(exactly = 1) { mockCacheDao.getByZipcode("") }
    }

    /** UPDATE **/
    @Test
    fun `updatePlace should emit Loading and then Success on successful DAO update`() = runTest {
        // Arrange
        val placeToUpdate = mockUnfavoritePlaces.first()
        coEvery { mockCacheDao.update(any()) } returns Unit

        // Act
        val responses = repository.updatePlace(placeToUpdate).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Success", responses[1] is Response.Success)
        coVerify(exactly = 1) { mockCacheDao.update(placeToUpdate.toEntity()) }
    }

    @Test
    fun `updatePlace should emit Loading and then Failure when DAO throws exception`() = runTest {
        // Arrange
        val placeToUpdate = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Update failed")
        coEvery { mockCacheDao.update(any()) } throws exception

        // Act
        val responses = repository.updatePlace(placeToUpdate).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Failure", responses[1] is Response.Failure)
        coVerify(exactly = 1) { mockCacheDao.update(placeToUpdate.toEntity()) }
    }

    /** DELETE **/
    @Test
    fun `deletePlace should emit Loading and then Success on successful DAO deletion`() = runTest {
        // Arrange
        val placeToDelete = mockUnfavoritePlaces.first()
        coEvery { mockCacheDao.deleteIfUnfavorite(any()) } returns Unit

        // Act
        val responses = repository.deletePlace(placeToDelete).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Success", responses[1] is Response.Success)
        coVerify(exactly = 1) { mockCacheDao.deleteIfUnfavorite(placeToDelete.cep.text) }
    }

    @Test
    fun `deletePlace should emit Loading and then Failure when DAO throws exception`() = runTest {
        // Arrange
        val placeToDelete = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Delete failed")
        coEvery { mockCacheDao.deleteIfUnfavorite(any()) } throws exception

        // Act
        val responses = repository.deletePlace(placeToDelete).toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Failure", responses[1] is Response.Failure)
        coVerify(exactly = 1) { mockCacheDao.deleteIfUnfavorite(placeToDelete.cep.text) }
    }

    @Test
    fun `deleteAllUnwanted should emit Loading and then Success on successful DAO deletion`() = runTest {
        // Arrange
        coEvery { mockCacheDao.deleteAllUnwanted() } returns Unit

        // Act
        val responses = repository.deleteAllUnwanted().toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Success", responses[1] is Response.Success)
        coVerify(exactly = 1) { mockCacheDao.deleteAllUnwanted() }
    }

    @Test
    fun `deleteAllUnwanted should emit Loading and then Failure when DAO throws exception`() = runTest {
        // Arrange
        val exception = SQLiteException("Delete all failed")
        coEvery { mockCacheDao.deleteAllUnwanted() } throws exception

        // Act
        val responses = repository.deleteAllUnwanted().toList()

        // Assert
        assertTrue("Expected 2 responses", responses.size == 2)
        assertTrue("First response should be Loading", responses[0] is Response.Loading)
        assertTrue("Second response should be Failure", responses[1] is Response.Failure)
        coVerify(exactly = 1) { mockCacheDao.deleteAllUnwanted() }
    }
}