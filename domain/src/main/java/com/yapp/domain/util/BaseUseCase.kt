package com.yapp.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


abstract class BaseUseCase<Result, in Params>() {

    abstract suspend operator fun invoke(params: Params): Result

    protected fun <T> Flow<T>.toResult() = map {
        State.Success(it) as State<T>
    }.catch { cause: Throwable ->
        emit(State.Failed(cause))
    }

}