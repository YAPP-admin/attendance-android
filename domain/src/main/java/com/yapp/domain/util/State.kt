package com.yapp.domain.util

sealed class State<out T> {
    class Success<T>(val data: T) : State<T>()
    class Failed(val throwable: Throwable) : State<Nothing>()
}
