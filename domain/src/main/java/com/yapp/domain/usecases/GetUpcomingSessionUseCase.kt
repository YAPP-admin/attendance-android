package com.yapp.domain.usecases

import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend fun invoke(params: Unit?): Result<SessionEntity?> {
        // 세션 당일 밤 12시까지
        return remoteConfigRepository.getSessionList().mapCatching { sessionList ->
            sessionList.firstOrNull { session -> DateUtil.isUpcomingSession(session.date) }
        }
    }

}