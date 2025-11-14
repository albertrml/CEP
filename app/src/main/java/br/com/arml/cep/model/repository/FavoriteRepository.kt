package br.com.arml.cep.model.repository

import androidx.compose.ui.util.fastFilterNotNull
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.mapSuccess
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.exception.BackupException
import br.com.arml.cep.model.exception.CepDatabaseException
import br.com.arml.cep.model.source.local.FavoriteDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    /** Create **/
    fun addToFavorite(zipcode: String, note: NoteEntity) = asResponse {
        favoriteDao.createFavorite(zipcode, note)
    }

    /** Read **/
    /*fun getAFavoriteWithNotes(query: String) = asResponse {
        val result = favoriteDao
            .readAFavoriteWithNotes(query)
            ?: throw CepDatabaseException.FavoriteNotFound()
        result
    }*/

    fun getFavoritesByTitle(title: String = "") = favoriteDao
        .readFavoritesByTitle(title)
        .map{ map ->
            map.map { (place, notes) ->
                PlaceWithNotes(place, notes)
            }
        }
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    fun getFavoritesByZipcode(zipcode: String) = favoriteDao
        .readFavoritesByZipcode(zipcode)
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    /** Update **/
    fun updateNoteFromFavorite(note: NoteEntity) = asResponse {
        favoriteDao.updateNote(note)
    }

    /** Delete **/
    fun deleteFromFavorite(zipcode: String) = asResponse {
        favoriteDao.deleteFromFavorite(zipcode)
    }

    fun deleteNoteFromFavorite(zipcode: String, note: NoteEntity) = asResponse {
        val count = favoriteDao.countNotesFromFavorite(zipcode = zipcode)
        if (count <= 1) throw CepDatabaseException.IllegalNoteQuantity()
        favoriteDao.deleteNote(note)
    }

    /** Export **/
    fun exportFavorites() = favoriteDao
        .exportFavorites()
        .toResponseFlow()
        .mapSuccess { entities ->
            entities.map { placeWithNotes ->
                val notes = placeWithNotes.notes.map { it.toModel() }
                placeWithNotes.place.toModel(notes = notes)
            }
        }

    /** Import **/
    fun importFavorites(favorites: List<Place>) = asResponse {
        with(favoriteDao) {
            val entries = favorites.map { place ->
                PlaceWithNotes(
                    place = place.toEntity(),
                    notes = place.notes.map { note -> note.toEntity() }
                )
            }.fastFilterNotNull()

            if (entries.isEmpty()) throw BackupException.ImportEmptyFavoriteException()

            entries.forEach { entry ->
                if (!doesPlaceExist(entry.place.zipcode))
                    insertPlace(entry.place)

                val zipcode = entry.place.zipcode

                entry.notes.forEach { note ->
                    if(!doesNoteExist(zipcode, note.title, note.content)){
                        createFavorite(zipcode, note)
                    }
                }
            }
        }
    }
}