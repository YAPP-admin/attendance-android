package com.yapp.domain.usecases

import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class GetSessionListUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend fun invoke(params: Unit?): Result<List<SessionEntity>> {
        return remoteConfigRepository.getSessionList()
    }

}