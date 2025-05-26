package br.com.arml.cep.domain

import br.com.arml.cep.model.entity.Cep
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.model.repository.CepRepository
import br.com.arml.cep.util.exception.CepException
import br.com.arml.cep.util.type.Response
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CepUseCaseTest {
    private val repository = mockk<CepRepository>()
    private lateinit var useCase: CepUseCase

    private val mockZipCode = mockAddress.zipCode
    private val mockCep = Cep.build(mockZipCode)

    @Before
    fun setup(){
        useCase = CepUseCase(repository)
    }

    @Test
    fun `should emit success when repository returns success`() = runTest{
        coEvery {
            repository.getAddressByCep(mockCep)
        } returns flowOf(Response.Success(mockAddress))

        useCase.searchCep(mockZipCode).collect{ response ->
            when(response){
                is Response.Success -> {
                    assertTrue(
                        "The response address must be equals to mockAddress",
                        response.result == mockAddress
                    )
                }
                is Response.Loading -> { assertTrue(true)}
                is Response.Failure -> {
                    assertTrue(
                        "Should emmit success, not failure",
                        false
                    )
                }
            }
        }
    }

    @Test
    fun `should emit failure when zipcode doesn't contains only digits`() = runTest{
        val invalidZipCodes = listOf(
            "12345-789","a1234-567", "1234567@", "teste123"
        )
        invalidZipCodes.forEachIndexed { index, invalidZipCode ->
            useCase.searchCep(invalidZipCode).collect{ response ->
                when(response){
                    is Response.Success -> {
                        assertTrue(
                            "Should emmit failure, not success",
                            false
                        )
                    }
                    is Response.Loading -> { assertTrue(true) }
                    is Response.Failure -> {
                        assertTrue(
                            "[${index}]: The exception must be InputCepException",
                            response.exception is CepException.InputCepException
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `should emit failure when zipcode doesn't contains 8 digits`() = runTest{
        val invalidZipCodes = listOf(
            "1234567","123456789"
        )
        invalidZipCodes.forEachIndexed { index, invalidZipCode ->
            useCase.searchCep(invalidZipCode).collect{ response ->
                when(response){
                    is Response.Success -> {
                        assertTrue(
                            "Should emmit failure, not success",
                            false
                        )
                    }
                    is Response.Loading -> { assertTrue(true) }
                    is Response.Failure -> {
                        assertTrue(
                            "[${index}]: The exception must be SizeCepException",
                            response.exception is CepException.SizeCepException
                        )
                    }
                }
            }
        }
    }

    @Test
    fun`should emit failure when zipcode is empty`() = runTest{
        useCase.searchCep("").collect{ response ->
            when(response){
                is Response.Success -> {
                    assertTrue(
                        "Should emmit failure, not success",
                        false
                    )
                }
                is Response.Loading -> { assertTrue(true) }
                is Response.Failure -> {
                    assertTrue(
                        "The exception must be EmptyCepException",
                        response.exception is CepException.EmptyCepException
                    )
                }
            }
        }
    }

    @Test
    fun `should emit failure when repository doesn't find an address for zipcode`() = runTest {
        val invalidZipCode = "99999999"
        coEvery {
            repository.getAddressByCep(Cep.build(invalidZipCode))
        } returns flowOf(Response.Failure(CepException.NotFoundCepException()))

        useCase.searchCep(invalidZipCode).collect { response ->
            when(response){
                 is Response.Success -> {
                     assertTrue("Should emmit failure, not success",false)
                 }
                 is Response.Loading -> { assertTrue(true) }
                 is Response.Failure -> {
                     assertTrue(
                         "The exception must be NotFoundCepException",
                         response.exception is CepException.NotFoundCepException
                     )
                 }
            }
        }
    }
}