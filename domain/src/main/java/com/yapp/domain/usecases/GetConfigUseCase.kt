package com.yapp.domain.usecases

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class GetConfigUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    suspend operator fun invoke(): Result<ConfigEntity> {
        return remoteConfigRepository.getConfig()
    }

}