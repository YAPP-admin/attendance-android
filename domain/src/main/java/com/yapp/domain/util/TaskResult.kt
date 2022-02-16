package com.yapp.domain.util

sealed class TaskResult<out T> {
    class Success<T>(val data: T) : TaskResult<T>()
    class Failed(val message: String) : TaskResult<Nothing>()
}
