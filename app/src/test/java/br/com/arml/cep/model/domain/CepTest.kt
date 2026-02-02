package br.com.arml.cep.model.domain

import br.com.arml.cep.model.exception.CepException
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class CepTest {
    private fun generateInvalidCep(hyphenPosition: Int): String {
        val prefix = "$hyphenPosition".repeat(hyphenPosition)
        val suffix = "$hyphenPosition".repeat(8 - hyphenPosition)
        return "$prefix-$suffix"
    }

    private fun assertCep(assertContent: (String) -> Unit){
        List(8){ it }.forEach {
            val input = generateInvalidCep(it)
            if (it != 5){ assertContent(input) }
        }
    }

    @Test
    fun `isValid should return true when input is valid`() {
        val input = "12345-678"

        val actual = Cep.isValid(input)

        assertThat(actual).isTrue()
    }

    @Test
    fun `isValid should return false when input is invalid`() {
        assertCep { assertThat(Cep.isValid(it)).isFalse() }

        val invalidInputs = listOf(
            "123456-789",
            "12345-67",
            "a12345678",
            "12345-67a",
            "@123#-%f1"
        )

        invalidInputs.forEach { input -> assertThat(Cep.isValid(input)).isFalse() }
    }

    @Test
    fun `Cep should build Cep from a valid input`() {
        val input = "12345-678"

        try {
            Cep.build(input)
        } catch (e: CepException) {
            Assert.fail("Cep.build() should not throw CepException, but $e")
        }
    }

    @Test
    fun `Cep should throw EmptyCepException when cep is empty`() {
        assertThrows(CepException.EmptyCepException::class.java) {
            Cep.build("")
        }
    }

    @Test
    fun `Cep should throw IllegalPatternException when cep contains non-valid format`(){
        assertCep {
            assertThrows(CepException.IllegalPatternException::class.java) {
                Cep.build(it)
            }
        }
    }

    @Test
    fun `Cep should throw SizeCepException when cep has less than 9 symbols`(){
        assertThrows(CepException.SizeCepException::class.java) {
            Cep.build("67133-11")
        }
    }

    @Test
    fun `Cep should throw SizeCepException when cep has more than 9 symbols`(){
        assertThrows(CepException.SizeCepException::class.java) {
            Cep.build("67133-1151")
        }
    }

    @Test
    fun `formattedCep should returns XXXXX-XXX pattern when cep has 8 digits`(){
        val cep = formattedCep("67133115")
        TestCase.assertEquals("67133-115", cep)
    }

    @Test
    fun `formattedCep should format filter first 8 only digits and returns XXXXX-XXX pattern`(){
        val inputs = listOf(
            "123--as45678",
            "12345678901234567890"
        )

        inputs.forEach { input ->
            val actual = formattedCep(input)
            assertThat(Cep.isValid(actual)).isTrue()
        }
    }

    @Test
    fun `unFormat should return 8 digis when input is a valid cep`(){
        val expected = "11111111"
        val cep = Cep.build("11111-111")

        val actual = unformatCep(cep.text)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `unFormat should filter only 8 digits from input`(){
        val expected = "12345678"
        val inputs = listOf("12345678","1234567890-1234567890")


        inputs.forEach { input ->
            val actual = unformatCep(input)
            assertThat(actual).isEqualTo(expected)
        }
    }


    @Test
    fun `updateCepField should return n-1 digits when newValue is lesser than oldValue but n is not 5`(){
        val oldValues = listOf("12345-678", "12345-67", "12345-6","1234","123","12","1")
        val newValues = listOf("12345-67", "12345-6", "12345-","123","12","1","")
        val expectedValues = listOf("1234567", "123456", "12345", "123", "12", "1", "")

        oldValues.forEachIndexed { index, oldValue ->
            val actual = updateCepField(oldValue, newValues[index])
            assertThat(actual).isEqualTo(expectedValues[index])
        }
    }

    @Test
    fun `should return n+1 digits when newValue is greater than oldValue`(){
        val oldValues = listOf("", "1", "12","123","1234","12345-","12345-6","12345-67")
        val newValues = listOf("1", "12", "123","1234","12345","12345-6","12345-67","12345-678")
        val expectedValues = listOf("1", "12", "123","1234","12345","123456","1234567","12345678")

        oldValues.forEachIndexed { index, oldValue ->
            val actual = updateCepField(oldValue, newValues[index])
            assertThat(actual).isEqualTo(expectedValues[index])
        }
    }

    @Test
    fun `should return 4 digits when newValue has 5 digits and oldValue has - as last character`(){
        val oldValue = "12345-"
        val newValue = "12345"
        val expectedValue = "1234"

        val actual = updateCepField(oldValue, newValue)

        assertThat(actual).isEqualTo(expectedValue)
    }
}