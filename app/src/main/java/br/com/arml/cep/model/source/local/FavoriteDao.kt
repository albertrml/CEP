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
    suspend fun insertNoteEntityToFavorite(zipcode: String, note: NoteEntity) {
        val idNote = createNote(note)
        createFavoriteLink(FavoriteEntity(zipcode, idNote))
    }

    // Create a new entry into place table. This method is only used as help function to import data.
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaceEntity(place: PlaceEntity)

    /** Read **/
    // Count all notes in the favorite table with the given zipcode.
    @Transaction
    @Query("SELECT COUNT(*) FROM Favorites WHERE zipcode_place = :zipcode")
    suspend fun countNotesEntitiesFromFavorite(zipcode: String): Int

    @Query(
        value = """
            SELECT EXISTS (
                SELECT 1 FROM Favorites f
                JOIN Notes n ON f.id_note = n.id
                WHERE f.zipcode_place = :zipcode AND n.title = :title AND n.content = :content
            )
        """
    )
    suspend fun doesNoteEntityExist(zipcode: String, title: String, content: String): Boolean

    // Search for a place with given zipcode.
    @Query("SELECT EXISTS (SELECT 1 FROM Places WHERE zipcode = :zipcode)")
    suspend fun doesPlaceEntityExist(zipcode: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM Notes WHERE title = :title)")
    suspend fun doesTitleExist(title: String): Boolean

    // Search for a place with given zipcode and return its notes.
    @Transaction
    @Query("""
        SELECT p.* FROM Places p
        JOIN Favorites f ON p.zipcode = f.zipcode_place
        WHERE zipcode = :zipcode"""
    )
    suspend fun selectFavorite(zipcode: String): PlaceWithNotes?

    // Search for favorite places that have a note with the given title.
    @Query(
        value = """
            SELECT p.*, n.*
            FROM Places p
            JOIN Favorites f ON p.zipcode = f.zipcode_place
            JOIN Notes n ON f.id_note = n.id
            WHERE n.title LIKE '%' || :query || '%'
            ORDER BY p.zipcode ASC, n.title ASC
        """
    )
    fun selectFavoritesByTitle(query: String): Flow<List<PlaceWithNotes>>

    // Search for all place registers that are favorite.
    @Transaction
    @Query(
        value = """
            SELECT DISTINCT p.* FROM Places p
            JOIN Favorites f ON p.zipcode = f.zipcode_place
            WHERE p.zipcode LIKE '%' || :query || '%'
            ORDER BY p.zipcode ASC
        """
    )
    fun selectFavoritesByZipcode(query: String = ""): Flow<List<PlaceWithNotes>>

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
            DELETE FROM Notes 
            WHERE id IN (
                SELECT id_note FROM Favorites
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
    @Query("""
        SELECT DISTINCT p.* FROM places p
        JOIN favorites f ON p.zipcode = f.zipcode_place
    """)
    fun exportFavorites(): Flow<List<PlaceWithNotes>>
}
