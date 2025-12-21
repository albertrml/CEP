package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.ADDING_NOTE_TO_FAVORITE_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.DELETING_NOTE_FROM_FAVORITE_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.EXPORTING_FAVORITES_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.EXPORTING_FAVORITES_SUCCESS_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.IMPORTING_FAVORITES_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.IMPORTING_FAVORITES_SUCCESS_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.UNWANTED_FAVORITE_SELECTED_NONE
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.UPDATING_NOTE_FROM_FAVORITE_FAILURE_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.UPDATING_NOTE_FROM_FAVORITE_SUCCESS_MSG
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.getAddingNoteToFavoriteSuccessMessage
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.getChangingToUnwantedFailureMessage
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.getChangingToUnwantedSuccessMessage
import br.com.arml.cep.application.EnvironmentVariables.FavoriteReducerVariables.getDeletingNoteFromFavoriteSuccessMessage
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.favorite.FavoriteEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnCancelFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwantedResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnDeleteNoteFromFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnSelectFavoriteToUnwanted

class FavoriteReducer: Reducer<FavoriteState, FavoriteEvent, FavoriteEffect> {
    override fun reduce(
        previousState: FavoriteState,
        event: FavoriteEvent
    ): Pair<FavoriteState, FavoriteEffect?> {
        return when(event){
            /** Events associated with making favorite as unwanted **/
            /* Select favorite to remove and show warning. If confirm, change the favorite to
               unwanted; otherwise, hide warning. Whether Success or Failure in changing operation,
               a message is shown to user in the snackBar. */
            is OnSelectFavoriteToUnwanted -> {
                val updatedState = previousState.copy(
                    isVisibleUnwantedWarning = true,
                    selectedFavoriteToUnwanted = event.place
                )
                updatedState to null
            }
            is OnCancelFavoriteToUnwanted -> {
                val updatedState = previousState.copy(
                    isVisibleUnwantedWarning = false,
                    selectedFavoriteToUnwanted = null
                )
                updatedState to null
            }
            is OnConfirmFavoriteToUnwantedResponse -> {
                val zipcode = previousState.selectedFavoriteToUnwanted?.cep?.text
                    ?: return previousState to ShowSnackbar(UNWANTED_FAVORITE_SELECTED_NONE)

                when (event.response) {
                    is Response.Loading ->
                        previousState to null

                    is Response.Success -> {
                        val msg = getChangingToUnwantedSuccessMessage(zipcode)
                        previousState.copy(
                            selectedFavoriteToUnwanted = null,
                            isVisibleUnwantedWarning = false
                        ) to ShowSnackbar(msg)
                    }

                    is Response.Failure -> {
                        val msg = getChangingToUnwantedFailureMessage(zipcode)
                        previousState.copy(
                            selectedFavoriteToUnwanted = null,
                            isVisibleUnwantedWarning = false
                        ) to ShowSnackbar(msg)
                    }
                }
            }
            /** End vents associated with making favorite as unwanted **/

            /** Events associated with making favorite as unwanted **/
            is OnAddNoteToFavoriteResponse -> {
                val (response, zipcode) = event
                val effect = when(response){
                    is Response.Loading -> null
                    is Response.Success -> {
                        ShowSnackbar(getAddingNoteToFavoriteSuccessMessage(zipcode))
                    }
                    is Response.Failure -> {
                        ShowSnackbar(ADDING_NOTE_TO_FAVORITE_FAILURE_MSG)
                    }
                }
                previousState to effect
            }
            /** End events associated with making favorite as unwanted **/

            /** Events associated with Delete Note from Favorite **/
            is OnDeleteNoteFromFavoriteResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        val zipcode = response.result
                        val msg = getDeletingNoteFromFavoriteSuccessMessage(zipcode)
                        previousState to ShowSnackbar(msg)
                    }
                    is Response.Failure -> {
                        previousState to ShowSnackbar(DELETING_NOTE_FROM_FAVORITE_FAILURE_MSG)
                    }
                }
            }
            /** End events associated with Delete Note to Favorite **/

            /** Events associated with Navigate to Detail Pane **/
            is OnNavigateToDetailPane -> {
                val updatedState = previousState.copy(
                    selectedNoteToDetail = event.note,
                    selectedAddressToDetail = event.address,
                    selectedDataToDetail = event.address to event.note
                )
                updatedState to null
            }
            is FavoriteEvent.OnNavigateBackToListPane -> {
                val updatedState = previousState.copy(
                    selectedNoteToDetail = null,
                    selectedAddressToDetail = null,
                    selectedDataToDetail = null
                )
                updatedState to null
            }
            /** End events associated with Navigate to Detail Pane **/

            /** Events associated with update note **/
            is FavoriteEvent.OnUpdateNoteFromFavoriteResponse -> {
                when(event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        previousState to ShowSnackbar(UPDATING_NOTE_FROM_FAVORITE_SUCCESS_MSG)
                    }
                    is Response.Failure -> {
                        previousState to ShowSnackbar(UPDATING_NOTE_FROM_FAVORITE_FAILURE_MSG)
                    }
                }
            }
            /** End events associated with update note **/

            /** Events associated with Fetch and Filter Favorites **/
            is FavoriteEvent.OnFetchFavoritesResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        previousState.copy(fetchEntries = response) to null
                    }
                    is Response.Failure -> {
                        previousState.copy(fetchEntries = response) to null
                    }
                }
            }
            /** End events associated with Fetch and Filter Favorites **/

            /** Events associated with Import Favorites **/
            is FavoriteEvent.OnImportFavorites -> {
                previousState.copy(isVisibleImportAlert = true) to null
            }
            is FavoriteEvent.OnCancelImport -> {
                previousState.copy(isVisibleImportAlert = false) to null
            }
            is FavoriteEvent.OnConfirmImportResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        val updatedState = previousState.copy(
                            isVisibleImportAlert = false,
                            importedFavorites = response
                        )
                        updatedState to ShowSnackbar(IMPORTING_FAVORITES_SUCCESS_MSG)
                    }
                    is Response.Failure -> {
                        val updatedState = previousState.copy(
                            isVisibleImportAlert = false,
                            importedFavorites = response
                        )
                        val msg = response.exception.message ?: IMPORTING_FAVORITES_FAILURE_MSG
                        updatedState to ShowSnackbar(msg)
                    }
                }
            }
            /** End events associated with Import Favorites **/

            /** Events associated with Export Favorites **/
            is FavoriteEvent.OnExportFavorites -> {
                previousState.copy(isVisibleExportAlert = true) to null
            }
            is FavoriteEvent.OnCancelExport -> {
                previousState.copy(isVisibleExportAlert = false) to null
            }
            is FavoriteEvent.OnConfirmExportResponse -> {
                when(event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        val updatedState = previousState.copy(
                            isVisibleExportAlert = false,
                            exportedFavorites = event.response
                        )
                        updatedState to ShowSnackbar(EXPORTING_FAVORITES_SUCCESS_MSG)
                    }
                    is Response.Failure -> {
                        val updatedState = previousState.copy(
                            isVisibleExportAlert = false,
                            exportedFavorites = event.response
                        )
                        val msg = event.response.exception.message ?: EXPORTING_FAVORITES_FAILURE_MSG
                        updatedState to ShowSnackbar(msg)
                    }
                }
            }
            /** End events associated with Export Favorites **/

            else -> previousState to null
        }
    }
}