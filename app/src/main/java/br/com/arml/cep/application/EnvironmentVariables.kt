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

    data object LogReducerVariables: EnvironmentVariables() {
        const val DELETE_ALL_LOGS_SUCCESS_MSG = "Todos os logs foram deletados"
        const val DELETE_ALL_LOGS_FAILURE_MSG = "Não foi possível deletar todos os logs"
    }

    data object CacheReducerVariables: EnvironmentVariables() {
        const val DELETE_ALL_CACHE_SUCCESS_MSG = "Todos registros em cache foram excluídos"
        const val DELETE_ALL_CACHE_FAILURE_MSG = "Não foi possível excluir todos os registros em cache"
        fun getDeletingCacheSuccessMessage(zipcode: String) = "CEP $zipcode removido do cache"
        fun getDeletingCacheFailureMessage(zipcode: String) = "Não foi possível remover o CEP $zipcode do cache"
        fun getAddingFavoriteSuccessMessage(zipcode: String) = "CEP $zipcode adicionado aos favoritos"
        fun getAddingFavoriteFailureMessage(zipcode: String) = "Não foi possível adicionar o CEP $zipcode aos favoritos"

    }
}
