package br.com.arml.cep.model.repository

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.source.local.LogLocalDataSource
import br.com.arml.cep.model.source.local.PlaceLocalDataSource
import br.com.arml.cep.model.source.remote.PlaceRemoteDataSource
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource,
    private val placeLocalDataSource: PlaceLocalDataSource,
    private val logLocalDataSource: LogLocalDataSource
){
    fun deleteAllUnwantedPlaces() = asResponse { placeLocalDataSource.deleteAllNotFavorite() }
    fun deletePlace(entry: PlaceEntry) = asResponse { placeLocalDataSource.delete(entry) }
    fun filterPlacesByCep(query: String) = placeLocalDataSource.filterByCep(query)
    fun filterPlacesByCepAndFavorite(query: String) = placeLocalDataSource.filterByCepAndFavorite(query)
    fun getAddressByCep(cep: Cep): Flow<Response<Address>> = asResponse {
        val zipCode = cep.text
        val cepDAO = placeRemoteDataSource.getAddressByCep(zipCode)
        if (cepDAO.erro == "true") throw CepException.NotFoundCepException()
        cepDAO.toAddress()
    }
    fun getFavoritePlaces() = placeLocalDataSource.readFavorites()
    fun getPlace(cep: Cep): Flow<Response<PlaceEntry>> = asResponse {
        /* get the entry from the database */
        val zipCode = cep.text
        val entryDB = placeLocalDataSource.read(zipCode)

        /* if the entry is not in the database, get from api and save in the database */
        val entry = entryDB?:run {
            val address = placeRemoteDataSource.getAddressByCep(zipCode)
            if (address.erro == "true") throw CepException.NotFoundCepException()
            val entryPlaceAPI = PlaceEntry(
                cep = cep,
                address = address.toAddress()
            )
            placeLocalDataSource.create(entryPlaceAPI)
            entryPlaceAPI
        }

        val entryLog = LogEntry(
            cep = cep,
            timestamp = Timestamp(System.currentTimeMillis())
        )
        logLocalDataSource.create(entryLog)

        /* return the entry from the database */
        entry
    }
    fun getUnwantedPlaces() = placeLocalDataSource.readUnwanted()
    fun getUnwantedPlacesByCepAndUnwanted(query: String) = placeLocalDataSource.filterByCepAndUnwanted(query)
    fun importFavoritePlaces(places: List<PlaceEntry>) = asResponse {
        places.forEach {
            if (placeLocalDataSource.read(it.cep.text) != null)
                placeLocalDataSource.update(it)
            else
                placeLocalDataSource.create(it)
        }
    }
    fun updatePlace(entry: PlaceEntry) = asResponse { placeLocalDataSource.update(entry) }
}

