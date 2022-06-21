package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetConfigUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<ConfigEntity>>, Unit>(
    coroutineDispatcher) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<ConfigEntity>> {
        return firebaseRemoteConfig.getConfig().toResult()
    }

}