package br.com.arml.cep.util.exception

sealed class CepException(override val message: String): Exception() {
    class EmptyCepException: CepException("Cep não pode ser vazio")
    class InputCepException: CepException("Cep aceita apenas números")
    class SizeCepException: CepException("Cep deve ter 8 números")
    class NotFoundCepException: CepException("Cep não encontrado")
}