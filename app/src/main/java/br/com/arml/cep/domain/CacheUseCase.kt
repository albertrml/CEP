package br.com.arml.cep.domain

import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.repository.CacheRepository
import br.com.arml.cep.model.repository.FavoriteRepository
import javax.inject.Inject

class CacheUseCase @Inject constructor(
    private val cacheRepository: CacheRepository,
    private val favoriteRepository: FavoriteRepository
) {
    fun addToFavorite(place: Place) = with(place.cep.text) {
        favoriteRepository
            .addToFavorite(
                zipcode = this,
                note = NoteEntity(title = this, content = "")
            )
    }

    fun clearCache() = cacheRepository.deleteAllUnwanted()

    fun removePlaceFromCache(place: Place) = cacheRepository.deletePlace(place)

    fun findCachedPlacesByCep(query: String = "") = cacheRepository
        .getPlacesByZipcode(query = query)
}