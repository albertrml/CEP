package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.mapSuccess
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.source.local.CacheDao
import jakarta.inject.Inject

class CacheRepository @Inject constructor(
    private val cacheDao: CacheDao
) {
    // Create
    fun insertPlace(place: Place) = asResponse { cacheDao.insert(place.toEntity()) }

    // Read
    fun getPlacesByZipcode(query: String) = cacheDao
        .getByZipcode(query)
        .toResponseFlow()
        .mapSuccess { entity -> entity.map { it.toModel() }  }

    // Update
    fun updatePlace(place: Place) = asResponse { cacheDao.update(place.toEntity()) }

    // Delete
    fun deletePlace(place: Place) = asResponse {
        cacheDao.deleteIfUnfavorite(place.cep.text)
    }

    fun deleteAllUnwanted() = asResponse { cacheDao.deleteAllUnwanted() }
}