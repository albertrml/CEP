package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.entity.PlaceEntry

data class CacheState(
    val fetchEntries: Response<List<PlaceEntry>> = Loading,
    val deleteEntry: Response<Unit> = Loading,
    val wasFiltered: Boolean = false,
    val placeForDetails: PlaceEntry? = null,
    val placeUpdate: PlaceEntry? = null,
)