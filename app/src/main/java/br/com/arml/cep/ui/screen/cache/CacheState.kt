package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.utils.PlaceFilterOption

data class CacheState(
    val fetchEntries: Response<List<PlaceEntry>> = Loading,
    val filterOperation: PlaceFilterOption = PlaceFilterOption.None,

    val deleteEntry: Response<Unit> = Loading,


    val placeForDetails: PlaceEntry? = null,
    val placeUpdate: PlaceEntry? = null,
)