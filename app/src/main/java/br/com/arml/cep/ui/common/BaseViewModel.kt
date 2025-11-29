package br.com.arml.cep.ui.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<
        State : Reducer.ViewState,
        Event : Reducer.ViewEvent,
        Effect : Reducer.ViewEffect
>(
    initialState: State,
    private val reducer: Reducer<State, Event, Effect>
): ViewModel(){

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel(capacity = Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init { _state.tryEmit(initialState) }

    fun sendEvent(event: Event){
        val (newState, _ ) = reducer.reduce(_state.value, event)
        _state.update { newState }
    }

    fun sendEventForEffect(event: Event){
        val (newState, effect) = reducer.reduce(_state.value, event)
        _state.update { newState }
        effect?.let { _effect.trySend(it) }
    }
}