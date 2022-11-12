package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.Resources
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetConfigUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    suspend fun invoke(): Result<ConfigEntity> {
        return remoteConfigRepository.getConfig()
    }

}