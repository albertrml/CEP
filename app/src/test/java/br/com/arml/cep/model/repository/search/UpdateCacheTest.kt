package br.com.arml.cep.model.repository.search

import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.dto.AddressDTO
import br.com.arml.cep.model.entity.isContentEquals
import br.com.arml.cep.model.exception.CepDatabaseException.DatabaseCorruptException
import br.com.arml.cep.model.exception.CepDatabaseException.DiskFullException
import br.com.arml.cep.model.exception.CepDatabaseException.UnknownDatabaseException
import br.com.arml.cep.model.exception.ViaCepException
import br.com.arml.cep.model.mock.mockAddressesDTO
import br.com.arml.cep.model.mock.mockCity
import br.com.arml.cep.model.mock.mockStreet
import br.com.arml.cep.model.mock.mockUF
import br.com.arml.cep.model.repository.SearchRepository
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import br.com.arml.cep.utils.mockAnswer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class UpdateCacheTest {
    private val service = mockk<PlaceRemoteDataSource>()
    private val cacheDao = mockk<CacheDao>()
    private val logDao = mockk<LogDao>()
    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(),any(),any()) } returns 0

        repository = SearchRepository(service, cacheDao, logDao)
    }

    private fun mockCacheSearch(
        zipcode: String,
        isFound: Boolean,
        exception: Exception? = null,
    ) = mockAnswer({ cacheDao.isPlaceExist(zipcode) }, isFound, exception)

    private fun mockCacheInsert(
        placeEntity: PlaceEntity,
        id: Long = 0L,
        exception: Exception? = null,
    ) = mockAnswer({ cacheDao.insertPlaceEntity(placeEntity) }, id, exception)

    private fun mockAPIFetchByCep(
        zipcode: String,
        dto: AddressDTO,
        exception: Exception? = null,
    ) = mockAnswer({ service.getAddressByCep(zipcode) }, dto, exception)

    private fun mockAPIFetchByAddress(
        uf: String,
        city: String,
        street: String,
        dtos: List<AddressDTO>,
        exception: Exception? = null,
    ) = mockAnswer({ service.getAddresses(uf, city, street) }, dtos, exception)

    /*
     * Function: updateCache(cep: Cep)
     * Dependencies:
     *  - From cacheDao: isPlaceExist, insertPlaceEntity
     *  - From service: getAddressByCep
     */

    @Test
    fun `updatePlace by CEP should update database with remote data when place is not found in database`() =
        runTest {
            val expectedAddressDTO = mockAddressesDTO.first()
            val expectedPlaceEntity = expectedAddressDTO.toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)

            mockCacheSearch(query, false)
            mockAPIFetchByCep(query, expectedAddressDTO)
            coEvery { cacheDao.insertPlaceEntity(any()) } returns 1L

            repository.updateCache(expectedCep)

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 1) { service.getAddressByCep(query) }
            coVerify(exactly = 1) {
                cacheDao.insertPlaceEntity(
                    match { it.isContentEquals(expectedPlaceEntity) }
                )
            }
        }

    @Test
    fun `updatePlace by CEP should not update database when place is found in database`() =
        runTest {
            val expectedAddressDTO = mockAddressesDTO.first()
            val expectedPlaceEntity = expectedAddressDTO.toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)

            mockCacheSearch(query, true)

            repository.updateCache(expectedCep)

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 0) { service.getAddressByCep(query) }
            coVerify(exactly = 0) { cacheDao.insertPlaceEntity(expectedPlaceEntity) }
        }

    @Test
    fun `updatePlace by CEP should do nothing when place is not found in database and remote data sent error`() =
        runTest {
            val expectedAddressDTO = AddressDTO(erro = "true")
            val expectedPlaceEntity = mockAddressesDTO.first().toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)

            mockCacheSearch(query, false)
            mockAPIFetchByCep(query, expectedAddressDTO)

            repository.updateCache(expectedCep)

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 1) { service.getAddressByCep(query) }
            coVerify(exactly = 0) { cacheDao.insertPlaceEntity(expectedPlaceEntity) }
        }

    @Test
    fun `updatePlace by CEP should throws exception when isPlaceExist throws exception`() =
        runTest {
            val expectedAddressDTO = mockAddressesDTO.first()
            val expectedPlaceEntity = expectedAddressDTO.toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)
            val expectedException = DatabaseCorruptException()

            mockCacheSearch(query, false, exception = expectedException)
            mockAPIFetchByCep(query, expectedAddressDTO)
            mockCacheInsert(expectedPlaceEntity)

            assertThrows(expectedException.javaClass) {
                runBlocking { repository.updateCache(expectedCep) }
            }

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 0) { service.getAddressByCep(query) }
            coVerify(exactly = 0) { cacheDao.insertPlaceEntity(expectedPlaceEntity) }
        }

    @Test
    fun `updatePlace by CEP should throws exception when getAddressByCep throws exception`() =
        runTest {
            val expectedAddressDTO = mockAddressesDTO.first()
            val expectedPlaceEntity = expectedAddressDTO.toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)
            val expectedException = ViaCepException.InvalidQueryException()

            mockCacheSearch(query, false)
            mockAPIFetchByCep(query, expectedAddressDTO, exception = expectedException)

            assertThrows(expectedException.javaClass) {
                runBlocking { repository.updateCache(expectedCep) }
            }

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 1) { service.getAddressByCep(query) }
            coVerify(exactly = 0) { cacheDao.insertPlaceEntity(expectedPlaceEntity) }
        }

    @Test
    fun `updatePlace by CEP should throws exception when insertPlaceEntity throws exception`() =
        runTest {
            val expectedAddressDTO = mockAddressesDTO.first()
            val expectedPlaceEntity = expectedAddressDTO.toPlaceEntity()
            val query = expectedPlaceEntity.zipcode
            val expectedCep = Cep.build(query)
            val expectedException = DatabaseCorruptException()

            mockCacheSearch(query, false)
            mockAPIFetchByCep(query, expectedAddressDTO)
            coEvery { cacheDao.insertPlaceEntity(any()) } throws expectedException

            assertThrows(expectedException.javaClass) {
                runBlocking { repository.updateCache(expectedCep) }
            }

            coVerify(exactly = 1) { cacheDao.isPlaceExist(query) }
            coVerify(exactly = 1) { service.getAddressByCep(query) }
            coVerify(exactly = 1) { cacheDao.insertPlaceEntity(any()) }
        }

    /*
     * Function: updateCache(uf: String, city: String, street: String)
     * Dependencies:
     *  - From cacheDao: isPlaceExist, insertPlaceEntity
     *  - From service: getAddresses
     */

    @Test
    fun `updatePlace by Address should update database with remote data when place is not found in database`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCepList = mockAddressesDTO.map { Cep.build(it.cep!!) }

            mockAPIFetchByAddress(uf, city, street, mockAddressesDTO)
            mockAddressesDTO.forEachIndexed { index, dTO ->
                mockCacheSearch(dTO.cep!!, false)
                coEvery { cacheDao.insertPlaceEntity(any()) } returns (index + 1).toLong()
            }

            val actualCepList = repository.updateCache(uf, city, street)

            assertEquals(expectedCepList, actualCepList)
            coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
            mockAddressesDTO.forEach { addressDTO ->
                coVerify(exactly = 1) { cacheDao.isPlaceExist(addressDTO.cep!!) }
                coVerify(exactly = 1) {
                    cacheDao.insertPlaceEntity(
                        match { it.isContentEquals(addressDTO.toPlaceEntity()) }
                    )
                }
            }
        }

    @Test
    fun `updatePlace by Address should not update with remote data when place is found in database`() =
        runTest {
            val uf = mockUF
            val city = mockCity
            val street = mockStreet
            val expectedCepList = mockAddressesDTO.map { Cep.build(it.cep!!) }

            mockAPIFetchByAddress(uf, city, street, mockAddressesDTO)
            mockAddressesDTO.forEachIndexed { index, dTO ->
                if (index % 2 == 0) {
                    mockCacheSearch(dTO.cep!!, false)
                    coEvery { cacheDao.insertPlaceEntity(any()) } returns (index + 1).toLong()
                } else {
                    mockCacheSearch(dTO.cep!!, true)
                }
            }

            val actualCepList = repository.updateCache(uf, city, street)

            assertEquals(expectedCepList, actualCepList)
            coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
            mockAddressesDTO.forEachIndexed { index, dTO ->
                val count = if (index % 2 == 0) 1 else 0
                coVerify(exactly = 1) { cacheDao.isPlaceExist(dTO.cep!!) }
                coVerify(exactly = count) {
                    cacheDao.insertPlaceEntity(
                        match { it.isContentEquals(dTO.toPlaceEntity()) }
                    )
                }
            }
        }

    @Test
    fun `updatePlace by Address should not update when remote return empty list`() = runTest {
        val uf = mockUF
        val city = mockCity
        val street = mockStreet
        val expectedCepList = emptyList<Cep>()

        mockAPIFetchByAddress(uf, city, street, emptyList())

        val actualCepList = repository.updateCache(uf, city, street)

        assertEquals(expectedCepList, actualCepList)
        coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
        coVerify(exactly = 0) { cacheDao.isPlaceExist(any()) }
        coVerify(exactly = 0) { cacheDao.insertPlaceEntity(any()) }
    }

    @Test
    fun `updatePlace by Address should throws exception when getAddresses throws exception`() = runTest {
        val uf = mockUF
        val city = mockCity
        val street = mockStreet
        val exceptionThrown = UnknownHostException()
        val expectedException = ViaCepException.NetworkOfflineException()

        mockAPIFetchByAddress(uf, city, street, emptyList(), exceptionThrown)

        assertThrows(expectedException.javaClass){
            runBlocking{ repository.updateCache(uf, city, street) }
        }

        coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
        coVerify(exactly = 0) { cacheDao.isPlaceExist(any()) }
        coVerify(exactly = 0) { cacheDao.insertPlaceEntity(any()) }
    }

    @Test
    fun `updatePlace by Address should throws exception when isPlaceExist throws exception`() = runTest {
        val uf = mockUF
        val city = mockCity
        val street = mockStreet
        val expectedZipcode = mockAddressesDTO.first().cep!!
        val exceptionThrown = SQLiteDiskIOException()
        val expectedException = UnknownDatabaseException("")

        mockAPIFetchByAddress(uf, city, street, mockAddressesDTO)
        mockCacheSearch(expectedZipcode, false, exception = exceptionThrown)

        assertThrows(expectedException.javaClass){
            runBlocking{ repository.updateCache(uf, city, street) }
        }

        coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
        coVerify(exactly = 1) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 0) { cacheDao.insertPlaceEntity(any()) }
    }

    @Test
    fun `updatePlace by Address should throws exception when insertPlaceEntity throws exception`() = runTest {
        val uf = mockUF
        val city = mockCity
        val street = mockStreet
        val expectedZipcode = mockAddressesDTO.first().cep!!
        val exceptionThrown = SQLiteFullException()
        val expectedException = DiskFullException()

        mockAPIFetchByAddress(uf, city, street, mockAddressesDTO)
        mockCacheSearch(expectedZipcode, false)
        coEvery { cacheDao.insertPlaceEntity(any()) } throws exceptionThrown

        assertThrows(expectedException.javaClass){
            runBlocking{ repository.updateCache(uf, city, street) }
        }

        coVerify(exactly = 1) { service.getAddresses(uf, city, street) }
        coVerify(exactly = 1) { cacheDao.isPlaceExist(expectedZipcode) }
        coVerify(exactly = 1) { cacheDao.insertPlaceEntity(any()) }
    }
}