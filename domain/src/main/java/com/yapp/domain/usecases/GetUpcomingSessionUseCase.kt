package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.RenewDateUtil
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val dateUtil: RenewDateUtil
) {

    suspend operator fun invoke(): Result<Session?> {
        // 세션 당일 밤 12시까지
        return remoteConfigRepository.getSessionList().mapCatching { sessionList ->
            sessionList.firstOrNull { session -> dateUtil.isUpcomingDate(session.date) }
        }
    }
}
