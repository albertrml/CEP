package br.com.arml.cep.model.domain

import br.com.arml.cep.model.dto.AddressDTO
import junit.framework.TestCase
import org.junit.Test

class AddressTest {
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

    @Test
    fun `should convert AddressDTO to Address`() {
        val address = mockAddressDTO.toAddress()
        TestCase.assertEquals(mockAddressDTO.cep, address.zipCode)
        TestCase.assertEquals(mockAddressDTO.logradouro, address.street)
        TestCase.assertEquals(mockAddressDTO.complemento, address.complement)
        TestCase.assertEquals(mockAddressDTO.bairro, address.district)
        TestCase.assertEquals(mockAddressDTO.localidade, address.city)
        TestCase.assertEquals(mockAddressDTO.estado, address.state)
        TestCase.assertEquals(mockAddressDTO.regiao, address.region)
        TestCase.assertEquals(mockAddressDTO.uf, address.uf)
        TestCase.assertEquals(mockAddressDTO.ddd, address.ddd)
    }

    @Test
    fun `should handle null values in AddressDTO`() {
        val mockAddressDTO = AddressDTO()
        val address = mockAddressDTO.toAddress()
        TestCase.assertEquals("", address.zipCode)
        TestCase.assertEquals("", address.street)
        TestCase.assertEquals("", address.complement)
        TestCase.assertEquals("", address.district)
        TestCase.assertEquals("", address.city)
        TestCase.assertEquals("", address.state)
        TestCase.assertEquals("", address.region)
        TestCase.assertEquals("", address.uf)
        TestCase.assertEquals("", address.ddd)
    }
}