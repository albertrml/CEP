package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Place
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

fun List<Place>.toJson(moshi: Moshi): String {
    val placeListType = Types.newParameterizedType(List::class.java, Place::class.java)
    val placeListAdapter: JsonAdapter<List<Place>> = moshi.adapter(placeListType)
    val json = placeListAdapter.toJson(this) ?: ""
    return json
}

fun String.toPlaceList(moshi: Moshi): List<Place> {
    val placeListType = Types.newParameterizedType(List::class.java, Place::class.java)
    val placeListAdapter: JsonAdapter<List<Place>> = moshi.adapter(placeListType)
    val places: List<Place> = placeListAdapter.fromJson(this) ?: emptyList()
    return places
}

