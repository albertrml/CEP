package br.com.arml.cep.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CepUseCase
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: CepUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> searchCep(event.code)
            is SearchEvent.OnFavorite -> favoriteCep(event.placeEntry)
            is SearchEvent.OnClear -> cleanState()
        }
    }

    private fun favoriteCep(entry: PlaceEntry) {
        viewModelScope.launch {
            if (entry.isFavorite.value) return@launch

            val favoriteEntry = entry.copy(
                isFavorite = Favorite(true),
                note = Note.build(
                    title = entry.cep.text,
                    content = ""
                )
            )

            useCase.favoriteEntry(favoriteEntry).collect { response ->
                _state.update {
                    if (response is Response.Success) {
                        it.copy(insert = response,entry = Response.Success(favoriteEntry))
                    } else
                        it.copy(insert = response)
                }
            }
        }
    }

    private fun cleanState() {
        _state.update { SearchState() }
    }

    private fun searchCep(code: String) {
        viewModelScope.launch {
            useCase.fetchEntry(code).collectLatest { response ->
                _state.update { it.copy(entry = response) }
            }
        }
    }
}