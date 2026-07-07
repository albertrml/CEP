package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.cep.ui.common.Reducer

data class FavoriteState(
    /** Delete **/
    val selectedFavoriteToUnwanted: Place? = null,
    val isVisibleUnwantedWarning: Boolean = false,

    /** Navigate **/
    val selectedDataToDetail: Pair<Address, Note?>? = null,
    val selectedNoteToDetail: Note? = null,
    val selectedAddressToDetail: Address? = null,

    /** Fetch and Filter **/
    val places: Response<List<Place>> = Response.Loading,

    /** Import and Export **/
    val importedFavorites: Response<Unit> = Response.Loading,
    val isVisibleImportAlert: Boolean = false,
    val isVisibleExportAlert: Boolean = false,
): Reducer.ViewState