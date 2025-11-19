package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.source.local.CacheDao
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchRepository @Inject constructor(
    private val searchService: PlaceRemoteDataSource,
    private val cacheDao: CacheDao,
    private val logDao: LogDao
) {
    fun getPlace(cep: Cep): Flow<Response<Place>> = asResponse {
        /* get the entry from the database */
        val zipCode = cep.text
        val entryDB = cacheDao.selectCachedPlaceEntityByZipcode(zipCode)

        /* if the entry is not in the database, get from api and save in the database */
        val entry = entryDB?:run {
            val address = searchService.getAddressByCep(zipCode)
            if (address.erro == "true") throw CepException.NotFoundCepException()
            val placeEntity = Place(
                cep = cep,
                address = address.toAddress()
            ).toEntity()
            cacheDao.insertPlaceEntity(placeEntity)
            placeEntity
        }

        val logEntity = LogEntity(
            zipcodePlace = cep.text,
            timestamp = System.currentTimeMillis()
        )
        logDao.insertLogEntity(logEntity)

        /* return the entry from the database */
        entry.toModel()
    }
}