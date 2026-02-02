package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.exception.AdapterException.InputDoesNotMatchCepPatternException
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CepAdapterTest {
    @Test
    fun `should convert String to Cep and back`() {
        val expectedCepString = "12345-678"
        val expectedCep = Cep.build(expectedCepString)
        val actualCepString = CepJsonAdapter().toJson(expectedCep)
        val actualCep = CepJsonAdapter().fromJson(expectedCepString)
        assertEquals(expectedCepString, actualCepString)
        assertEquals(expectedCep, actualCep)
    }

    @Test
    fun `toJson should return valid cep from Cep `(){
        val expectedCepString = "12345-678"
        val cep = Cep.build("12345-678")

        val actualCepString = CepJsonAdapter().toJson(cep)

        assertThat(actualCepString).isEqualTo(expectedCepString)
    }

    @Test
    fun `fromJson should return valid cep from String`(){
        val cepString = "12345-678"
        val expectedCep = Cep.build("12345-678")

        val actualCep = CepJsonAdapter().fromJson(cepString)

        assertThat(actualCep).isEqualTo(expectedCep)
    }

    @Test
    fun `fromJson should throws CepAdapterException when input is not a valid cep`() {
        val invalidCep = listOf(
            "123456-789",
            "12345-67",
            "a12345678",
            "12345-67a",
            "@123#-%f1",
            ""
        )

        invalidCep.forEach { input ->
            assertThrows(InputDoesNotMatchCepPatternException::class.java) {
                CepJsonAdapter().fromJson(input)
            }
        }
    }
}