package br.com.arml.cep.model.converter

import br.com.arml.cep.model.domain.Favorite
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteConverterTest {

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

}