package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMaginotlineTimeUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<String>>, Unit>(coroutineDispatcher) {
    override suspend fun invoke(params: Unit?): Flow<TaskResult<String>> {
        return firebaseRemoteConfig.getMaginotlineTime().toResult()
    }
}