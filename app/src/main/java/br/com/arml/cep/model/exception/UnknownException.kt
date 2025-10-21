package br.com.arml.cep.model.exception


sealed class UnknownException(override val message: String): Exception() {
    class FetchPlaceException: UnknownException(
        message = "Erro desconhecido ao coletar favoritos"
    )
}