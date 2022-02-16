package com.yapp.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d
abstract class BaseViewModel<S : UiState, A : UiSideEffect, E : UiEvent>(
    initialState: S,
) : ViewModel() {

    private val _uiState = MutableStateFlow<S>(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableEventFlow<E>()
    val event = _event.asEventFlow()

    /**
     * `Channel` replicate SingleLiveEvent behavior.
     */
    private val _effect: Channel<A> = Channel()
    val effect = _effect.receiveAsFlow()

    // Get current state
    private val currentState: S
        get() = _uiState.value

    protected suspend fun setEvent(event: E) {
        _event.emit(event)
    }

    protected abstract fun handleEvent(event : E)

    protected fun setState(reduce: S.() -> S) {
        val state = currentState.reduce()
        _uiState.value = state
    }

    protected fun setEffect(builder: () -> A) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    protected suspend fun <T> Flow<TaskResult<T>>.collectWithCallback(onSuccess: suspend (T) -> Unit, onFailed: suspend (String) -> Unit) {
        collect { result ->
            when (result) {
                is TaskResult.Success -> {
                    onSuccess.invoke(result.data)
                }

                is TaskResult.Failed -> {
                    onFailed.invoke(result.message)
                }
            }
        }
    }
}
