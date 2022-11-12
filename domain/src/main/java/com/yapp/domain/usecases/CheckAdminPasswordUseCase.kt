package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CheckAdminPasswordUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    suspend fun invoke(params: Params): Result<Boolean> = kotlin.runCatching{
        val configEntity = remoteConfigRepository.getConfig()
            .getOrThrow()

        configEntity.adminPassword == params!!.inputText
    }.fold(
        onSuccess = { isPasswordEquals ->
            Result.success(isPasswordEquals)
        },
        onFailure = { exception ->
            Result.failure(exception)
        }

    )

    class Params(
        val inputText: String,
    )

}