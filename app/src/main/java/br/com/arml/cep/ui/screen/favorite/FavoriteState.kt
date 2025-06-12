package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry

data class FavoriteState(
    val fetchEntries: Response<List<PlaceEntry>> = Response.Loading,
    val placeForEdit: PlaceEntry? = null,
    val placeForUnwanted: PlaceEntry? = null,
    val wasFiltered: Boolean = false,
    val makeUnwanted: Response<Unit> = Response.Loading,
    val updateEntry: Response<Unit> = Response.Loading
)
