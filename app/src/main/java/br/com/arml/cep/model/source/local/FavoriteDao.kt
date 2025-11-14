package br.com.arml.cep.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.arml.cep.model.entity.FavoriteEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.relation.PlaceWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    /** Create **/
    // Insert a favorite linking a place with a note, but does note create them.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createFavoriteLink(favorite: FavoriteEntity)

    // Create a new entry into note table and return its id.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNote(note: NoteEntity): Long

    // Create a new entry into favorite place if a new entry note is created.
    @Transaction
    suspend fun createFavorite(zipcode: String, note: NoteEntity) {
        val idNote = createNote(note)
        createFavoriteLink(FavoriteEntity(zipcode, idNote))
    }

    // Create a new entry into place table. This method is only used as help function to import data.
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlace(place: PlaceEntity)

    /** Read **/
    // Count all notes in the favorite table with the given zipcode.
    @Transaction
    @Query("SELECT COUNT(*) FROM favorites WHERE zipcode_place = :zipcode")
    suspend fun countNotesFromFavorite(zipcode: String): Int

    // Search for a place with given zipcode.
    @Query("SELECT EXISTS (SELECT 1 FROM places WHERE zipcode = :zipcode)")
    suspend fun doesPlaceExist(zipcode: String): Boolean

    // Search for favorite places that have a note with the given title.
    @Query(
        value = """
            SELECT p.*, n.*
            FROM places p
            JOIN favorites f ON p.zipcode = f.zipcode_place
            JOIN notes n ON f.id_note = n.id
            WHERE n.title LIKE '%' || :query || '%'
        """
    )
    fun readFavoritesByTitle(query: String): Flow<Map<PlaceEntity, List<NoteEntity>>>

    // Search for a place with given zipcode and return its notes.
    @Transaction
    @Query("SELECT * FROM places WHERE zipcode = :zipcode")
    suspend fun readAFavoriteWithNotes(zipcode: String): PlaceWithNotes?

    // Search for all place registers that are favorite.
    @Query(
        value = """
            SELECT DISTINCT p.* FROM places p
            JOIN favorites f ON p.zipcode = f.zipcode_place
            WHERE p.zipcode LIKE '%' || :query || '%'
        """
    )
    fun readFavoritesByZipcode(query: String = ""): Flow<List<PlaceWithNotes>>

    @Query(
        value = """
            SELECT EXISTS (
                SELECT 1 FROM favorites f
                JOIN notes n ON f.id_note = n.id
                WHERE f.zipcode_place = :zipcode AND n.title = :title AND n.content = :content
            )
        """
    )
    suspend fun doesNoteExist(zipcode: String, title: String, content: String): Boolean

    /** Update **/
    // Update a note.
    @Update
    suspend fun updateNote(note: NoteEntity)

    /** Delete **/
    // All notes which are linked to the given zipcode in the favorite table are deleted. As
    // As functionality, this method remove a place with the given zipcode from the favorite list.
    @Transaction
    @Query(
        value = """
            DELETE FROM notes 
            WHERE id IN (
                SELECT id_note FROM favorites
                WHERE zipcode_place = :zipcode
            )
        """
    )
    suspend fun deleteFromFavorite(zipcode: String)

    // Delete a note.
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    /** Export **/
    // Export all notes from favorite places.
    @Transaction
    /*@Query("""
        SELECT DISTINCT p.* FROM places p
        JOIN favorites f ON p.zipcode = f.zipcode_place
    """)*/
    @Query("""
        SELECT * FROM places 
        WHERE zipcode IN (
            SELECT DISTINCT zipcode FROM favorites
        )
    """)
    fun exportFavorites(): Flow<List<PlaceWithNotes>>
}