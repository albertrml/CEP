package br.com.arml.cep.util.exception

sealed class CepException(override val message: String): Exception() {
    class EmptyCepException: CepException("Cep must not be empty")
    class InputCepException: CepException("Cep only accepts digits")
    class SizeCepException: CepException("Cep must have 8 digits")
}