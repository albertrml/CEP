package br.com.arml.cep.model.source.local.migrations

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        /** Criação da tabela Places **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS 
            Places(
                zipcode TEXT NOT NULL PRIMARY KEY,
                street TEXT NOT NULL,
                complement TEXT NOT NULL,
                district TEXT NOT NULL,
                city TEXT NOT NULL,
                state TEXT NOT NULL,
                uf TEXT NOT NULL,
                region TEXT NOT NULL,
                country TEXT NOT NULL,
                ddd TEXT NOT NULL
            )
        """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_places_street_district_city_state` ON `Places` (`street`, `district`, `city`, `state`)")

        /** Criação da tabela Logs **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS
            Logs(
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                zipcode_place TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                FOREIGN KEY(zipcode_place) REFERENCES Places(zipcode) 
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION 
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_logs_zipcode_place ON Logs(zipcode_place)
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
                title TEXT NOT NULL UNIQUE,
                content TEXT NOT NULL
            )
        """.trimIndent()
        )


        /** Crição da tabela Favorites **/
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS
            Favorites(
                zipcode_place TEXT NOT NULL,
                id_note INTEGER NOT NULL,
                FOREIGN KEY(zipcode_place) REFERENCES Places(zipcode)
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION,
                FOREIGN KEY(id_note) REFERENCES Notes(id)
                    ON DELETE CASCADE
                    ON UPDATE NO ACTION,
                PRIMARY KEY(zipcode_place, id_note)
            )
        """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_favorites_zipcode_place` ON `Favorites` (`zipcode_place`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_favorites_id_note` ON `Favorites` (`id_note`)")

        /** Migração dos dados da tabela log_table para a tabela Logs **/
        db.execSQL(
            """
            INSERT INTO Logs(zipcode_place, timestamp)
            SELECT cep, timestamp FROM log_table
        """.trimIndent()
        )

        /** Migração dos dados da tabela place_table para a tabela Places **/
        db.execSQL("""
            INSERT INTO Places(
                zipcode, 
                street, 
                complement, 
                district, 
                city, 
                state, 
                uf, 
                region, 
                country, 
                ddd
            )
            SELECT 
                cep, 
                address_street, 
                address_complement, 
                address_district, 
                address_city, 
                address_state, 
                address_uf, 
                address_region, 
                address_country, 
                address_ddd 
            FROM place_table
        """.trimIndent())

        /** Migração dos dados correspondentes da tabela place_table às tabelas Favorites e Notes **/
        db.beginTransaction()
        try {
            val cursor = db.query("""
                SELECT cep, note FROM place_table 
                WHERE favorite_status = 1 AND note IS NOT NULL
            """.trimIndent())

            cursor.use { c ->
                if (c.moveToFirst()){
                    val cepIndex = c.getColumnIndex("cep")
                    val noteIndex = c.getColumnIndex("note")
                    val delimiter = "||<NOTE_SEP>||"

                    do {
                        val cep = c.getString(cepIndex)
                        val note = c.getString(noteIndex)
                        val title = note.substringBefore(delimiter)
                        val content = note.substringAfter(delimiter)

                        val notesValues = ContentValues().apply {
                            put("title", title)
                            put("content", content)
                        }

                        val idNote = db.insert(
                            "Notes",
                            OnConflictStrategy.IGNORE,
                            notesValues
                        )

                        if (idNote == -1L) continue

                        val favoritesValues = ContentValues().apply{
                            put("zipcode_place", cep)
                            put("id_note", idNote)
                        }
                        db.insert(
                            "Favorites",
                            OnConflictStrategy.IGNORE,
                            favoritesValues
                        )

                    } while (c.moveToNext())
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        /** Remoção da tabela log_table **/
        db.execSQL("DROP TABLE log_table")

        /** Remoção da tabela place_table **/
        db.execSQL("DROP TABLE place_table")
    }
}