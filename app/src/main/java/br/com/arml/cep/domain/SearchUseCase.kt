package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.repository.FavoriteRepository
import br.com.arml.cep.model.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val favoriteRepository: FavoriteRepository
) {
    fun addToFavorite(place: Place) = with(place.cep.text) {
        favoriteRepository.addToFavorite(
            zipcode = this,
            note = Note.build(title = this, content = "")
        )
    }

    fun searchPlace(zipcode: String): Flow<Response<Place>> = try {
        val cep = Cep.build(zipcode)
        searchRepository.getPlace(cep = cep)
    } catch (e: CepException) {
        flowOf(Response.Failure(e))
    }

    fun searchPlaces(uf: String, city: String, street: String): Flow<Response<List<Place>>> =
        searchRepository.getPlaces(uf,city, street)
}