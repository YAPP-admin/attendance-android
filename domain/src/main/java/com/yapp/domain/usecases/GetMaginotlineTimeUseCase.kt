package com.yapp.domain.usecases

import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class GetMaginotlineTimeUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend fun invoke(): Result<String> {
        return remoteConfigRepository.getMaginotlineTime()
    }

}