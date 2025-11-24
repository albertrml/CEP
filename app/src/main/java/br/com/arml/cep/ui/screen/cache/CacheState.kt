package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.utils.PlaceFilterOption

data class CacheState(
    val fetchEntries: Response<List<Place>> = Loading,
    val filterOperation: PlaceFilterOption = PlaceFilterOption.None,
    val addNoteEntry: Response<String> = Loading,
    val deleteEntry: Response<Unit> = Loading,
    val placeForDetails: Place? = null,
    val placeUpdate: Place? = null
)