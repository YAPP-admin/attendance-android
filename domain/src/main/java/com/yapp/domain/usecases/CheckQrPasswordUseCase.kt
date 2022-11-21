package com.yapp.domain.usecases

import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class CheckQrPasswordUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(inputPassword: String): Result<Boolean> {
        return remoteConfigRepository.getQrPassword().mapCatching { qrPassword: String ->
            inputPassword == qrPassword
        }
    }

}