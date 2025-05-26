package br.com.arml.cep.model

import br.com.arml.cep.model.entity.AddressDTO
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AddressDTOTest {

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
        assertEquals(mockAddressDTO.cep, address.zipCode)
        assertEquals(mockAddressDTO.logradouro, address.street)
        assertEquals(mockAddressDTO.complemento, address.complement)
        assertEquals(mockAddressDTO.bairro, address.district)
        assertEquals(mockAddressDTO.localidade, address.city)
        assertEquals(mockAddressDTO.estado, address.state)
        assertEquals(mockAddressDTO.regiao, address.region)
        assertEquals(mockAddressDTO.uf, address.uf)
        assertEquals(mockAddressDTO.ddd, address.ddd)
    }

    @Test
    fun `should handle null values in AddressDTO`() {
        val mockAddressDTO = AddressDTO()
        val address = mockAddressDTO.toAddress()
        assertEquals("", address.zipCode)
        assertEquals("", address.street)
        assertEquals("", address.complement)
        assertEquals("", address.district)
        assertEquals("", address.city)
        assertEquals("", address.state)
        assertEquals("", address.region)
        assertEquals("", address.uf)
        assertEquals("", address.ddd)
    }

}