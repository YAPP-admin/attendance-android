package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CheckAdminPasswordUseCase @Inject constructor(
    private val remoteConfigProvider: FirebaseRemoteConfig,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<Flow<TaskResult<Boolean>>, CheckAdminPasswordUseCase.Params>(dispatcherProvider) {


    override suspend fun invoke(params: Params?): Flow<TaskResult<Boolean>> {
        return remoteConfigProvider.getConfig()
            .map { configEntity ->
                return@map configEntity.adminPassword == params!!.inputText
            }
            .toResult()
    }

    class Params(
        val inputText: String
    )

}