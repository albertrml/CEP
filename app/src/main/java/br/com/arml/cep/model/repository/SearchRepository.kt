package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.toPlaceWithNotes
import br.com.arml.cep.model.entity.LogEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
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
        val resultDB = cacheDao.selectPlaceWithNotesByZipcode(cep.text)
        var gotFromAPI = false
        val (placeEntity, noteEntities) = resultDB ?: run {
            gotFromAPI = true
            getPlaceFromApi(cep)
        }
        val notes = noteEntities.map { it.toModel() }

        if (gotFromAPI) cacheDao.insertPlaceEntity(placeEntity)
        logAccess(cep)

        placeEntity.toModel(notes)
    }

    private suspend fun getPlaceFromApi(cep: Cep): PlaceWithNotes {
        val address = searchService.getAddressByCep(cep.text)
        if (address.erro == "true") throw CepException.NotFoundCepException()
        val placeWithNotes = Place(
            cep = cep,
            address = address.toAddress()
        ).toPlaceWithNotes()
        return placeWithNotes
    }

    private suspend fun logAccess(cep: Cep){
        val logEntity = LogEntity(
            zipcodePlace = cep.text,
            timestamp = System.currentTimeMillis()
        )
        logDao.insertLogEntity(logEntity)
    }
}