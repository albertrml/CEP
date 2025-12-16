package br.com.arml.cep.application

sealed class EnvironmentVariables(){
    data object FavoriteReducerVariables: EnvironmentVariables(){
        const val UNWANTED_FAVORITE_SELECTED_NONE = "Nenhum favorito selecionado"
        const val ADDING_NOTE_TO_FAVORITE_FAILURE_MSG = "Não foi possível adicionar nota ao CEP"
        const val DELETING_NOTE_FROM_FAVORITE_FAILURE_MSG = "Não foi possível remover nota"
        const val UPDATING_NOTE_FROM_FAVORITE_FAILURE_MSG = "Não foi possível atualizar nota"
        const val UPDATING_NOTE_FROM_FAVORITE_SUCCESS_MSG = "Não foi possível atualizar nota"
        const val IMPORTING_FAVORITES_SUCCESS_MSG = "Backup importado com sucesso"
        const val IMPORTING_FAVORITES_FAILURE_MSG = "Não foi possível importar backup"
        const val EXPORTING_FAVORITES_SUCCESS_MSG = "Backup exportado com sucesso"
        const val EXPORTING_FAVORITES_FAILURE_MSG = "Não foi possível exportar backup"

        fun getChangingToUnwantedSuccessMessage(zipcode: String) = "O cep $zipcode foi desfavoritado com sucesso"
        fun getChangingToUnwantedFailureMessage(zipcode: String) = "Não foi possível desfavoritar o cep $zipcode"
        fun getAddingNoteToFavoriteSuccessMessage(zipcode: String) = "Nota adicionada ao cep $zipcode"
        fun getDeletingNoteFromFavoriteSuccessMessage(zipcode: String) = "Nota removida do cep $zipcode"
    }

    data object SearchReducerVariables: EnvironmentVariables(){
        const val ADDING_TO_FAVORITE_FAILURE_MSG = "Não foi possível adicionar ao favorito"
        fun getAddingToFavoriteSuccessMessage(zipcode: String) = "CEP $zipcode adicionado aos favoritos"
    }
}
