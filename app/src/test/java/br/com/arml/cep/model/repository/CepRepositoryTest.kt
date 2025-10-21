package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.dto.AddressDTO
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntries
import br.com.arml.cep.model.source.local.LogLocalDataSource
import br.com.arml.cep.model.source.local.PlaceLocalDataSource
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CepRepositoryTest {
    private val service = mockk<PlaceRemoteDataSource>()
    private val placeLocalDataSource = mockk<PlaceLocalDataSource>()
    private val logLocalDataSource = mockk<LogLocalDataSource>()
    private lateinit var repository: PlaceRepository

    val mockAddressDTO = AddressDTO(
        cep = "12345678",
        logradouro = "Rua Exemplo",
        complemento = "Apt 101",
        bairro = "Centro",
        localidade = "Cidade",
        uf = "SP",
        estado = "São Paulo",
        regiao = "Sudeste",
        ddd = "11"
    )
    val mockErrorAddressDTO = AddressDTO(erro = "true")
    val expectedAddress = mockAddressDTO.toAddress()

    @Before
    fun setup() {
        repository = PlaceRepository(service,placeLocalDataSource,logLocalDataSource)
    }

    /*** Get Address ***/
    @Test
    fun `should emit success when getAddressByCep works properly`() = runTest {
        val cep = Cep.build("12345678")
        coEvery { service.getAddressByCep(cep.text) } returns mockAddressDTO
        repository.getAddressByCep(cep).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertEquals(expectedAddress, response.result)
                }

                is Response.Loading -> {
                    assertTrue(true)
                }

                is Response.Failure -> {
                    assertTrue(
                        "Deveria ser Success, mas foi Failure",
                        false
                    )
                }
            }
        }
    }

    @Test
    fun `should emit failure when getAddressByCep returns error`() = runTest {
        coEvery { service.getAddressByCep("12345678") } returns mockErrorAddressDTO
        repository.getAddressByCep(Cep.build("12345678")).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertTrue(
                        "Deveria ser Failure devido a Cep não encontrado, mas foi Success",
                        false
                    )
                }
                is Response.Loading -> {
                    assertTrue(true)
                }
                is Response.Failure -> {
                    assertEquals(
                        CepException.NotFoundCepException().message,
                        response.exception.message
                    )
                }
            }
        }
    }

    @Test
    fun `should emit failure when getAddressByCep throws exception`() = runTest {
        val cep = Cep.build("99999999")
        val errorJson = """{"message": "Formato de CEP inválido"}"""
        val errorResponseBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        val mockHttpErrorResponse = retrofit2
            .Response.error<AddressDTO>(
                400,
                errorResponseBody
            )

        coEvery {
            service.getAddressByCep(cep.text)
        } throws HttpException(mockHttpErrorResponse)

        repository.getAddressByCep(cep).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertTrue("Deveria ser Failure devido a HttpException, mas foi Success", false)
                }

                is Response.Loading -> { assertTrue(true) }

                is Response.Failure -> {
                    assertTrue(
                        "A exceção deveria ser HttpException",
                        response.exception is HttpException
                    )
                    val httpException = response.exception as HttpException
                    assertEquals(400, httpException.code())
                }
            }
        }
    }

    /*** Get an Entry ***/
    @Test
    fun `should emit success entry when the cep exists in the database`() = runTest{
        val entry = mockPlaceEntries.first()
        coEvery { placeLocalDataSource.read(any()) } returns entry
        coEvery { logLocalDataSource.create(any()) } returns Unit

        val flow = repository.getPlace(entry.cep)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(entry), collected.last())
        assertEquals(entry, (collected.last() as Response.Success).result)
    }

    @Test
    fun `should emit success entry when the cep does not exists in the database but exists in the api`() = runTest{
        val entry = mockPlaceEntries[0]
        coEvery { placeLocalDataSource.read(any()) } returns null
        coEvery { service.getAddressByCep(any()) } returns entry.address.toAddressDTO()
        coEvery { placeLocalDataSource.create(any()) } returns Unit
        coEvery { logLocalDataSource.create(any()) } returns Unit

        val flow = repository.getPlace(entry.cep)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(entry), collected.last())
        assertEquals(entry, (collected.last() as Response.Success).result)
    }



    @Test
    fun `should emit failure entry when the cep does not exists in the database and the api`() = runTest{
        val entry = mockPlaceEntries[0]
        coEvery { placeLocalDataSource.read(any()) } returns null
        coEvery { service.getAddressByCep(any()) } returns AddressDTO(erro = "true")

        repository.getPlace(entry.cep).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertTrue("Deveria ser Failure,mas foi Success", false)
                }
                is Response.Loading -> { assertTrue(true) }
                is Response.Failure -> {
                    assertEquals(
                        CepException.NotFoundCepException().message,
                        response.exception.message
                    )
                }
            }
        }
    }

    @Test
    fun `should emit failure when http exception occurs`() = runTest{
        val entry = mockPlaceEntries[0]
        val errorJson = """{"message": "Formato de CEP inválido"}"""
        val errorResponseBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        val mockHttpErrorResponse = retrofit2
            .Response.error<AddressDTO>(
                400,
                errorResponseBody
            )

        coEvery { placeLocalDataSource.read(any()) } returns null
        coEvery {
            service.getAddressByCep(any())
        } throws HttpException(mockHttpErrorResponse)

        repository.getPlace(entry.cep).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertTrue("Deveria ser Failure devido a HttpException, mas foi Success", false)
                }
                is Response.Loading -> { assertTrue(true) }
                is Response.Failure -> {
                    assertTrue(
                        "A exceção deveria ser HttpException",
                        response.exception is HttpException
                    )
                    val httpException = response.exception as HttpException
                    assertEquals(400, httpException.code())
                }
            }
        }
    }

    /*** Delete All Unwanted Places ***/
    @Test
    fun `should delete all unwanted places`() = runTest {
        coEvery { placeLocalDataSource.deleteAllNotFavorite() } returns Unit
        val flow = repository.deleteAllUnwantedPlaces()
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(Unit), collected.last())
        assertEquals(Unit, (collected.last() as Response.Success).result)
    }

    /*** Delete Place ***/
    @Test
    fun `should emits Loading and Success when delete a place Entry`() = runTest {
        val entry = mockPlaceEntries[0]
        coEvery { placeLocalDataSource.delete(entry) } returns Unit
        val flow = repository.deletePlace(entry)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(Unit), collected.last())
        assertEquals(Unit, (collected.last() as Response.Success).result)
    }

    /*** Filter Places By Cep ***/
    @Test
    fun `should emits filtered places by cep`() = runTest {
        val cep = mockPlaceEntries.first().cep.text
        val expectedPlaces = mockPlaceEntries.filter { it.cep.text.contains(cep) }
        val flowOfPlaces: Flow<List<PlaceEntry>> =  flowOf(expectedPlaces)
        coEvery { placeLocalDataSource.filterByCep(any()) } returns flowOfPlaces

        val flow = repository.filterPlacesByCep(cep)
        val result = flow.toList().maxBy { it.size }
        assertEquals(expectedPlaces,result)
    }

    /*** Filter Favorite Places by Cep ***/
    @Test
    fun `should emits filtered favorite places by cep`() = runTest {
        val cep = mockPlaceEntries.first().cep.text
        val expectedFavoritePlaces = mockPlaceEntries.filter {
            it.cep.text.contains(cep) && it.isFavorite.value

        }
        val flowOfFavoritePlaces: Flow<List<PlaceEntry>> =  flowOf(expectedFavoritePlaces)
        coEvery { placeLocalDataSource.filterByCepAndFavorite(any()) } returns flowOfFavoritePlaces

        val flow = repository.filterPlacesByCepAndFavorite(cep)
        val result = flow.toList().maxBy { it.size }

        assertEquals(expectedFavoritePlaces,result)
    }

    /*** get Unfavorite Places ***/
    @Test
    fun `should emits only unfavorite places`() = runTest {
        val expectedUnfavoritePlaces = mockPlaceEntries.filter { !it.isFavorite.value }
        val flowOfUnfavoritePlaces: Flow<List<PlaceEntry>> =  flowOf(expectedUnfavoritePlaces)
        coEvery { placeLocalDataSource.readUnwanted() } returns flowOfUnfavoritePlaces

        val flow = repository.getUnwantedPlaces()
        val result = flow.toList().maxBy { it.size }
        assertEquals(expectedUnfavoritePlaces,result)
    }

    @Test
    fun `should emits a specific unfavorite place by cep`() = runTest {
        val cep = mockPlaceEntries.first().cep.text
        val expectedUnfavoritePlaces = mockPlaceEntries.filter {
            it.cep.text.contains(cep) && !it.isFavorite.value
        }
        val flowOfUnfavoritePlaces: Flow<List<PlaceEntry>> =  flowOf(expectedUnfavoritePlaces)
        coEvery { placeLocalDataSource.filterByCepAndUnwanted(any()) } returns flowOfUnfavoritePlaces

        val flow = repository.getUnwantedPlacesByCepAndUnwanted(cep)
        val result = flow.toList().maxBy { it.size }
        assertEquals(expectedUnfavoritePlaces,result)
    }

    /*** Update Place ***/
    @Test
    fun `should update an existent place`() = runTest {
        val oldEntry = mockUnfavoritePlaceEntries.first()
        val updatedEntry = oldEntry.copy(isFavorite = Favorite(true))
        coEvery { placeLocalDataSource.update(updatedEntry) } returns Unit
        val flow = repository.updatePlace(updatedEntry)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(Unit), collected.last())
        assertEquals(Unit, (collected.last() as Response.Success).result)
    }

    /*** Import Places ***/
    @Test
    fun `should create new places when they do not exist in the database`() = runTest {
        val importedPlaces = mockPlaceEntries
        importedPlaces.forEach { place ->
            coEvery { placeLocalDataSource.read(place.cep.text) } returns null
            coEvery { placeLocalDataSource.create(place) } returns Unit
        }
        val flow = repository.importFavoritePlaces(importedPlaces)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(Unit), collected.last())
        assertEquals(Unit, (collected.last() as Response.Success).result)
    }

    @Test
    fun `should update places when they exist in the database`() = runTest {
        val importedPlaces = mockPlaceEntries
        importedPlaces.forEach { place ->
            coEvery { placeLocalDataSource.read(place.cep.text) } returns place
            coEvery { placeLocalDataSource.update(place) } returns Unit
        }

        val flow = repository.importFavoritePlaces(importedPlaces)
        val collected = flow.toList()

        assertEquals(2,collected.size)
        assertEquals(Response.Loading, collected.first())
        assertEquals(Response.Success(Unit), collected.last())
        assertEquals(Unit, (collected.last() as Response.Success).result)
    }
}