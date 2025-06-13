package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.entity.PlaceEntry

sealed class CacheEvent {
    // Fetch
    data object OnFetchCache : CacheEvent()
    data class OnFilterByCep(val cep: String) : CacheEvent()
    data object OnFilterNone : CacheEvent()

    // Delete
    data object OnDeleteAll : CacheEvent()
    data class OnDelete(val place: PlaceEntry) : CacheEvent()

    // Update
    data class OnUpdate(val place: PlaceEntry) : CacheEvent()

    // Select
    data class OnSelectEntryForDetails(val place: PlaceEntry?) : CacheEvent()
}



