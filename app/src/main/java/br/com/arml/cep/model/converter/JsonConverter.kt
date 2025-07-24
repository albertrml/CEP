package br.com.arml.cep.model.converter

import br.com.arml.cep.model.entity.PlaceEntry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

fun List<PlaceEntry>.toJson(moshi: Moshi): String {
    val placeListType = Types.newParameterizedType(List::class.java, PlaceEntry::class.java)
    val placeListAdapter: JsonAdapter<List<PlaceEntry>> = moshi.adapter(placeListType)
    val json = placeListAdapter.toJson(this) ?: ""
    return json
}

fun String.toPlaceList(moshi: Moshi): List<PlaceEntry> {
    val placeListType = Types.newParameterizedType(List::class.java, PlaceEntry::class.java)
    val placeListAdapter: JsonAdapter<List<PlaceEntry>> = moshi.adapter(placeListType)
    val places: List<PlaceEntry> = placeListAdapter.fromJson(this) ?: emptyList()
    return places
}

