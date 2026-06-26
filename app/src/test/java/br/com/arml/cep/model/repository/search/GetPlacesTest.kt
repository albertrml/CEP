@file:Suppress("UNUSED_EXPRESSION")
package br.com.arml.cep.model.repository.search

import android.util.Log
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.exception.CepDatabaseException
import br.com.arml.cep.model.exception.ViaCepException
import br.com.arml.cep.model.mock.mockCity
import br.com.arml.cep.model.mock.mockStreet
import br.com.arml.cep.model.mock.mockUF
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntities
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.repository.SearchRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import br.com.arml.cep.model.utils.normalizeForAPISearch
import br.com.arml.cep.model.utils.normalizeForDBSearch
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockAnswer
import br.com.arml.cep.utils.mockFlowAnswer
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPlacesTest {
    private val service = mockk<PlaceRemoteDataSource>()
    private val cacheDao = mockk<CacheDao>()
    private val logDao = mockk<LogDao>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(SearchRepository(service, cacheDao, logDao))
    }

    private fun mockCacheUpdate(
        uf: String,
        city: String,
        street: String,
        cepList: List<Cep>,
        exception: Exception? = null
    ) = mockAnswer(
        {
            repository.updateCache(
                uf,
                city.normalizeForDBSearch(),
                street.normalizeForDBSearch()
            )
        },
        cepList,
        exception
    )

    private fun mockLogAccess(
        cepList: List<Cep>,
        exception: Exception? = null
    ) = mockAnswer(
        { cepList.forEach { repository.logAccess(it) } },
        Unit,
        exception
    )

    private fun mockCacheSearch(
        uf: String,
        city: String,
        street: String,
        places: List<PlaceWithNotes>,
        exception: Exception? = null
    ) = mockFlowAnswer(
        {
            cacheDao.selectPlacesWithNotes(
                uf,
                city.normalizeForDBSearch(),
                street.normalizeForDBSearch()
            )
        },
        places,
        exception
    )

    /*
     * Function: getPlaces(uf: String, city: String, street: String)
     * Dependencies:
     *  - From SearchRepository: updateCache
     *  - From cacheDao: selectPlacesWithNotes
     */

    @Test
    fun `getPlaces should returns Success when there are or are not places in database and there are in remote`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCityOnDB = city.normalizeForDBSearch()
            val expectedStreetOnDB = street.normalizeForDBSearch()
            val expectedUFOnApi = uf.normalizeForAPISearch()
            val expectedCityOnApi = city.normalizeForAPISearch()
            val expectedStreetOnApi = street.normalizeForAPISearch()
            val expectedPlaceList = mockUnfavoritePlaces
            val expectedPlaceWithNotesList = mockUnfavoritePlaceEntities
            val expectedCepList = expectedPlaceList.map { it.cep }

            mockCacheUpdate(
                expectedUFOnApi,
                expectedCityOnApi,
                expectedStreetOnApi,
                expectedCepList
            )
            mockLogAccess(expectedCepList)
            mockCacheSearch(
                uf,
                expectedCityOnDB,
                expectedStreetOnDB,
                expectedPlaceWithNotesList
            )

            val responses = repository.getPlaces(uf, city, street).toList()
            responses.assertFlowSuccess { assertThat(it).isEqualTo(expectedPlaceList) }

            coVerify(exactly = 1) {
                repository.updateCache(
                    expectedUFOnApi,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
            coVerify(exactly = 1) { expectedCepList.forEach { repository.logAccess(it) } }
            coVerify(exactly = 1) {
                cacheDao.selectPlacesWithNotes(
                    uf,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
        }

    @Test
    fun `getPlaces should returns Failure when updateCache throws exception`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCityOnDB = city.normalizeForDBSearch()
            val expectedStreetOnDB = street.normalizeForDBSearch()
            val expectedUFOnApi = uf.normalizeForAPISearch()
            val expectedCityOnApi = city.normalizeForAPISearch()
            val expectedStreetOnApi = street.normalizeForAPISearch()
            val expectedPlaceList = mockUnfavoritePlaces
            val expectedCepList = expectedPlaceList.map { it.cep }
            val expectedException = ViaCepException.NetworkOfflineException()

            mockCacheUpdate(
                expectedUFOnApi,
                expectedCityOnApi,
                expectedStreetOnApi,
                expectedCepList,
                expectedException
            )
            mockCacheSearch(
                uf,
                expectedCityOnDB,
                expectedStreetOnDB,
                emptyList()
            )

            val responses = repository.getPlaces(uf, city, street).toList()
            responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

            coVerify(exactly = 1) {
                repository.updateCache(
                    expectedUFOnApi,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
            coVerify(exactly = 0) { expectedCepList.forEach { repository.logAccess(it) } }
            coVerify(exactly = 1) {
                cacheDao.selectPlacesWithNotes(
                    uf,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
        }

    @Test
    fun `getPlaces should returns Failure when log can not insert a registry into database`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCityOnDB = city.normalizeForDBSearch()
            val expectedStreetOnDB = street.normalizeForDBSearch()
            val expectedUFOnApi = uf.normalizeForAPISearch()
            val expectedCityOnApi = city.normalizeForAPISearch()
            val expectedStreetOnApi = street.normalizeForAPISearch()
            val expectedPlaceList = mockUnfavoritePlaces
            val expectedCepList = expectedPlaceList.map { it.cep }
            val expectedException = CepDatabaseException.DiskFullException()

            mockCacheUpdate(
                expectedUFOnApi,
                expectedCityOnApi,
                expectedStreetOnApi,
                expectedCepList
            )
            mockLogAccess(expectedCepList, expectedException)
            mockCacheSearch(
                uf,
                expectedCityOnDB,
                expectedStreetOnDB,
                emptyList()
            )

            val responses = repository.getPlaces(uf, city, street).toList()
            responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

            coVerify(exactly = 1) {
                repository.updateCache(
                    expectedUFOnApi,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
            coVerify(exactly = 1) { expectedCepList.forEach { repository.logAccess(it) } }
            coVerify(exactly = 1) {
                cacheDao.selectPlacesWithNotes(
                    uf,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
        }

    @Test
    fun `getPlaces should returns Failure when search throws an database exception`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCityOnDB = city.normalizeForDBSearch()
            val expectedStreetOnDB = street.normalizeForDBSearch()
            val expectedUFOnApi = uf.normalizeForAPISearch()
            val expectedCityOnApi = city.normalizeForAPISearch()
            val expectedStreetOnApi = street.normalizeForAPISearch()
            val expectedPlaceList = mockUnfavoritePlaces
            val expectedCepList = expectedPlaceList.map { it.cep }
            val expectedException = CepDatabaseException.DiskFullException()

            mockCacheUpdate(
                expectedUFOnApi,
                expectedCityOnApi,
                expectedStreetOnApi,
                expectedCepList
            )
            mockLogAccess(expectedCepList)
            mockCacheSearch(
                uf,
                expectedCityOnDB,
                expectedStreetOnDB,
                emptyList(),
                expectedException
            )

            val responses = repository.getPlaces(uf, city, street).toList()
            responses.assertFlowFailure { assertThat(it).isInstanceOf(expectedException.javaClass) }

            coVerify(exactly = 1) {
                repository.updateCache(
                    expectedUFOnApi,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
            coVerify(exactly = 1) { expectedCepList.forEach { repository.logAccess(it) } }
            coVerify(exactly = 1) {
                cacheDao.selectPlacesWithNotes(
                    uf,
                    expectedCityOnDB,
                    expectedStreetOnDB
                )
            }
        }
}