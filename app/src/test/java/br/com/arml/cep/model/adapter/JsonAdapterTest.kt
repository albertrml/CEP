package br.com.arml.cep.model.adapter

import br.com.arml.cep.di.AdapterModule
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import junit.framework.TestCase.assertEquals
import org.junit.Test

class JsonAdapterTest {
    val moshi = AdapterModule.provideMoshiToBackup()

    @Test
    fun `should convert PlaceList to Json and back`() {
        val expectedPlaceList = mockUnfavoritePlaces
        val actualPlaceListJson = expectedPlaceList.toJson(moshi)
        val actualPlaceList = actualPlaceListJson.toPlaceList(moshi)
        assertEquals(expectedPlaceList, actualPlaceList)
    }
}