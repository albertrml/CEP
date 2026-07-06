package br.com.arml.cep.model.exception

const val TITLE_TOO_SHORT = "título curto"
const val TITLE_TOO_LONG = "título longo"
const val CONTENT_TOO_LONG = "Conteúdo longo"

sealed class AdapterException (override val message: String): Exception() {
    class InputDoesNotMatchCepPatternException(): AdapterException(
        "JsonAdapterException: String não atende o padrão do CEP (XXXXX-XXX)"
    )
    class InputDoesNotAttendNoteRequirementsException(cause: String): AdapterException(
        "JsonAdapterException: A entrada não foi adaptada para a anotação por razão de $cause"
    )
    class UnknownException(e: Exception): AdapterException(
        "JsonAdapterException: ${e.message ?: "Erro desconhecido"}"
    )
}