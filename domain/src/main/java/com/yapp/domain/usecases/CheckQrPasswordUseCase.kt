package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckQrPasswordUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val coroutineDispatcher: DispatcherProvider,
) : BaseUseCase<Flow<TaskResult<Boolean>>, String>(coroutineDispatcher) {

    override suspend fun invoke(params: String?): Flow<TaskResult<Boolean>> {
        return firebaseRemoteConfig.getQrPassword().map { password ->
            params == password
        }.toResult()
    }
}