package br.com.arml.cep.model.converter

import androidx.room.TypeConverter
import br.com.arml.cep.model.domain.Favorite

object FavoriteConverter {
    @TypeConverter
    fun fromFavoriteToBoolean(favorite: Favorite?): Boolean? {
        return favorite?.value
    }

    @TypeConverter
    fun fromBooleanToFavorite(isFavorite: Boolean?): Favorite? {
        return isFavorite?.let { Favorite(it) }
    }

}