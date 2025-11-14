package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.utils.PlaceFilterOption

data class FavoriteState(
    /** Create **/
    val addNoteEntry: Response<Unit> = Response.Loading,

    /** Read **/
    val fetchEntries: Response<List<Place>> = Response.Loading,
    val filterOperation: PlaceFilterOption = PlaceFilterOption.None,

    /** Update **/
    // The only data which can be updated is note. To do so, we need a note if valid idNote
    val favoriteEntry: Pair<Address, Note?>? = null,
    val noteForEdit: Note? = null,
    val updateNoteEntry: Response<Unit> = Response.Loading,
    val placeForEdit: Place? = null,

    /** Remove **/
    // To remove a favorite, we need select a place to remove before to call the operation
    val placeForUnwanted: Place? = null,
    val deleteNoteEntry: Response<Unit> = Response.Loading,

    /** Export **/
    val exportAlert: Boolean = false,
    val exportBackup: Response<String> = Response.Loading,

    /** Import **/
    val importAlert: Boolean = false,
    val importBackup: Response<Unit> = Response.Loading,
)
