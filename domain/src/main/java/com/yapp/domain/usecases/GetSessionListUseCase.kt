package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class GetSessionListUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(): Result<List<Session>> {
        return remoteConfigRepository.getSessionList()
    }

}