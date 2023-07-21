package com.yapp.domain.usecases

import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class CheckSignUpPasswordUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(inputPassword: String): Result<Boolean> {
        return remoteConfigRepository.getSignUpPassword().mapCatching { qrPassword: String ->
            inputPassword == qrPassword
        }
    }
}