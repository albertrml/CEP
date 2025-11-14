package br.com.arml.cep.model.source.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.runner.RunWith

/*private const val TEST_DB = "migration-test"*/

@RunWith(AndroidJUnit4::class)
class CepRoomDatabaseTest {

    /*private val favoriteEntry = mockFavoritePlaces.first()
    private val unfavoriteEntry = mockUnfavoritePlaces.first()
    private val favoriteTimestamp = 1672531200L
    private val unfavoriteTimestamp = 1686367800L*/

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CepRoomDatabase::class.java
    )

    /*@Test
    @Throws(IOException::class)
    fun migrate1To2_andValidateSchema_andData() = runTest {
        // 1. Cria um banco de dados na versão 1 e insere dados de teste
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                sql = """
                    CREATE TABLE place_table (
                        cep TEXT PRIMARY KEY NOT NULL,
                        zipcode TEXT, street TEXT, complement TEXT, 
                        district TEXT, city TEXT, state TEXT, 
                        uf TEXT, region TEXT, country TEXT, 
                        ddd TEXT, favorite_status INTEGER DEFAULT 0 NOT NULL, note TEXT
                    )
                """.trimIndent()
            )
            execSQL(
                sql = """
                    CREATE TABLE log_table (
                        id INTEGER PRIMARY KEY, 
                        cep TEXT, 
                        timestamp INTEGER
                    )
                """.trimIndent()
            )

            with(favoriteEntry) {
                execSQL(
                    sql = """
                        INSERT INTO place_table (
                            cep, zipcode, street, 
                            complement, district, city, 
                            state, uf, region, 
                            country, ddd, favorite_status, 
                            note
                        ) 
                        VALUES(
                            '${cep.text}', '${cep.toFormattedCep()}', '${address.street}', 
                            '${address.complement}', '${address.district}', '${address.city}', 
                            '${address.state}', '${address.uf}', '${address.region}', 
                            '${address.country}', '${address.ddd}', 1, 
                            '${notes?.title ?: ""}||<NOTE_SEP>||${notes?.content ?: ""}'
                        )
                    """.trimIndent()
                )
            }
            with(unfavoriteEntry) {
                execSQL(
                    """
                        INSERT INTO place_table (
                            cep, zipcode, street, 
                            complement, district, city, 
                            state, uf, region, 
                            country, ddd, favorite_status, 
                            note
                        ) 
                        VALUES(
                            '${cep.text}', '${cep.toFormattedCep()}', '${address.street}', 
                            '${address.complement}', '${address.district}', '${address.city}', 
                            '${address.state}', '${address.uf}', '${address.region}', 
                            '${address.country}', '${address.ddd}', 0, ''
                        )
                    """.trimIndent()
                )
            }
            execSQL(
                sql = """
                    INSERT INTO log_table (id, cep, timestamp) 
                    VALUES(1, '${favoriteEntry.cep.text}', $favoriteTimestamp)
                """.trimIndent()
            )
            execSQL(
                sql = """
                    INSERT INTO log_table (id, cep, timestamp) 
                    VALUES(2, '${unfavoriteEntry.cep.text}', $unfavoriteTimestamp)
                """.trimIndent()
            )

            close()
        }

        // 2. Executa a migração para a versão 2 e valida o esquema
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // 3. Abre o banco migrado e verifica se os dados foram copiados corretamente
        val migratedDb = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CepRoomDatabase::class.java,
            TEST_DB
        ).addMigrations(MIGRATION_1_2).build()

        // Verifica se os logs foram migrados corretamente
        val favoriteLog = migratedDb.logDao().getLogByZipcode(favoriteEntry.cep.text).first()
        assertThat(favoriteLog).hasSize(1)
        assertThat(favoriteLog[0].timestamp).isEqualTo(favoriteTimestamp)

        val unfavoriteLog = migratedDb.logDao().getLogByZipcode(unfavoriteEntry.cep.text).first()
        assertThat(unfavoriteLog).hasSize(1)
        assertThat(unfavoriteLog[0].timestamp).isEqualTo(unfavoriteTimestamp)

        // Verifica se o place favorito foi migrado e tem sua nota
        val favoritePlace = migratedDb.favoriteDao().readAFavoriteWithNotes(favoriteEntry.cep.text).first()
        assertThat(favoritePlace).isNotNull()
        favoritePlace!!.run {
            assertThat(place.zipcode).isEqualTo(favoriteEntry.address.zipCode)
            assertThat(place.street).isEqualTo(favoriteEntry.address.street)
            assertThat(place.complement).isEqualTo(favoriteEntry.address.complement)
            assertThat(place.city).isEqualTo(favoriteEntry.address.city)
            assertThat(place.district).isEqualTo(favoriteEntry.address.district)
            assertThat(place.country).isEqualTo(favoriteEntry.address.country)
            assertThat(place.region).isEqualTo(favoriteEntry.address.region)
            assertThat(place.uf).isEqualTo(favoriteEntry.address.uf)
            assertThat(place.ddd).isEqualTo(favoriteEntry.address.ddd)
            assertThat(notes).hasSize(1)
            notes[0].run {
                assertThat(title).isEqualTo(favoriteEntry.notes?.title ?: "")
                assertThat(content).isEqualTo(favoriteEntry.notes?.content ?: "")
            }
        }

        // Verifica se o place não-favorito está no "cache"
        val unwantedPlace = migratedDb.cacheDao().getByZipcode(unfavoriteEntry.cep.text).first()
        assertThat(unwantedPlace).hasSize(1)
        unwantedPlace[0].apply {
            assertThat(zipcode).isEqualTo(unfavoriteEntry.address.zipCode)
            assertThat(street).isEqualTo(unfavoriteEntry.address.street)
            assertThat(complement).isEqualTo(unfavoriteEntry.address.complement)
            assertThat(city).isEqualTo(unfavoriteEntry.address.city)
            assertThat(district).isEqualTo(unfavoriteEntry.address.district)
            assertThat(country).isEqualTo(unfavoriteEntry.address.country)
            assertThat(region).isEqualTo(unfavoriteEntry.address.region)
            assertThat(uf).isEqualTo(unfavoriteEntry.address.uf)
            assertThat(ddd).isEqualTo(unfavoriteEntry.address.ddd)
        }

        migratedDb.close()
    }*/
}