package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.toPlaceWithNotes
import br.com.arml.cep.model.entity.dto.AddressDTO
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {
    private val searchService = mockk<PlaceRemoteDataSource>()
    private val cacheDao = mockk<CacheDao>()
    private val logDao = mockk<LogDao>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepository(searchService, cacheDao, logDao)
        coJustRun { logDao.insertLogEntity(any()) }
    }

    // region Mock Helper Functions
    private fun mockCacheHit(place: Place) {
        coEvery { cacheDao.selectPlaceWithNotesByZipcode(place.cep.text) } returns place.toPlaceWithNotes()
    }

    private fun mockCacheMiss() {
        coEvery { cacheDao.selectPlaceWithNotesByZipcode(any()) } returns null
    }

    private fun mockRemoteSuccess(place: Place) {
        coEvery { searchService.getAddressByCep(place.cep.text) } returns place.address.toAddressDTO()
        coJustRun { cacheDao.insertPlaceEntity(any()) }
    }

    private fun mockRemoteNotFound() {
        coEvery { searchService.getAddressByCep(any()) } returns AddressDTO(erro = "true")
    }

    private fun mockRemoteThrows(exception: Exception) {
        coEvery { searchService.getAddressByCep(any()) } throws exception
    }
    // endregion

    @Test
    fun `getPlace should retrieve from local when entry exists`() = runTest {
        val expected = mockUnfavoritePlaces.first()
        mockCacheHit(expected)

        val responses = repository.getPlace(expected.cep).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expected) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expected.cep.text) }
        coVerify(exactly = 1) { logDao.insertLogEntity(any()) }
        coVerify(exactly = 0) { searchService.getAddressByCep(any()) }
    }

    @Test
    fun `getPlace should retrieve from remote when entry does not exist locally`() = runTest {
        val expected = mockUnfavoritePlaces.first()
        mockCacheMiss()
        mockRemoteSuccess(expected)

        val responses = repository.getPlace(expected.cep).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(expected) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expected.cep.text) }
        coVerify(exactly = 1) { searchService.getAddressByCep(expected.cep.text) }
        coVerify(exactly = 1) { logDao.insertLogEntity(any()) }
    }

    @Test
    fun `getPlace should fail when CEP is not found`() = runTest {
        val cep = mockUnfavoritePlaces.first().cep
        mockCacheMiss()
        mockRemoteNotFound()

        val responses = repository.getPlace(cep).toList()

        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(CepException.NotFoundCepException::class.java)
        }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(cep.text) }
        coVerify(exactly = 1) { searchService.getAddressByCep(cep.text) }
        coVerify(exactly = 0) { logDao.insertLogEntity(any()) }
    }

    @Test
    fun `getPlace should fail when remote service throws an exception`() = runTest {
        val cep = mockUnfavoritePlaces.first().cep
        val expectedException = Exception("Remote service failure")
        mockCacheMiss()
        mockRemoteThrows(expectedException)

        val responses = repository.getPlace(cep).toList()

        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException::class.java) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(cep.text) }
        coVerify(exactly = 1) { searchService.getAddressByCep(cep.text) }
        coVerify(exactly = 0) { logDao.insertLogEntity(any()) }
    }
}
