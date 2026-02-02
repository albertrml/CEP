package br.com.arml.cep.model.exception

import br.com.arml.cep.model.domain.CEP_LENGTH

sealed class CepException(override val message: String): Exception() {
    class EmptyCepException: CepException("Cep não pode ser vazio")
    class SizeCepException: CepException("Cep deve ter $CEP_LENGTH números")
    class NotFoundCepException: CepException("Cep não encontrado")
    class ConversionRoomException(wrongCpf: String, errorMsg: String): CepException(
        message = "Erro inesperado ao converter String para Cep: '$wrongCpf'. Causa: $errorMsg"
    )
    class IllegalPatternException: CepException(
        message = "Cep aceita apenas o formato XXXXX-XXX, onde X é um número de 0 a 9"
    )
}