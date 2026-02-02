package br.com.arml.cep.model.exception

sealed class BackupException(override val message: String): Exception() {
    class ExportUriException: BackupException("Falha ao exportar backup")
    class ImportUriException: BackupException("Falha ao importar backup")
    class ImportEmptyFavoriteException: BackupException("Arquivo não contém favoritos")
}