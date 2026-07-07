package br.com.arml.cep.domain

import androidx.compose.ui.util.fastFilterNotNull
import br.com.arml.cep.model.adapter.toJson
import br.com.arml.cep.model.adapter.toPlaceList
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.core.response.mapSuccess
import br.com.arml.cep.model.qualifier.BackupMoshi
import br.com.arml.cep.model.repository.FavoriteRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    @param:BackupMoshi private val moshi: Moshi,
    private val favoriteRepository: FavoriteRepository
) {
    /** CREATE **/
    fun addNoteToFavorite(cep: Cep, note: Note) = favoriteRepository
        .addToFavorite(cep.text, note)

    /** READ **/
    fun fetchFavorites() = favoriteRepository.getFavoritesByTitle("")

    fun filterByCep(query: String) = favoriteRepository.getFavoritesByZipcode(query)

    fun filterByTitle(query: String) = favoriteRepository.getFavoritesByTitle(query)

    /** UPDATE **/
    fun updateNote(note: Note) = favoriteRepository
        .updateNoteFromFavorite(note)

    /** DELETE **/
    fun removeFromFavorite(place: Place) = favoriteRepository
        .deleteFromFavorite(place.cep.text)

    fun deleteNote(cep: Cep, note: Note) = favoriteRepository
        .deleteNoteFromFavorite(cep.text, note)

    /** EXPORT **/
    fun exportFavorites() = favoriteRepository
        .exportFavorites()
        .mapSuccess { places -> places.fastFilterNotNull().toJson(moshi) }


    /** IMPORT **/
    fun importFavorites(jsonBackup: String): Flow<Response<Unit>> = try {
        val favorites = jsonBackup.toPlaceList(moshi)
        favoriteRepository.importFavorites(favorites)
    } catch (exception: Exception) {
        flowOf(Response.Failure(exception))
    }

}