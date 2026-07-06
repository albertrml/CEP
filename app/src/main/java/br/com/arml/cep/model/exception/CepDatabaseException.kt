package br.com.arml.cep.model.exception

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import br.com.arml.cep.model.exception.CepDatabaseException.ConstraintViolationException
import br.com.arml.cep.model.exception.CepDatabaseException.DatabaseCorruptException
import br.com.arml.cep.model.exception.CepDatabaseException.DiskFullException
import br.com.arml.cep.model.exception.CepDatabaseException.UnknownDatabaseException

sealed class CepDatabaseException(override val message: String): Exception(message) {
    class IllegalNoteQuantity : CepDatabaseException(
        """Favoritos devem ter pelo menos uma nota. Por favor, desfavorite o cep para 
           apagar as notas restantes.""".trimIndent()
    )
    class ConstraintViolationException(message: String) : CepDatabaseException(message)
    class DiskFullException : CepDatabaseException(
        "Espaço de armazenamento insuficiente no dispositivo."
    )
    class DatabaseCorruptException : CepDatabaseException(
        "O banco de dados local foi corrompido e precisará ser reiniciado."
    )
    class UnknownDatabaseException(message: String) : CepDatabaseException(message)
}

/**
  * Função genérica para capturar e mapear erros comuns de banco de dados.
  */
private suspend fun <T> wrapDatabaseErrors(tag: String, f: suspend () -> T): T {
    return try {
        f()
    } catch (e: CepDatabaseException) {
        throw e
    } catch (e: SQLiteConstraintException) {
        Log.e(tag, "Violação de restrição (Chave duplicada ou FK)", e)
        throw ConstraintViolationException(
            "Erro de integridade: este registro já existe ou possui dependências."
        )
    } catch (e: SQLiteFullException) {
        Log.e(tag, "Memória do dispositivo cheia", e)
        throw DiskFullException()
    } catch (e: SQLiteDatabaseCorruptException) {
        Log.e(tag, "Banco de dados corrompido", e)
        throw DatabaseCorruptException()
    } catch (e: SQLiteDiskIOException) {
        Log.e(tag, "Erro de I/O no disco", e)
        throw UnknownDatabaseException("Erro de leitura/escrita no dispositivo.")
    } catch (e: SQLiteException) {
        Log.e(tag, "Erro genérico de SQLite", e)
        throw UnknownDatabaseException("Erro interno do banco de dados.")
    } catch (e: Exception) {
        Log.e(tag, "Erro inesperado na operação de banco", e)
        throw UnknownDatabaseException("Ocorreu um erro inesperado ao acessar os dados.")
    }
}

suspend fun <T> tryConnectionCepDatabase(f: suspend () -> T): T {
    return wrapDatabaseErrors("DatabaseConnection", f)
}

suspend fun <T> tryReadCepDatabase(f: suspend () -> T): T {
    return wrapDatabaseErrors("DatabaseRead", f)
}

suspend fun <T> tryWriteCepDatabase(f: suspend () -> T): T {
    return wrapDatabaseErrors("DatabaseWrite", f)
}

suspend fun <T> tryUpdateCepDatabase(f: suspend () -> T): T {
    return wrapDatabaseErrors("DatabaseUpdate", f)
}

suspend fun <T> tryDeleteCepDatabase(f: suspend () -> T): T {
    return wrapDatabaseErrors("DatabaseDelete", f)
}