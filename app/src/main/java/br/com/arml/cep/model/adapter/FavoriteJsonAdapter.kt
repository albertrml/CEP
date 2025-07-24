package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Favorite
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class FavoriteJsonAdapter {
    @ToJson
    fun toJson(favorite: Favorite): Boolean {
        return favorite.value
    }

    @FromJson
    fun fromJson(favorite: Boolean): Favorite {
        return Favorite(favorite)
    }

}