package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.exception.tryConnectionViaCep
import br.com.arml.cep.model.exception.tryReadCepDatabase
import br.com.arml.cep.model.exception.tryWriteCepDatabase
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import br.com.arml.cep.model.utils.normalizeForAPISearch
import br.com.arml.cep.model.utils.normalizeForDBSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchService: PlaceRemoteDataSource,
    private val cacheDao: CacheDao,
    private val logDao: LogDao
) {
    fun getPlace(cep: Cep): Flow<Response<Place>> = asResponse {
        try {
            updateCache(cep)
        } catch (e: Exception) {
            val exists = tryReadCepDatabase { cacheDao.isPlaceExist(cep.text) }
            if (!exists) throw e
        }

        val resultDB = tryReadCepDatabase { cacheDao.selectPlaceWithNotesByZipcode(cep.text) }
            ?: throw CepException.NotFoundCepException()

        logAccess(cep)
        resultDB.toModel()
    }

    fun getPlaces(uf: String, city: String, street: String): Flow<Response<List<Place>>> = cacheDao
        .selectPlacesWithNotes(
            uf,
            city.normalizeForDBSearch(),
            street.normalizeForDBSearch()
        )
        .map { entities -> entities.map { it.toModel() } }
        .onStart {
            val cepList = updateCache(
                uf.normalizeForAPISearch(),
                city.normalizeForAPISearch(),
                street.normalizeForAPISearch()
            )
            cepList.forEach { cep -> logAccess(cep) }
        }
        .toResponseFlow()


    suspend fun logAccess(cep: Cep) {
        tryWriteCepDatabase {
            val logEntity = LogEntity(
                zipcodePlace = cep.text,
                timestamp = System.currentTimeMillis()
            )
            logDao.insertLogEntity(logEntity)
        }
    }

    suspend fun updateCache(cep: Cep) {
        val exists = tryReadCepDatabase { cacheDao.isPlaceExist(cep.text) }
        if (exists) return

        val addressDTO = tryConnectionViaCep { searchService.getAddressByCep(cep.text) }
        if (addressDTO.erro == "true") return

        tryWriteCepDatabase { cacheDao.insertPlaceEntity(addressDTO.toPlaceEntity()) }
    }

    suspend fun updateCache(uf: String, city: String, street: String): List<Cep> {
        val addressesFromAPI = tryConnectionViaCep {
            searchService.getAddresses(
                uf,
                city,
                street
            )
        }
        if (addressesFromAPI.isEmpty()) return emptyList()

        val placeEntities = addressesFromAPI.map { it.toPlaceEntity() }
        placeEntities.forEach { placeEntity ->
            val exists = tryReadCepDatabase { cacheDao.isPlaceExist(placeEntity.zipcode) }
            if (!exists) tryWriteCepDatabase { cacheDao.insertPlaceEntity(placeEntity) }
        }

        return placeEntities.map { Cep.build(it.zipcode) }
    }
}