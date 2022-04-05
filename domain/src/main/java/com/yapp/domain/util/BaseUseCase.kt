package com.yapp.domain.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


abstract class BaseUseCase<Result, in Params : Any?>(private val coroutineDispatcher: CoroutineDispatcher) {

    abstract suspend operator fun invoke(params: Params? = null): Result

    protected fun <T> Flow<T>.toResult() = map {
        TaskResult.Success(it) as TaskResult<T>
    }.catch { cause: Throwable ->
        emit(TaskResult.Failed(cause))
    }.flowOn(coroutineDispatcher)

}