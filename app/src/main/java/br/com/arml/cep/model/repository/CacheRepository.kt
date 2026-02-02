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
    /** Create **/
    fun insertPlace(place: Place) = asResponse { cacheDao.insertPlaceEntity(place.toEntity()) }

    /** Read **/
    fun getPlacesByZipcode(query: String) = cacheDao
        .selectCachedPlaceEntitiesByZipcode(query)
        .toResponseFlow()
        .mapSuccess { entity -> entity.map { it.toModel() }  }

    /** Update **/
    fun updatePlace(place: Place) = asResponse { cacheDao.updatePlaceEntity(place.toEntity()) }

    /** Delete **/
    fun deletePlace(place: Place) = asResponse {
        cacheDao.deleteCachedPlaceEntity(place.cep.text)
    }
    fun deleteAllUnwanted() = asResponse { cacheDao.deleteAllCachedPlaceEntities() }
}