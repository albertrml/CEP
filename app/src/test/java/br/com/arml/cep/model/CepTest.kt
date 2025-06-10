package br.com.arml.cep.model

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.updateCepField
import br.com.arml.cep.model.exception.CepException
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CepTest {

    @Test
    fun `should throw EmptyCepException when cep is empty`() {
        assertThrows(CepException.EmptyCepException::class.java){
            Cep.build("")
        }
    }

    @Test
    fun `should throw InputCepException when cep contains non-digit characters`(){
        assertThrows(CepException.InputCepException::class.java){
            Cep.build("6713311a")
        }
    }

    @Test
    fun `should throw SizeCepException when cep has less than 8 digits`(){
        assertThrows(CepException.SizeCepException::class.java){
            Cep.build("6713311")
        }
    }

    @Test
    fun `should throw SizeCepException when cep has more than 8 digits`(){
        assertThrows(CepException.SizeCepException::class.java){
            Cep.build("671331151")
        }
    }

    @Test
    fun `should format be XXXXX-XXX when cep has 8 digits`(){
        val cep = Cep.format("67133115")
        assertEquals("67133-115",cep)
    }

    @Test
    fun `should format filter any only digits`(){
        val cep = Cep.format("671--as33115")
        assertEquals("67133-115",cep)
    }

    @Test
    fun `should format filter only 8 digits`(){
        val cep = Cep.format("67133115890097668")
        assert(cep == "67133-115")
    }

    @Test
    fun `should unFormat be XXXXX-XXX when cep has 8 digits`(){
        val cep = Cep.unFormat("67133-115")
        assertEquals("67133115",cep)
    }

    @Test
    fun `should unFormat filter any only digits`(){
        val cep = Cep.unFormat("671--as33115")
        assertEquals("67133115",cep)
    }

    @Test
    fun `should unFormat filter only first 8 digits`(){
        val cep = Cep.unFormat("67133115890097668")
        assertEquals("67133115",cep)

    }

    @Test
    fun `should return n-1 digits when newValue is lesser than oldValue but n is not 5`(){
        val oldValues = listOf("12345-678", "12345-67", "12345-6","1234","123","12","1")
        val newValues = listOf("12345-67", "12345-6", "12345-","123","12","1","")
        val expectedValues = listOf("1234567", "123456", "12345", "123", "12", "1", "")
        oldValues.forEachIndexed { index, oldValue ->
            assertEquals(expectedValues[index],updateCepField(oldValue, newValues[index]))
        }
    }

    @Test
    fun `should return n+1 digits when newValue is greater than oldValue`(){
        val oldValues = listOf("", "1", "12","123","1234","12345-","12345-6","12345-67")
        val newValues = listOf("1", "12", "123","1234","12345","12345-6","12345-67","12345-678")
        val expectedValues = listOf("1", "12", "123","1234","12345","123456","1234567","12345678")
        oldValues.forEachIndexed { index, oldValue ->
            assertEquals(expectedValues[index],updateCepField(oldValue, newValues[index]))
        }
    }

    @Test
    fun `should return 4 digits when newValue has 5 digits and oldValue has - as last character`(){
        val oldValue = "12345-"
        val newValue = "12345"
        val expectedValue = "1234"
        assertEquals(expectedValue,updateCepField(oldValue, newValue))
    }
}