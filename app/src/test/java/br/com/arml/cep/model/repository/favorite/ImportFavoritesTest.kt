package br.com.arml.cep.model.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.isContentEquals
import br.com.arml.cep.model.exception.BackupException.ImportEmptyFavoriteException
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.source.local.FavoriteDao
import br.com.arml.cep.utils.assertFlowFailure
import br.com.arml.cep.utils.assertFlowSuccess
import br.com.arml.cep.utils.mockAnswer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ImportFavoritesTest {
    private val mockFavoriteDao = mockk<FavoriteDao>()
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = spyk(FavoriteRepository(mockFavoriteDao))
    }

    private fun mockFavoriteDoesPlaceEntityExist(
        zipcode: String,
        exists: Boolean = false,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.doesPlaceEntityExist(zipcode) },
        exists,
        exception
    )

    private fun mockFavoriteInsertPlaceEntity(
        placeEntity: PlaceEntity,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.insertPlaceEntity(placeEntity) },
        Unit,
        exception
    )

    private fun mockFavoriteDoesNoteEntityExist(
        zipcode: String,
        title: String,
        content: String,
        exists: Boolean = false,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.doesNoteEntityExist(zipcode,title,content) },
        exists,
        exception
    )

    private fun mockFavoriteInsertNoteEntityToFavorite(
        zipcode: String,
        noteEntity: NoteEntity,
        exception: Exception? = null
    ) = mockAnswer(
        { mockFavoriteDao.insertNoteEntityToFavorite(zipcode,noteEntity) },
        Unit,
        exception
    )

    /*
     * Function: importFavorites(favorites: List<Place>)
     * Dependencies:
     *  - From FavoriteDao: doesPlaceEntityExist, doesNoteEntityExist, insertNoteEntityToFavorite
     */

    @Test
    fun `importFavorites should insert all data when none exists`() = runTest {
        val data = mockFavoritePlaces

        data.forEach { place ->
            val placeEntity = place.toEntity()
            val noteEntities = place.notes.map { it.toEntity() }

            mockFavoriteDoesPlaceEntityExist(placeEntity.zipcode)
            coEvery { mockFavoriteDao.insertPlaceEntity(any()) } answers { }

            noteEntities.forEach { noteEntity ->
                mockFavoriteDoesNoteEntityExist(
                    zipcode = placeEntity.zipcode,
                    title = noteEntity.title,
                    content = noteEntity.content
                )
                mockFavoriteInsertNoteEntityToFavorite(placeEntity.zipcode, noteEntity)
            }
        }

        val responses = repository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = data.size) { mockFavoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = data.sumOf { it.notes.size }) {
            mockFavoriteDao.insertNoteEntityToFavorite(any(), any())
        }
    }

    @Test
    fun `importFavorites should insert only data that does not exist`() = runTest {
        val data = mockFavoritePlaces

        data.forEachIndexed { dataIndex, place ->
            val placeEntity = place.toEntity()
            val noteEntities = place.notes.map { it.toEntity() }

            mockFavoriteDoesPlaceEntityExist(placeEntity.zipcode, exists = dataIndex % 2 == 0)
            coEvery { mockFavoriteDao.insertPlaceEntity(any()) } answers { }

            noteEntities.forEachIndexed { index, noteEntity ->
                mockFavoriteDoesNoteEntityExist(
                    zipcode = placeEntity.zipcode,
                    title = noteEntity.title,
                    content = noteEntity.content,
                    exists = if (dataIndex % 2 == 0) index % 2 == 0 else false
                )
                mockFavoriteInsertNoteEntityToFavorite(placeEntity.zipcode, noteEntity)
            }
        }

        val responses = repository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        data.forEachIndexed { dataIndex, place ->
            coVerify(exactly = if (dataIndex % 2 == 0) 0 else 1) {
                mockFavoriteDao.insertPlaceEntity(
                    match {  it.isContentEquals(place.toEntity()) }
                )
            }

            place.notes.forEachIndexed { index, note ->
                val q = if (dataIndex % 2 == 0) {
                    if (index % 2 == 0) 0 else 1
                } else 1
                val zipcode = place.cep.text
                coVerify(exactly = q) {
                    mockFavoriteDao.insertNoteEntityToFavorite(zipcode, note.toEntity())
                }
            }
        }
    }

    @Test
    fun `importFavorites should not insert anything when all data exists`() = runTest {
        val data = mockFavoritePlaces

        data.forEach { place ->
            val placeEntity = place.toEntity()
            val noteEntities = place.notes.map { it.toEntity() }

            mockFavoriteDoesPlaceEntityExist(placeEntity.zipcode, exists = true)
            mockFavoriteInsertPlaceEntity(placeEntity)

            noteEntities.forEach { noteEntity ->
                mockFavoriteDoesNoteEntityExist(
                    zipcode = placeEntity.zipcode,
                    title = noteEntity.title,
                    content = noteEntity.content,
                    exists = true
                )
                mockFavoriteInsertNoteEntityToFavorite(placeEntity.zipcode, noteEntity)
            }
        }

        val responses = repository.importFavorites(data).toList()

        responses.assertFlowSuccess { assertThat(it).isEqualTo(Unit) }
        coVerify(exactly = 0) { mockFavoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 0) { mockFavoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should emits Failure when data is empty`() = runTest {
        val expectedException = ImportEmptyFavoriteException()
        val responses = repository.importFavorites(emptyList()).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException.javaClass)
        }
    }

    @Test
    fun `importFavorites should Emit Failure when insertPlace throws Exception`() = runTest {
        val data = mockFavoritePlaces
        val expectedException = SQLiteException("Insert Place Error")

        data.forEach { place ->
            val placeEntity = place.toEntity()
            val noteEntities = place.notes.map { it.toEntity() }

            mockFavoriteDoesPlaceEntityExist(placeEntity.zipcode)
            coEvery { mockFavoriteDao.insertPlaceEntity(any()) } throws expectedException

            noteEntities.forEach { noteEntity ->
                mockFavoriteDoesNoteEntityExist(
                    zipcode = placeEntity.zipcode,
                    title = noteEntity.title,
                    content = noteEntity.content
                )
                mockFavoriteInsertNoteEntityToFavorite(placeEntity.zipcode, noteEntity)
            }
        }

        val responses = repository.importFavorites(data).toList()

        responses.assertFlowFailure {
            assertThat(it).isInstanceOf(expectedException::class.java)
        }
        coVerify(exactly = 1) { mockFavoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 0) { mockFavoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }

    @Test
    fun `importFavorites should Emit Failure when insertNote throws Exception`() = runTest {
        val data = mockFavoritePlaces
        val expectedException = SQLiteException("Insert Note Error")

        data.forEach { place ->
            val placeEntity = place.toEntity()
            val noteEntities = place.notes.map { it.toEntity() }

            mockFavoriteDoesPlaceEntityExist(placeEntity.zipcode)
            coEvery { mockFavoriteDao.insertPlaceEntity(any()) } answers { }

            noteEntities.forEach { noteEntity ->
                mockFavoriteDoesNoteEntityExist(
                    zipcode = placeEntity.zipcode,
                    title = noteEntity.title,
                    content = noteEntity.content
                )
                mockFavoriteInsertNoteEntityToFavorite(
                    placeEntity.zipcode,
                    noteEntity,
                    expectedException
                )
            }
        }

        val responses = repository.importFavorites(data).toList()

        responses.assertFlowFailure { throwable ->
            assertThat(throwable).isInstanceOf(expectedException.javaClass)
        }
        coVerify(atLeast = 1) { mockFavoriteDao.insertPlaceEntity(any()) }
        coVerify(exactly = 1) { mockFavoriteDao.insertNoteEntityToFavorite(any(), any()) }
    }
}