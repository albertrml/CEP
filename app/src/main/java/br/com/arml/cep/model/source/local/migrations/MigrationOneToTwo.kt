package br.com.arml.cep.model.source.local.migrations

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.arml.cep.model.utils.normalizeForDBSearch

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        /** Criação da tabela Places **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS 
            Places(
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                zipcode TEXT NOT NULL,
                street TEXT NOT NULL,
                complement TEXT NOT NULL,
                district TEXT NOT NULL,
                city TEXT NOT NULL,
                state TEXT NOT NULL,
                uf TEXT NOT NULL,
                region TEXT NOT NULL,
                country TEXT NOT NULL,
                ddd TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                street_search TEXT NOT NULL,
                city_search TEXT NOT NULL
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_places_search ON 
            Places (
                street_search, 
                district, 
                city_search, 
                state
            )
        """.trimIndent()
        )
        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS index_places_zipcode ON Places(zipcode)
        """.trimIndent()
        )
        db.execSQL(
            """
                CREATE INDEX IF NOT EXISTS index_places_street_search ON Places(street_search)
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE INDEX IF NOT EXISTS index_places_city_search ON Places(city_search)
            """.trimIndent()
        )


        /** Criação da tabela Logs **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS
            Logs(
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                id_place INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                FOREIGN KEY(id_place) REFERENCES Places(id) 
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION 
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_logs_id_place ON Logs(id_place)
        """.trimIndent()
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_logs_timestamp ON Logs(timestamp)
        """.trimIndent()
        )

        /** Criação da tabela Notes **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS
            Notes(
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                content TEXT NOT NULL
            )
        """.trimIndent()
        )

        db.execSQL(
            """
                CREATE UNIQUE INDEX IF NOT EXISTS index_notes_title_content ON Notes(title)
            """.trimIndent()
        )

        /** Crição da tabela Favorites **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS
            Favorites(
                id_place INTEGER NOT NULL,
                id_note INTEGER NOT NULL,
                FOREIGN KEY(id_place) REFERENCES Places(id)
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION,
                FOREIGN KEY(id_note) REFERENCES Notes(id)
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION,
                PRIMARY KEY(id_place, id_note)
            )
        """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_favorites_id_place` ON `Favorites` (`id_place`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_favorites_id_note` ON `Favorites` (`id_note`)")

        /** Migração dos dados em uma única transação atômica **/
        db.beginTransaction()
        try {
            /** Migração da tabela Places **/
            val cursorPlaces = db.query("""SELECT * FROM place_table""".trimIndent())
            cursorPlaces.use { c ->
                if (c.moveToFirst()) {
                    val cepIndex = c.getColumnIndex("address_zipCode")
                    val streetIndex = c.getColumnIndex("address_street")
                    val complementIndex = c.getColumnIndex("address_complement")
                    val districtIndex = c.getColumnIndex("address_district")
                    val cityIndex = c.getColumnIndex("address_city")
                    val stateIndex = c.getColumnIndex("address_state")
                    val ufIndex = c.getColumnIndex("address_uf")
                    val regionIndex = c.getColumnIndex("address_region")
                    val countryIndex = c.getColumnIndex("address_country")
                    val dddIndex = c.getColumnIndex("address_ddd")
                    val currentTime = System.currentTimeMillis()

                    do {
                        val streetValue = c.getString(streetIndex)
                        val cityValue = c.getString(cityIndex)
                        val placeValues = ContentValues().apply {
                            put("zipcode", c.getString(cepIndex))
                            put("street", streetValue)
                            put("complement", c.getString(complementIndex))
                            put("district", c.getString(districtIndex))
                            put("city", cityValue)
                            put("state", c.getString(stateIndex))
                            put("uf", c.getString(ufIndex))
                            put("region", c.getString(regionIndex))
                            put("country", c.getString(countryIndex))
                            put("ddd", c.getString(dddIndex))
                            put("created_at", currentTime)
                            put("street_search", streetValue.normalizeForDBSearch())
                            put("city_search", cityValue.normalizeForDBSearch())
                        }
                        db.insert(
                            "Places",
                            OnConflictStrategy.IGNORE,
                            placeValues
                        )
                    } while (c.moveToNext())
                }
            }

            /** Migração dos dados da tabela log_table para a tabela Logs **/
            db.execSQL(
                """
                INSERT INTO Logs(id_place, timestamp)
                SELECT p_new.id, l.timestamp 
                FROM log_table l
                JOIN Places p_new ON p_new.zipcode = l.cep
            """.trimIndent()
            )

            /** Migração dos dados correspondentes da tabela place_table às tabelas Favorites e Notes **/
            val cursorNotes = db.query(
                """
                SELECT address_zipCode, note FROM place_table 
                WHERE favorite_status = 1 AND note IS NOT NULL
            """.trimIndent()
            )
            cursorNotes.use { c ->
                if (c.moveToFirst()) {
                    val cepIndex = c.getColumnIndex("address_zipCode")
                    val noteIndex = c.getColumnIndex("note")
                    val delimiter = "||<NOTE_SEP>||"

                    do {
                        val cep = c.getString(cepIndex)
                        val note = c.getString(noteIndex)

                        val parts = note.split(delimiter, limit = 2)
                        val title = parts.getOrNull(0).orEmpty()
                        val content = parts.getOrNull(1).orEmpty()

                        val notesValues = ContentValues().apply {
                            put("title", title)
                            put("content", content)
                        }

                        val idNote = db.insert(
                            "Notes",
                            OnConflictStrategy.IGNORE,
                            notesValues
                        )

                        if (idNote != -1L) {
                            // Busca o ID numérico do lugar na nova tabela Places
                            val cursorId = db.query(
                                "SELECT id FROM Places WHERE zipcode = ?",
                                arrayOf(cep)
                            )
                            cursorId.use { cId ->
                                if (cId.moveToFirst()) {
                                    val idPlace = cId.getLong(0)
                                    val favoritesValues = ContentValues().apply {
                                        put("id_place", idPlace)
                                        put("id_note", idNote)
                                    }
                                    db.insert(
                                        "Favorites",
                                        OnConflictStrategy.IGNORE,
                                        favoritesValues
                                    )
                                }
                            }
                        }

                    } while (c.moveToNext())
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        /** Remoção das tabelas legadas **/
        db.execSQL("DROP TABLE IF EXISTS log_table")
        db.execSQL("DROP TABLE IF EXISTS place_table")
    }
}