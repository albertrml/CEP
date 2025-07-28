package br.com.arml.cep.model.converter

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Timestamp

class TypeConverterTest {
    @Test
    fun `should convert Cep to String and back`(){
        val expectedCep = Cep.build("12345678")
        val expectedCepString = "12345678"
        val actualCepString = CepConverter.fromCepToString(expectedCep)
        val actualCep = CepConverter.fromStringToCep(expectedCepString)
        val actualNullCep = CepConverter.fromStringToCep(null)
        val actualNullStringCep = CepConverter.fromStringToCep(null)
        assertEquals(expectedCepString, actualCepString)
        assertEquals(expectedCep, actualCep)
        assertEquals(null, actualNullCep)
        assertEquals(null, actualNullStringCep)
    }

    @Test
    fun `should convert Favorite to Boolean and back`(){
        val expectedFavorite = Favorite(true)
        val expectedBooleanFavorite = true
        val actualBooleanFavorite = FavoriteConverter.fromFavoriteToBoolean(expectedFavorite)
        val actualFavorite = FavoriteConverter.fromBooleanToFavorite(expectedBooleanFavorite)
        val actualNullFavorite = FavoriteConverter.fromBooleanToFavorite(null)
        val actualNullBooleanFavorite = FavoriteConverter.fromFavoriteToBoolean(null)
        assertEquals(expectedBooleanFavorite, actualBooleanFavorite)
        assertEquals(expectedFavorite, actualFavorite)
        assertEquals(null, actualNullFavorite)
        assertEquals(null, actualNullBooleanFavorite)
    }

    @Test
    fun `should convert Note to String and back`(){
        val expectedNote = Note.build("Title", "Content")
        val expectedNoteString = "Title||<NOTE_SEP>||Content"
        val actualNoteString = NoteConverter.fromNoteToString(expectedNote)
        val actualNote = NoteConverter.fromStringToNote(expectedNoteString)
        val actualNullNote = NoteConverter.fromStringToNote(null)
        val actualNullNoteString = NoteConverter.fromNoteToString(null)
        assertEquals(expectedNoteString, actualNoteString)
        assertEquals(expectedNote, actualNote)
        assertEquals(null, actualNullNote)
        assertEquals(null, actualNullNoteString)
    }

    @Test
    fun `should convert Timestamp to Long and back`(){
        val expectedTimestamp = Timestamp(System.currentTimeMillis())
        val expectedTimestampLong = expectedTimestamp.time
        val actualTimestampLong = TimestampConverter.fromTimestampToLong(expectedTimestamp)
        val actualTimestamp = TimestampConverter.fromLongToTimestamp(expectedTimestampLong)
        val actualNullTimestamp = TimestampConverter.fromLongToTimestamp(null)
        val actualNullTimestampLong = TimestampConverter.fromTimestampToLong(null)
        assertEquals(expectedTimestampLong, actualTimestampLong)
        assertEquals(expectedTimestamp, actualTimestamp)
        assertEquals(null, actualNullTimestamp)
        assertEquals(null, actualNullTimestampLong)
    }
}