package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer
import br.com.arml.cep.ui.screen.favorite.FavoriteEffect.OnSuccessExportFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnCancelExport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnCancelFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnCancelImport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmExportResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwantedResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmImportResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnDeleteNoteFromFavoriteResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnExportFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFetchFavoritesResponse
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnImportFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnNavigateBackToListPane
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnSelectFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnUpdateNoteFromFavoriteResponse
import br.com.arml.cep.ui.utils.UiText

class FavoriteReducer: Reducer<FavoriteState, FavoriteEvent, FavoriteEffect> {
    override fun reduce(
        previousState: FavoriteState,
        event: FavoriteEvent
    ): Pair<FavoriteState, FavoriteEffect?> {
        return when(event){
            /** Events associated with making favorite as unwanted **/
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
                    ?: return previousState to ShowSnackbar(UiText.StringResource(R.string.favorite_unwanted_selected_none))

                when (event.response) {
                    is Response.Loading ->
                        previousState to null

                    is Response.Success -> {
                        val effect = ShowSnackbar(UiText.StringResource(R.string.favorite_unfav_success, zipcode))
                        previousState.copy(
                            selectedFavoriteToUnwanted = null,
                            isVisibleUnwantedWarning = false
                        ) to effect
                    }

                    is Response.Failure -> {
                        val effect = ShowSnackbar(UiText.StringResource(R.string.favorite_unfav_failure, zipcode))
                        previousState.copy(
                            selectedFavoriteToUnwanted = null,
                            isVisibleUnwantedWarning = false
                        ) to effect
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
                        ShowSnackbar(UiText.StringResource(R.string.favorite_add_note_success, zipcode))
                    }
                    is Response.Failure -> {
                        ShowSnackbar(UiText.StringResource(R.string.favorite_add_note_failure))
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
                        val effect = ShowSnackbar(UiText.StringResource(R.string.favorite_delete_note_success, zipcode))
                        previousState to effect
                    }
                    is Response.Failure -> {
                        previousState to ShowSnackbar(UiText.StringResource(R.string.favorite_delete_note_failure))
                    }
                }
            }
            /** End events associated with Delete Note to Favorite **/

            /** Events associated with Navigate to Detail Pane **/
            is OnNavigateToDetailPane -> {
                val (address, note) = event
                val updatedState = previousState.copy(
                    selectedAddressToDetail = address,
                    selectedNoteToDetail = note,
                    selectedDataToDetail = address to note
                )
                updatedState to null
            }
            is OnNavigateBackToListPane -> {
                val updatedState = previousState.copy(
                    selectedNoteToDetail = null,
                    selectedAddressToDetail = null,
                    selectedDataToDetail = null
                )
                updatedState to null
            }
            /** End events associated with Navigate to Detail Pane **/

            /** Events associated with update note **/
            is OnUpdateNoteFromFavoriteResponse -> {
                when(event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        previousState to ShowSnackbar(UiText.StringResource(R.string.favorite_update_note_success))
                    }
                    is Response.Failure -> {
                        previousState to ShowSnackbar(UiText.StringResource(R.string.favorite_update_note_failure))
                    }
                }
            }
            /** End events associated with update note **/

            /** Events associated with Fetch and Filter Favorites **/
            is OnFetchFavoritesResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    else -> previousState.copy(places = response) to null
                }
                /*val updatedState = previousState.copy(places = event.response)
                updatedState to null*/
            }
            /** End events associated with Fetch and Filter Favorites **/

            /** Events associated with Import Favorites **/
            is OnImportFavorites -> {
                previousState.copy(isVisibleImportAlert = true) to null
            }
            is OnCancelImport -> {
                previousState.copy(isVisibleImportAlert = false) to null
            }
            is OnConfirmImportResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        val updatedState = previousState.copy(
                            isVisibleImportAlert = false,
                            importedFavorites = response
                        )
                        updatedState to ShowSnackbar(UiText.StringResource(R.string.favorite_import_success))
                    }
                    is Response.Failure -> {
                        val updatedState = previousState.copy(
                            isVisibleImportAlert = false,
                            importedFavorites = response
                        )
                        val effect = ShowSnackbar(
                            response.exception.message?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.favorite_import_failure)
                        )
                        updatedState to effect
                    }
                }
            }
            /** End events associated with Import Favorites **/

            /** Events associated with Export Favorites **/
            is OnExportFavorites -> {
                previousState.copy(isVisibleExportAlert = true) to null
            }
            is OnCancelExport -> {
                previousState.copy(isVisibleExportAlert = false) to null
            }
            is OnConfirmExportResponse -> {
                when(val response = event.response){
                    is Response.Loading -> previousState to null
                    is Response.Success -> {
                        val updatedState = previousState.copy(isVisibleExportAlert = false)
                        updatedState to OnSuccessExportFavorites(response.result)
                    }
                    is Response.Failure -> {
                        val updatedState = previousState.copy(isVisibleExportAlert = false)
                        val effect = ShowSnackbar(
                            response.exception.message?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.favorite_export_failure)
                        )
                        updatedState to effect
                    }
                }
            }
            /** End events associated with Export Favorites **/

            else -> previousState to null
        }
    }
}