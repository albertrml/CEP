package br.com.arml.cep.model.source.local.migration

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.model.domain.toEntity
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.model.source.local.CepRoomDatabase
import br.com.arml.cep.model.source.local.migrations.MIGRATION_1_2
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.fail

private const val TEST_DB = "migration-test"

@RunWith(AndroidJUnit4::class)
class MigrationOneToTwoTest {
    lateinit var migratedDb: CepRoomDatabase
    private val favoriteEntry = mockFavoritePlaces.first()
    private val unfavoriteEntry = mockUnfavoritePlaces.last()
    private val favoriteTimestamp = 1672531200L
    private val unfavoriteTimestamp = 1686367800L

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CepRoomDatabase::class.java
    )

    @Before
    fun setupMigration() {
        // Ensures that test data does not collide, which was the root cause of the error.
        assertThat(unfavoriteEntry.cep.text).isNotEqualTo(favoriteEntry.cep.text)

        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                sql = """
                    CREATE TABLE IF NOT EXISTS place_table (
                        cep TEXT NOT NULL PRIMARY KEY, 
                        favorite_status INTEGER NOT NULL, note TEXT, 
                        address_zipCode TEXT NOT NULL, address_street TEXT NOT NULL, 
                        address_complement TEXT NOT NULL, address_district TEXT NOT NULL, 
                        address_city TEXT NOT NULL, address_state TEXT NOT NULL,
                        address_uf TEXT NOT NULL, address_region TEXT NOT NULL, 
                        address_country TEXT NOT NULL, address_ddd TEXT NOT NULL
                    )
                """.trimIndent()
            )
            execSQL(
                sql = """
                    CREATE TABLE IF NOT EXISTS log_table (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        cep TEXT NOT NULL, 
                        timestamp INTEGER NOT NULL, 
                        FOREIGN KEY(cep) REFERENCES place_table(cep) 
                        ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """.trimIndent()
            )

            // Inserts the NON-FAVORITE record
            with(unfavoriteEntry) {
                execSQL(
                    """
                        INSERT INTO place_table ( 
                            cep,
                            favorite_status, note, 
                            address_zipCode, address_street, 
                            address_complement, address_district, 
                            address_city, address_state,
                            address_uf, address_region, 
                            address_country, address_ddd
                        ) 
                        VALUES(
                            '${cep.text}',
                             0, null,
                            '${address.zipCode}', '${address.street}', 
                            '${address.complement}', '${address.district}', 
                            '${address.city}', '${address.state}', 
                            '${address.uf}', '${address.region}', 
                            '${address.country}', '${address.ddd}'
                        )
                    """.trimIndent()
                )

                execSQL(
                    sql = """
                    INSERT INTO log_table (id, cep, timestamp) 
                    VALUES(2, '${cep.text}', $unfavoriteTimestamp)
                """.trimIndent()
                )
            }

            // Inserts the FAVORITE record
            with(favoriteEntry) {
                execSQL(
                    sql = """
                        INSERT INTO place_table (
                            cep,
                            favorite_status, note,
                            address_zipCode, address_street, 
                            address_complement, address_district, 
                            address_city, address_state,
                            address_uf, address_region, 
                            address_country, address_ddd
                        ) 
                        VALUES(
                            '${cep.text}',
                             1, '${notes.first().title}||<NOTE_SEP>||${notes.first().content}',
                            '${address.zipCode}', '${address.street}', 
                            '${address.complement}', '${address.district}', 
                            '${address.city}', '${address.state}', 
                            '${address.uf}', '${address.region}', 
                            '${address.country}', '${address.ddd}' 
                        )
                    """.trimIndent()
                )

                execSQL(
                    sql = """
                    INSERT INTO log_table (id, cep, timestamp)
                    VALUES(1, '${cep.text}', $favoriteTimestamp)
                """.trimIndent()
                )
            }

            close()
        }

        // 2. Runs the migration to version 2 and validates the schema
        helper.runMigrationsAndValidate(
            TEST_DB,
            2,
            true,
            MIGRATION_1_2
        )

        // 3. Opens the migrated database and verifies that the data was copied correctly
        migratedDb = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CepRoomDatabase::class.java,
            TEST_DB
        ).addMigrations(MIGRATION_1_2).build()
    }

    @After
    fun tearDown() {
        migratedDb.clearAllTables()
        migratedDb.close()
    }

    @Test
    fun tables_shouldContainOnlyExpectedTables_whenMigrateFromOneToTwoVersion(){
        val query = SimpleSQLiteQuery("SELECT name FROM sqlite_master WHERE type='table'")
        val expectedTables = listOf("Favorites", "Logs", "Notes", "Places")
        val tables = migratedDb.query(query).use { cursor ->
            val index = cursor.getColumnIndex("name")
            generateSequence {
                if (cursor.moveToNext()) cursor.getString(index) else null
            }.toList()
        }
        assertThat(tables).containsAtLeastElementsIn(expectedTables)
    }

    @Test
    fun zipcodeKey_shouldBeEqualAddressZipcode_whenMigrateFromOneToTwoVersion(){
        val expectedZipcode = listOf(
            favoriteEntry.address.zipCode,
            unfavoriteEntry.address.zipCode
        )
        val query = SimpleSQLiteQuery("""
            SELECT p.zipcode, l.zipcode_place, f.zipcode_place 
            FROM Places p
            LEFT JOIN Logs l ON p.zipcode = l.zipcode_place
            LEFT JOIN Favorites f ON p.zipcode = f.zipcode_place
        """.trimIndent()
        )
        migratedDb.query(query).use{ cursor ->
            println("--- Joined ZipCode Results ---")
            val placeZipIndex = 0
            val logZipIndex = 1
            val favZipIndex = 2

            while(cursor.moveToNext()) {
                val placeZip = cursor.getString(placeZipIndex)
                val logZip = cursor.getString(logZipIndex)
                val favZip = cursor.getString(favZipIndex)

                println("Place: $placeZip, Log: $logZip, Favorite: $favZip")

                placeZip?.let { assertThat(it).isIn(expectedZipcode) }
                logZip?.let { assertThat(it).isIn(expectedZipcode) }
                favZip?.let { assertThat(it).isIn(expectedZipcode) }
            }
            println("------------------------------")
        }
    }

    @Test
    fun place_shouldContainOnlyNonFavoritePlace_whenQueriedForCachedPlaces() = runTest {
        val expectedPlace = unfavoriteEntry.toEntity()
        
        val cachedPlaces = migratedDb.cacheDao().selectCachedPlaceEntitiesByZipcode("").first()

        assertThat(cachedPlaces).hasSize(1)
        assertThat(cachedPlaces.first()).isEqualTo(expectedPlace)
    }

    @Test
    fun logs_shouldContainAllMigratedLogs() = runTest {
        val favoriteZip = favoriteEntry.address.zipCode
        val unfavoriteZip = unfavoriteEntry.address.zipCode

        val favoriteLog = migratedDb.logDao().selectLogEntitiesByZipcode(favoriteZip).first()
        assertThat(favoriteLog).hasSize(1)
        assertThat(favoriteLog.first().timestamp).isEqualTo(favoriteTimestamp)

        val unfavoriteLog = migratedDb.logDao().selectLogEntitiesByZipcode(unfavoriteZip).first()
        assertThat(unfavoriteLog).hasSize(1)
        assertThat(unfavoriteLog.first().timestamp).isEqualTo(unfavoriteTimestamp)
    }

    @Test
    fun favorite_shouldBeMigratedWithItsNotes() = runTest {
        val expectedPlace = favoriteEntry.toEntity()
        val expectedNote = favoriteEntry.notes.first()

        val favorite = migratedDb.favoriteDao().selectFavorite(expectedPlace.zipcode)

        assertThat(favorite).isNotNull()
        favorite?.let { placeWithNotes ->
            assertThat(placeWithNotes.place).isEqualTo(expectedPlace)
            assertThat(placeWithNotes.notes).hasSize(1)
            val migratedNote = placeWithNotes.notes.first()
            assertThat(migratedNote.title).isEqualTo(expectedNote.title)
            assertThat(migratedNote.content).isEqualTo(expectedNote.content)
        } ?: fail("Favorite place with zipcode ${expectedPlace.zipcode} not found after migration")
    }
}