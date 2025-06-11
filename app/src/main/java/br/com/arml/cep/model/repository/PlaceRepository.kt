package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.source.local.LogDao
import br.com.arml.cep.model.source.local.PlaceDao
import br.com.arml.cep.model.source.remote.CepApiService
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val service: CepApiService,
    private val placeDao: PlaceDao,
    private val logDao: LogDao
){
    fun getAddressByCep(cep: Cep): Flow<Response<Address>> = asResponse {
        val zipCode = cep.text
        val cepDAO = service.getAddressByCep(zipCode)
        if (cepDAO.erro == "true") throw CepException.NotFoundCepException()
        cepDAO.toAddress()
    }

    fun getPlace(cep: Cep): Flow<Response<PlaceEntry>> = asResponse {
        /* get the entry from the database */
        val zipCode = cep.text
        val entryDB = placeDao.read(zipCode)

        /* if the entry is not in the database, get from api and save in the database */
        val entry = entryDB?:run {
            val address = service.getAddressByCep(zipCode)
            if (address.erro == "true") throw CepException.NotFoundCepException()
            val entryPlaceAPI = PlaceEntry(
                cep = cep,
                address = address.toAddress()
            )
            placeDao.create(entryPlaceAPI)
            entryPlaceAPI
        }

        val entryLog = LogEntry(
            cep = cep,
            timestamp = Timestamp(System.currentTimeMillis())
        )
        logDao.create(entryLog)

        /* return the entry from the database */
        entry
    }

    fun getAllPlaces() = placeDao.readAll()

    fun getFavoritePlaces() = placeDao.readFavorites()

    fun updatePlace(entry: PlaceEntry) = asResponse { placeDao.update(entry) }

    fun deletePlace(entry: PlaceEntry) = asResponse { placeDao.delete(entry) }

    fun deleteAllNotFavoritePlaces() = asResponse { placeDao.deleteAllNotFavorite() }

    fun filterPlacesByCep(query: String) = placeDao.filterByCep(query)

    fun filterPlacesByCepAndFavorite(query: String) = placeDao.filterByCepAndFavorite(query)

}

