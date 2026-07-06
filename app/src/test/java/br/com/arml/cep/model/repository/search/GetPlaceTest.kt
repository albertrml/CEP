package br.com.arml.cep.model.repository.search

import android.util.Log
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.exception.CepDatabaseException
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.exception.ViaCepException
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import br.com.arml.cep.model.repository.SearchRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockAnswer
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPlaceTest {
    private val service = mockk<PlaceRemoteDataSource>()
    private val cacheDao = mockk<CacheDao>()
    private val logDao = mockk<LogDao>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(),any(),any()) } returns 0

        repository = spyk(SearchRepository(service, cacheDao, logDao))
    }

    private fun mockCacheDoesItExist(
        zipcode: String,
        isFound: Boolean,
        exception: Exception? = null,
    ) = mockAnswer({ cacheDao.isPlaceExist(zipcode) }, isFound, exception)

    private fun mockCacheSearch(
        zipcode: String,
        data: PlaceWithNotes?,
        exception: Exception? = null,
    ) = mockAnswer({ cacheDao.selectPlaceWithNotesByZipcode(zipcode) }, data, exception)

    private fun mockRepositoryLogAccess(
        cep: Cep,
        exception: Exception? = null,
    ) = mockAnswer({ repository.logAccess(cep) }, Unit, exception)

    private fun mockRepositoryUpdateCache(
        cep: Cep,
        exception: Exception? = null,
    ) = mockAnswer({ repository.updateCache(cep) }, Unit, exception)

    /*
     * Function: getPlace(cep: Cep)
     * Dependencies:
     *  - From SearchRepository: updateCache
     *  - From cacheDao: isPlaceExist, selectPlaceWithNotesByZipcode
     */

    @Test
    fun `getPlace should success when retrieve from local when entry exists and update cache normally`() = runTest {
        val expectedPlaceWithNotes = mockUnfavoritePlaceEntities.first()
        val expectedPlace = expectedPlaceWithNotes.toModel()
        val expectedZipcode = expectedPlaceWithNotes.place.zipcode
        val expectedCep = Cep.build(expectedZipcode)

        mockRepositoryUpdateCache(expectedCep)
        mockCacheSearch(expectedZipcode, expectedPlaceWithNotes)
        mockRepositoryLogAccess(expectedCep)

        val responses = repository.getPlace(expectedCep).toList()

        assert(responses.size == 2)
        responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedPlace) }

        coVerify(exactly = 1) { repository.updateCache(expectedCep) }
        coVerify(exactly = 0) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expectedZipcode) }
        coVerify(exactly = 1) { repository.logAccess(expectedCep) }
    }

    @Test
    fun `getPlace should fails when update cache throws exception and place is not found`() = runTest {
        val expectedPlaceWithNotes = mockUnfavoritePlaceEntities.first()
        val expectedZipcode = expectedPlaceWithNotes.place.zipcode
        val expectedCep = Cep.build(expectedZipcode)
        val expectedException = ViaCepException.NetworkOfflineException()

        mockRepositoryUpdateCache(expectedCep, expectedException)
        mockCacheDoesItExist(expectedZipcode, false)

        val responses = repository.getPlace(expectedCep).toList()

        assertEquals(2, responses.size)
        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

        coVerify(exactly = 1) { repository.updateCache(expectedCep) }
        coVerify(exactly = 1) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 0) { cacheDao.selectPlaceWithNotesByZipcode(expectedZipcode) }
        coVerify(exactly = 0) { repository.logAccess(expectedCep) }
    }

    @Test
    fun `getPlace should success when update cache throws exception and place is found`() = runTest {
        val expectedPlaceWithNotes = mockUnfavoritePlaceEntities.first()
        val expectedPlace = expectedPlaceWithNotes.toModel()
        val expectedZipcode = expectedPlaceWithNotes.place.zipcode
        val expectedCep = Cep.build(expectedZipcode)
        val expectedException = ViaCepException.NetworkOfflineException()

        mockRepositoryUpdateCache(expectedCep, expectedException)
        mockCacheDoesItExist(expectedZipcode, true)
        mockCacheSearch(expectedZipcode, expectedPlaceWithNotes)
        mockRepositoryLogAccess(expectedCep)

        val responses = repository.getPlace(expectedCep).toList()

        assertEquals(2, responses.size)
        responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedPlace) }

        coVerify(exactly = 1) { repository.updateCache(expectedCep) }
        coVerify(exactly = 1) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expectedZipcode) }
        coVerify(exactly = 1) { repository.logAccess(expectedCep) }
    }

    @Test
    fun `getPlace should fail when CEP is not found locally and remotely`() = runTest {
        val expectedPlaceWithNotes = mockUnfavoritePlaceEntities.first()
        val expectedZipcode = expectedPlaceWithNotes.place.zipcode
        val expectedCep = Cep.build(expectedZipcode)
        val expectedException = CepException.NotFoundCepException()

        mockRepositoryUpdateCache(expectedCep)
        mockCacheDoesItExist(expectedZipcode, false)
        mockCacheSearch(expectedZipcode, null)

        val responses = repository.getPlace(expectedCep).toList()

        assertEquals(2, responses.size)
        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

        coVerify(exactly = 1) { repository.updateCache(expectedCep) }
        coVerify(exactly = 0) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expectedZipcode) }
        coVerify(exactly = 0) { repository.logAccess(expectedCep) }
    }

    @Test
    fun `getPlace should fails when log access throws exception`() = runTest {
        val expectedPlaceWithNotes = mockUnfavoritePlaceEntities.first()
        val expectedZipcode = expectedPlaceWithNotes.place.zipcode
        val expectedCep = Cep.build(expectedZipcode)
        val expectedException = CepDatabaseException.DatabaseCorruptException()

        mockRepositoryUpdateCache(expectedCep)
        mockCacheSearch(expectedZipcode, expectedPlaceWithNotes)
        mockRepositoryLogAccess(expectedCep, expectedException)

        val responses = repository.getPlace(expectedCep).toList()

        assertEquals(2, responses.size)
        responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

        coVerify(exactly = 1) { repository.updateCache(expectedCep) }
        coVerify(exactly = 0) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 1) { cacheDao.selectPlaceWithNotesByZipcode(expectedZipcode) }
        coVerify(exactly = 1) { repository.logAccess(expectedCep) }
    }
}