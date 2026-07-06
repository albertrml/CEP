package br.com.arml.cep.model.converter

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.exception.CepException
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class CepConverterTest {
    @Test
    fun `fromCepToString should return 8 digits when Cep is valid`(){
        val cep = Cep.build("12345-678")
        val expectedCepString = "12345-678"

        val actualCepString = CepConverter.fromCepToString(cep)

        assertThat(actualCepString).isEqualTo(expectedCepString)
    }

    @Test
    fun `fromCepToString should return null when Cep is null`(){
        val actual = CepConverter.fromCepToString(null)

        assertThat(actual).isNull()
    }

    @Test
    fun `fromStringToCep should return Cep when String is valid`(){
        val cepString = "12345-678"
        val expectedCep = Cep.build("12345-678")

        val actualCep = CepConverter.fromStringToCep(cepString)

        assertThat(actualCep).isEqualTo(expectedCep)
    }

    @Test
    fun `fromStringToCep should return null when String is null`(){
        val actual = CepConverter.fromStringToCep(null)

        assertThat(actual).isNull()
    }

    @Test
    fun `fromStringToCep should throws EmptyCepException when String is empty`(){
        assertThrows(CepException.EmptyCepException::class.java) {
            CepConverter.fromStringToCep("")
        }
    }

    @Test
    fun `fromStringToCep should throws SizeCepException when String has not 9 symbols`(){
        val invalidCep = listOf("12345-67","12345-6789")

        invalidCep.forEach { input ->
            assertThrows(CepException.SizeCepException::class.java) {
                CepConverter.fromStringToCep(input)
            }
        }
    }

    @Test
    fun `fromStringToCep should throws IllegalPatternException when String does not match the pattern`(){
        val invalidCep = listOf(
            "-12345678",
            "1-2345678",
            "12-345678",
            "123-45678",
            "1234-5678",
            "123456-78",
            "1234567-8",
            "12345678-",
            "123456789",
            "a123$-123",
            "12345-67a",
            "@123#-%f1",
            "123-45678"
        )

        invalidCep.forEach { input ->
            println(input)
            assertThrows(CepException.IllegalPatternException::class.java) {
                CepConverter.fromStringToCep(input)
            }
        }
    }
}