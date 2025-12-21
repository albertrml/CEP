package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.common.Reducer

sealed class FavoriteEvent: Reducer.ViewEvent {
    /** Events associated with making favorite as unwanted **/
    data class OnSelectFavoriteToUnwanted(val place: Place) : FavoriteEvent()
    data object OnCancelFavoriteToUnwanted : FavoriteEvent()
    data class OnConfirmFavoriteToUnwanted(val place: Place) : FavoriteEvent()
    data class OnConfirmFavoriteToUnwantedResponse(val response: Response<Unit>) : FavoriteEvent()
    /** End events associated with making favorite as unwanted **/

    /** Events associated with Add Note to Favorite **/
    data class OnAddNoteToFavorite(val cep: Cep, val note: Note) : FavoriteEvent()
    data class OnAddNoteToFavoriteResponse(val response: Response<Unit>, val zipcode: String) : FavoriteEvent()
    /** End events associated with Add Note to Favorite **/

    /** Events associated with Delete Note from Favorite **/
    data class OnDeleteNoteFromFavorite(val noteWithCep: Pair<Cep, Note>) : FavoriteEvent()
    data class OnDeleteNoteFromFavoriteResponse(val response: Response<String>) : FavoriteEvent()
    /** End events associated with Delete Note to Favorite **/

    /** Events associated with update note **/
    data class OnUpdateNoteFromFavorite(val note: Note) : FavoriteEvent()
    data class OnUpdateNoteFromFavoriteResponse(val response: Response<Unit>) : FavoriteEvent()
    /** End events associated with update note **/

    /** Events associated with Navigate between Pane **/
    data class OnNavigateToDetailPane(val note: Note, val address: Address) : FavoriteEvent()
    data object OnNavigateBackToListPane : FavoriteEvent()
    /** End events associated with Navigate between Pane **/

    /** Events associated with Fetch and Filter Favorites **/
    data class OnFetchFavoritesResponse(val response: Response<List<Place>>) : FavoriteEvent()
    data class OnFilterByCep(val cep: String) : FavoriteEvent()
    data class OnFilterByTitle(val title: String) : FavoriteEvent()
    data object OnFilterNone : FavoriteEvent()
    /** End events associated with Fetch and Filter Favorites **/

    /** Events associated with Import Favorites **/
    data object OnImportFavorites : FavoriteEvent()
    data class OnConfirmImport(val json: String) : FavoriteEvent()
    data class OnConfirmImportResponse(val response: Response<Unit>) : FavoriteEvent()
    data object OnCancelImport : FavoriteEvent()
    /** End events associated with Import Favorites **/

    /** Events associated with Export Favorites **/
    data object OnExportFavorites : FavoriteEvent()
    data object OnConfirmExport : FavoriteEvent()
    data class OnConfirmExportResponse(val response: Response<String>) : FavoriteEvent()
    data object OnCancelExport : FavoriteEvent()
    /** End events associated with Export Favorites **/
}