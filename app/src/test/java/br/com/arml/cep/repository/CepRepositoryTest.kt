package br.com.arml.cep.repository

import br.com.arml.cep.model.dto.AddressDTO
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.mock.mockCepSearchEntry
import br.com.arml.cep.model.repository.PlaceRepository
import br.com.arml.cep.model.source.local.PlaceDao
import br.com.arml.cep.model.source.remote.CepApiService
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.model.source.local.LogDao
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CepRepositoryTest {
    private val service = mockk<CepApiService>()
    private val placeDao = mockk<PlaceDao>()
    private val logDao = mockk<LogDao>()
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
        repository = PlaceRepository(service,placeDao,logDao)
    }

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
        // getCepEntry(cep: Cep)
        val entry = mockPlaceEntries[0]
        coEvery { placeDao.read(any()) } returns entry
        repository.getPlace(entry.cep).collect { response ->
            when (response) {
                is Response.Success -> { assertEquals(entry, response.result) }
                is Response.Loading -> { assertTrue(true) }
                is Response.Failure -> {
                    assertTrue("Deveria ser Success, mas foi Failure", false)
                }
            }
        }
    }

    @Test
    fun `should emit success entry when the cep does not exists in the database but exists in the api`() = runTest{
        val entry = mockCepSearchEntry[0]
        coEvery { placeDao.read(any()) } returns null
        coEvery { service.getAddressByCep(any()) } returns entry.address.toAddressDTO()
        coEvery { placeDao.create(any()) } returns Unit

        repository.getPlace(entry.cep).collect { response ->
            when (response) {
                is Response.Success -> {
                    assertEquals(entry.cep, response.result.cep)
                    assertEquals(entry.address, response.result.address)
                    assertEquals(entry.isFavorite, response.result.isFavorite)
                    assertEquals(entry.note, response.result.note)
                }
                is Response.Loading -> { assertTrue(true) }
                is Response.Failure -> {
                    assertTrue("Deveria ser Success, mas foi Failure", false)
                }
            }
        }
    }



    @Test
    fun `should emit failure entry when the cep does not exists in the database and the api`() = runTest{
        val entry = mockCepSearchEntry[0]
        coEvery { placeDao.read(any()) } returns null
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
        val entry = mockCepSearchEntry[0]
        val errorJson = """{"message": "Formato de CEP inválido"}"""
        val errorResponseBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        val mockHttpErrorResponse = retrofit2
            .Response.error<AddressDTO>(
                400,
                errorResponseBody
            )

        coEvery { placeDao.read(any()) } returns null
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

}