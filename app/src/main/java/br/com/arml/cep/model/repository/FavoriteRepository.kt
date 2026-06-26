package br.com.arml.cep.model.repository

import androidx.compose.ui.util.fastFilterNotNull
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.asResponse
import br.com.arml.cep.model.domain.mapSuccess
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.domain.toResponseFlow
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import br.com.arml.cep.model.entity.relation.toModel
import br.com.arml.cep.model.entity.toModel
import br.com.arml.cep.model.exception.BackupException
import br.com.arml.cep.model.exception.CepDatabaseException
import br.com.arml.cep.model.source.local.FavoriteDao
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    /** Create **/
    fun addToFavorite(zipcode: String, note: Note) = asResponse {
        with(favoriteDao){
            var quantity = countNotesEntitiesFromFavorite(zipcode) + 1
            var suggestedTitle = note.title
            while (doesTitleExist(suggestedTitle)){
                suggestedTitle = "${note.title} (${quantity++})"
            }

            val newNote = Note.build(
                id = note.id,
                title = suggestedTitle,
                content = note.content
            ).toEntity()
            insertNoteEntityToFavorite(zipcode, newNote)
        }
    }


    /** Read **/
    fun getFavoritesByTitle(title: String = "") = favoriteDao
        .selectFavoritesByTitle(title)
        .map{ map ->
            map.map { (place, notes) ->
                PlaceWithNotes(place, notes)
            }
        }
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    fun getFavoritesByZipcode(zipcode: String) = favoriteDao
        .selectFavoritesByZipcode(zipcode)
        .toResponseFlow()
        .mapSuccess { entities -> entities.map { it.toModel() } }

    /** Update **/
    fun updateNoteFromFavorite(note: Note) = asResponse {
        favoriteDao.updateNote(note.toEntity())
    }

    /** Delete **/
    fun deleteFromFavorite(zipcode: String) = asResponse {
        favoriteDao.deleteFromFavorite(zipcode)
    }

    fun deleteNoteFromFavorite(zipcode: String, note: Note) = asResponse {
        val count = favoriteDao.countNotesEntitiesFromFavorite(zipcode = zipcode)
        if (count <= 1) throw CepDatabaseException.IllegalNoteQuantity()
        favoriteDao.deleteNote(note.toEntity())
        zipcode
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
                if (!doesPlaceEntityExist(entry.place.zipcode)) insertPlaceEntity(entry.place)

                val zipcode = entry.place.zipcode

                entry.notes.forEach { note ->
                    if(!doesNoteEntityExist(zipcode, note.title, note.content)){
                        insertNoteEntityToFavorite(zipcode, note)
                    }
                }
            }
        }
    }
}