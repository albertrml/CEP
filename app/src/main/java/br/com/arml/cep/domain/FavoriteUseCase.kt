package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.repository.PlaceRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: PlaceRepository
) {
    fun fetchFavorites() = favoriteRepository.getFavoritePlaces().toResponseFlow()
    fun update(entry: PlaceEntry) = favoriteRepository.updatePlace(entry)
    fun filterByCep(query: String) = favoriteRepository.filterPlacesByCepAndFavorite(query).toResponseFlow()
    fun filterByTitle(query: String) = favoriteRepository.getFavoritePlaces().map{
        it.filter {  it.note!!.title.contains(query) }
    }.toResponseFlow()
}