package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(): Result<Session?> {
        // 세션 당일 밤 12시까지
        return sessionRepository.getAllSession().mapCatching { sessionList ->
            val currentTime = LocalDateTime.now()
            sessionList.firstOrNull { session -> currentTime.isBefore(session.startTime) }
        }

    }
}
