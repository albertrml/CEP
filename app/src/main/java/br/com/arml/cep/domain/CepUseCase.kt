package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.repository.PlaceRepository
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.domain.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CepUseCase @Inject constructor(
    private val repository: PlaceRepository
){
    /*
    *  CepViewModel: fetchEntry
    *  HistoryViewModel:
    *       - fetchAllEntries
    *       - delete: EntryIsNotFavorite,and AllNotFavorite
    *       - filters: ByCep, ByInitialTimestamp, ByEndTimestamp, ByTimestamp
    *       - update: MarkEntryAsFavorite
    *   FavoriteViewModel:
    *       - fetchFavoriteEntries
    *       - deleteEntry
    *       - updateEntry
    *       - filterByCepAndFavorite
    */

    fun insertEntry(entry: PlaceEntry) = repository.insertPlace(entry)
    fun fetchEntry(code: String): Flow<Response<PlaceEntry>>{
        return try {
            val cep = Cep.build(code)
            repository.getPlace(cep)
        } catch (e: CepException) {
            flowOf(Response.Failure(e))
        }
    }
    fun fetchAllEntries() = repository.getAllPlaces()
    fun deleteEntry(entry: PlaceEntry) = repository.deletePlace(entry)
    fun deleteAllNotFavorite() = repository.deleteAllNotFavoritePlaces()
    fun filterByCep(query: String) = repository.filterPlacesByCep(query)
    fun fetchFavoriteEntries() = repository.getFavoritePlaces()
    fun updateEntry(entry: PlaceEntry) = repository.updatePlace(entry)
    fun filterByCepAndFavorite(query: String) = repository.filterPlacesByCepAndFavorite(query)
}