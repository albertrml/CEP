package br.com.arml.cep.model.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.source.local.CacheDao
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

class CacheRepositoryTest {
    private val mockCacheDao = mockk<CacheDao>()
    private lateinit var repository: CacheRepository

    @Before
    fun setup() {
        repository = CacheRepository(mockCacheDao)
    }

    /** CREATE **/
    @Test
    fun `insertPlace should emit Success on successful DAO insertion`() = runTest {
        val placeToInsert = mockUnfavoritePlaces.first()
        coJustRun { mockCacheDao.insertPlaceEntity(any()) }

        val responses = repository.insertPlace(placeToInsert).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { mockCacheDao.insertPlaceEntity(placeToInsert.toEntity()) }
    }

    @Test
    fun `insertPlace should emit Failure when DAO throws exception`() = runTest {
        val placeToInsert = mockUnfavoritePlaces.first()
        val exception = SQLiteConstraintException("Primary key conflict")
        coEvery { mockCacheDao.insertPlaceEntity(any()) } throws exception

        val responses = repository.insertPlace(placeToInsert).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(SQLiteConstraintException::class.java)
        }
        coVerify(exactly = 1) { mockCacheDao.insertPlaceEntity(placeToInsert.toEntity()) }
    }

    /** READ **/
    @Test
    fun `getPlacesByZipcode should emit Success with data`() = runTest {
        val mockEntityData = mockUnfavoritePlaceEntities.map { it.place }
        val expectedPlaces = mockUnfavoritePlaces
        coEvery {
            mockCacheDao.selectCachedPlaceEntitiesByZipcode(any())
        } returns flowOf(mockEntityData)

        val responses = repository.getPlacesByZipcode("").toList()

        responses.assertFlowSuccess { actualPlace ->
            assertThat(actualPlace).containsExactlyElementsIn(expectedPlaces)
        }
        coVerify(exactly = 1) { mockCacheDao.selectCachedPlaceEntitiesByZipcode("") }
    }

    @Test
    fun `getPlacesByZipcode should emit Failure when DAO flow throws exception`() = runTest {
        val exception = SQLiteException("Database read error")
        coEvery { mockCacheDao.selectCachedPlaceEntitiesByZipcode(any()) } returns flow { throw exception }

        val responses = repository.getPlacesByZipcode("").toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(SQLiteException::class.java)
        }
        coVerify(exactly = 1) { mockCacheDao.selectCachedPlaceEntitiesByZipcode("") }
    }

    /** UPDATE **/
    @Test
    fun `updatePlace should emit Success on successful DAO update`() = runTest {
        val placeToUpdate = mockUnfavoritePlaces.first()
        coJustRun { mockCacheDao.updatePlaceEntity(any()) }

        val responses = repository.updatePlace(placeToUpdate).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 1) { mockCacheDao.updatePlaceEntity(placeToUpdate.toEntity()) }
    }

    @Test
    fun `updatePlace should emit Failure when DAO throws exception`() = runTest {
        val placeToUpdate = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Update failed")
        coEvery { mockCacheDao.updatePlaceEntity(any()) } throws exception

        val responses = repository.updatePlace(placeToUpdate).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(SQLiteException::class.java)
        }
        coVerify(exactly = 1) { mockCacheDao.updatePlaceEntity(placeToUpdate.toEntity()) }
    }

    /** DELETE **/
    @Test
    fun `deletePlace should emit Success on successful DAO deletion`() = runTest {
        val placeToDelete = mockUnfavoritePlaces.first()
        coJustRun { mockCacheDao.deleteCachedPlaceEntity(any()) }

        val responses = repository.deletePlace(placeToDelete).toList()

        responses.assertFlowSuccess { assertThat(true).isTrue() }
        coVerify(exactly = 1) { mockCacheDao.deleteCachedPlaceEntity(placeToDelete.cep.text) }
    }

    @Test
    fun `deletePlace should emit Failure when DAO throws exception`() = runTest {
        val placeToDelete = mockUnfavoritePlaces.first()
        val exception = SQLiteException("Delete failed")
        coEvery { mockCacheDao.deleteCachedPlaceEntity(any()) } throws exception

        val responses = repository.deletePlace(placeToDelete).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(SQLiteException::class.java)
        }
        coVerify(exactly = 1) { mockCacheDao.deleteCachedPlaceEntity(placeToDelete.cep.text) }
    }

    @Test
    fun `deleteAllUnwanted should emit Success on successful DAO deletion`() = runTest {
        coJustRun { mockCacheDao.deleteAllCachedPlaceEntities() }

        val responses = repository.deleteAllUnwanted().toList()

        responses.assertFlowSuccess { assertThat(true).isTrue() }
        coVerify(exactly = 1) { mockCacheDao.deleteAllCachedPlaceEntities() }
    }

    @Test
    fun `deleteAllUnwanted should emit Failure when DAO throws exception`() = runTest {
        val exception = SQLiteException("Delete all failed")
        coEvery { mockCacheDao.deleteAllCachedPlaceEntities() } throws exception

        val responses = repository.deleteAllUnwanted().toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(SQLiteException::class.java)
        }
        coVerify(exactly = 1) { mockCacheDao.deleteAllCachedPlaceEntities() }
    }
}