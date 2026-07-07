package br.com.arml.cep.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arml.core.response.Response
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S: Reducer.ViewState, E: Reducer.ViewEvent, F: Reducer.ViewEffect>(
    private val initialState: S,
    private val reducer: Reducer<S, E, F>
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _event: MutableSharedFlow<E> = MutableSharedFlow()

    private val _effect: Channel<F> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    fun sendEvent(event: E) {
        viewModelScope.launch { _event.emit(event) }
    }

    private fun sendEffect(effect: F) {
        viewModelScope.launch {
            Log.d("BaseViewModel", "sendEffect: $effect")
            _effect.send(effect)
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _event.collect { event ->
                val (newState, effect) = reducer.reduce(_state.value, event)
                _state.value = newState
                effect?.let { sendEffect(it) }
            }
        }
    }

    protected fun sendEventForEffect(event: E) {
        val (newState, effect) = reducer.reduce(_state.value, event)
        _state.value = newState
        effect?.let { sendEffect(it) }
    }

    protected fun <T> collectAction(
        flow: Flow<Response<T>>,
        onResponse: (Response<T>) -> E
    ) {
        viewModelScope.launch {
            flow.collect { response ->
                when (response) {
                    is Response.Loading -> {
                        sendEvent(onResponse(response))
                    }
                    is Response.Success,
                    is Response.Failure -> {
                        sendEventForEffect(onResponse(response))
                    }
                }
            }
        }
    }
}