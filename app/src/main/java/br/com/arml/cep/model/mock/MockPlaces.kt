package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.toPlaceWithNotes
fun mockPlaces(size: Int, favorite: Boolean): List<Place> {
    return List(size){ i ->
        Place(
            cep = mockCep(i),
            address = mockAddress(i),
            isFavorite = Favorite(favorite),
            notes = if(favorite) mockNotes else emptyList()
        )
    }
}
fun mockPlaceWithNotes(size: Int) = mockPlaces(size, true).map { it.toPlaceWithNotes() }

val mockUnfavoritePlaces = mockPlaces(10, false)
val mockFavoritePlaces = mockPlaces(10, true)