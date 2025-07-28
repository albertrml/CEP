package br.com.arml.cep.domain

import br.com.arml.cep.model.adapter.toJson
import br.com.arml.cep.model.adapter.toPlaceList
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.qualifier.BackupMoshi
import br.com.arml.cep.model.repository.PlaceRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: PlaceRepository,
    @param:BackupMoshi private val moshi: Moshi
) {
    fun fetchFavorites() = favoriteRepository.getFavoritePlaces().toResponseFlow()
    fun update(entry: PlaceEntry) = favoriteRepository.updatePlace(entry)
    fun filterByCep(query: String) = favoriteRepository.filterPlacesByCepAndFavorite(query).toResponseFlow()
    fun filterByTitle(query: String) = favoriteRepository.getFavoritePlaces().map{ response ->
        response.filter {  placeEntry -> placeEntry.note!!.title.contains(query) }
    }.toResponseFlow()
    fun exportFavorite() = favoriteRepository.getFavoritePlaces().map{ favoritesPlaces ->
        favoritesPlaces.toJson(moshi)
    }.toResponseFlow()

    fun importFavorite(json: String) = favoriteRepository
        .importFavoritePlaces(json.toPlaceList(moshi))
}