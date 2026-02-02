package br.com.arml.cep.model.exception

sealed class CepDatabaseException(override val message: String): Exception() {
    class IllegalNoteQuantity: CepDatabaseException(
        message = "Favoritos devem ter pelo menos uma nota. " +
                  "Por favor, desfavorite o cep para apagar as notas restantes."
    )
}