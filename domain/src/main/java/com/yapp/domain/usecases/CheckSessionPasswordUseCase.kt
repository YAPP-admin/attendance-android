package com.yapp.domain.usecases

import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class CheckSessionPasswordUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(inputPassword: String): Result<Boolean> {
        return remoteConfigRepository.getSessionPassword().mapCatching { sessionPassword: String ->
            inputPassword == sessionPassword
        }
    }
}