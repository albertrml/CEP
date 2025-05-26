package br.com.arml.cep.repository

import br.com.arml.cep.model.entity.AddressDTO
import br.com.arml.cep.model.entity.Cep
import br.com.arml.cep.model.repository.CepRepository
import br.com.arml.cep.model.source.CepApiService
import br.com.arml.cep.util.exception.CepException
import br.com.arml.cep.util.type.Response
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
    private lateinit var repository: CepRepository

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
        repository = CepRepository(service)
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

                is Response.Loading -> {
                    assertTrue(true)
                }

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