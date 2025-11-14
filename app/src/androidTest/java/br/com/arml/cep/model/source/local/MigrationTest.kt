package br.com.arml.cep.model.source.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.model.source.local.migrations.MIGRATION_1_2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CepRoomDatabase::class.java
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // 1. Cria o banco de dados na versão 1 e o fecha.
        helper.createDatabase(TEST_DB, 1).apply {
            // Opcional: Você pode inserir dados no esquema antigo aqui para testar a migração dos dados.
            // execSQL("INSERT INTO place_table VALUES (...)")
            close()
        }

        // 2. Roda e valida a migração para a versão 2.
        // Esta é a linha que vai falhar e mostrar o erro completo no console de teste.
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // Opcional: Você pode reabrir o banco e verificar se os dados foram migrados corretamente.
    }
}
