package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dateUtil: DateUtil
) {

    suspend operator fun invoke(): Result<Session?> {
        // 세션 당일 밤 12시까지
        return sessionRepository.getAllSession().mapCatching { sessionList ->
            sessionList.firstOrNull { session -> dateUtil.isUpcomingDateFromCurrentTime(session.startTime) }
        }
    }
}
