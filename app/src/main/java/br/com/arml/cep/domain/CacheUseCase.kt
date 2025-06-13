package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.repository.PlaceRepository
import javax.inject.Inject

class CacheUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    fun deleteAll() = repository.deleteAllUnwantedPlaces()
    fun deleteEntry(entry: PlaceEntry) = repository.deletePlace(entry)

    fun fetchCache() = repository
        .getUnwantedPlaces()
        .toResponseFlow()

    fun filterByCep(query: String) = repository
        .getUnwantedPlacesByCepAndUnwanted(query)
        .toResponseFlow()

    fun updateEntry(entry: PlaceEntry) = repository.updatePlace(entry)
}