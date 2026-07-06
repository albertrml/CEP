package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.toPlaceWithNotes
import br.com.arml.cep.model.entity.relation.toModel

fun mockPlaces(size: Int, favorite: Boolean): List<Place> {
    return List(size){ i ->
        Place(
            cep = mockCep(i),
            address = mockAddress(i),
            isFavorite = Favorite(favorite),
            notes = if(favorite) generateMockNotes(i) else emptyList()
        )
    }
}

fun mockPlaceWithNotes(size: Int) = mockPlaces(size, true).map { it.toPlaceWithNotes() }
val mockUnfavoritePlaceEntities = mockPlaces(10, false).map { it.toPlaceWithNotes() }
val mockUnfavoritePlaces = mockUnfavoritePlaceEntities.map { it.toModel() }
val mockFavoritePlaceEntities = mockPlaces(10, true).map { it.toPlaceWithNotes() }
val mockFavoritePlaces = mockFavoritePlaceEntities.map { it.toModel() }