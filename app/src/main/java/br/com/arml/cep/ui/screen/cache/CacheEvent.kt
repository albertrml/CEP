package br.com.arml.cep.ui.screen.cache

import br.com.arml.cep.model.domain.Place

sealed class CacheEvent {
    // Fetch
    data object OnFetchCache : CacheEvent()
    data class OnFilterByCep(val query: String) : CacheEvent()
    data object OnFilterNone : CacheEvent()

    // Delete
    data object OnDeleteAll : CacheEvent()
    data class OnDelete(val place: Place) : CacheEvent()

    // Update
    data class OnUpdate(val place: Place) : CacheEvent()

    // Select
    data class OnSelectEntryForDetails(val place: Place?) : CacheEvent()
}



