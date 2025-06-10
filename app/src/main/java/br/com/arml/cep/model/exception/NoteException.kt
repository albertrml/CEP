package br.com.arml.cep.model.exception

import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH

sealed class NoteException(override val message: String): Exception(){
    class EmptyTitleException: NoteException(
        message = "Título não pode ser vazio"
    )

    class TitleTooLongException: NoteException(
        message = "Título não pode ter mais de ${MAX_TITLE_LENGTH} caracteres"
    )

    class TitleTooShortException: NoteException(
        message = "Título não pode ter menos de ${MIN_TITLE_LENGTH} caracteres"
    )

    class ContentTooLongException: NoteException(
        message = "Conteúdo não pode ter mais de ${MAX_CONTENT_LENGTH} caracteres"
    )

    class ConversionRoomNoteException(val value: String, val errorMsg: String): NoteException(
        message = "Erro inesperado ao converter String para Note: '$value'. Causa: $errorMsg"
    )
}