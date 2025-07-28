package br.com.arml.cep.model.adapter

import br.com.arml.cep.di.AdapterModule
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockPlaceEntries
import junit.framework.TestCase.assertEquals
import org.junit.Test

class JsonAdapterTest {
    val moshi = AdapterModule.provideMoshiToBackup()

    @Test
    fun `should convert String to Cep and back`() {
        val expectedCepString = "12345678"
        val expectedCep = Cep.build(expectedCepString)
        val actualCepString = CepJsonAdapter().toJson(expectedCep)
        val actualCep = CepJsonAdapter().fromJson(expectedCepString)
        assertEquals(expectedCepString, actualCepString)
        assertEquals(expectedCep, actualCep)
    }

    @Test
    fun `should convert Boolean to Favorite and Back`() {
        val expectedBooleanFavorite = true
        val expectedFavorite = Favorite(expectedBooleanFavorite)
        val actualBooleanFavorite = FavoriteJsonAdapter()
            .toJson(Favorite(expectedBooleanFavorite))
        val actualFavorite = FavoriteJsonAdapter()
            .fromJson(expectedBooleanFavorite)
        assertEquals(expectedBooleanFavorite, actualBooleanFavorite)
        assertEquals(expectedFavorite, actualFavorite)
    }

    @Test
    fun `should convert Note to NoteJson and back`() {
        val expectedNote = Note.build("Title", "Content")
        val expectedNoteJson = NoteJson("Title", "Content")
        val actualNoteJson = NoteJsonAdapter().toJson(expectedNote)
        val actualNote = NoteJsonAdapter().fromJson(expectedNoteJson)
        assertEquals(expectedNoteJson, actualNoteJson)
        assertEquals(expectedNote, actualNote)
    }

    @Test
    fun `should convert PlaceList to Json and back`() {
        val expectedPlaceList = mockPlaceEntries
        val actualPlaceListJson = expectedPlaceList.toJson(moshi)
        val actualPlaceList = actualPlaceListJson.toPlaceList(moshi)
        assertEquals(expectedPlaceList, actualPlaceList)
    }
}