package br.com.arml.cep.ui.screen.cep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.cep.domain.CepUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CepViewModel @Inject constructor(
    private val useCase: CepUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(CepState())
    val state = _state.asStateFlow()

    fun onEvent( event: CepEvent ){
        when(event){
            is CepEvent.SearchCep -> searchCep(event.code)
            is CepEvent.ClearCep -> cleanState()
        }
    }

    private fun cleanState(){
        _state.update { CepState() }
    }

    private fun searchCep(code: String){
        viewModelScope.launch {
            useCase.fetchEntry(code).collect { response ->
                _state.update { it.copy(entry = response) }
            }
        }
    }
}