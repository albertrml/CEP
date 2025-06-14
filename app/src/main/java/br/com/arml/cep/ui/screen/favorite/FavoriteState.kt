package br.com.arml.cep.ui.screen.favorite

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.utils.FavoriteFilterOption

data class FavoriteState(
    val fetchEntries: Response<List<PlaceEntry>> = Response.Loading,
    val placeForEdit: PlaceEntry? = null,
    val placeForUnwanted: PlaceEntry? = null,
    val updateEntry: Response<Unit> = Response.Loading,
    val filterOperation: FavoriteFilterOption = FavoriteFilterOption.None
)
