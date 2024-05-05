package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(): Result<Session?> {
        return sessionRepository.getAllSession().mapCatching { sessionList ->
            val currentTime = LocalDateTime.now()

            // 세션 당일 밤 12시까지 노출을 위해, 현재 시간의 일자와 일치하는 세션이 있는경우 Early Return
            sessionList
                .firstOrNull { session ->
                    session.startTime.year == currentTime.year &&
                        session.startTime.month == currentTime.month &&
                        session.startTime.dayOfMonth == currentTime.dayOfMonth
                }?.let { todaySession ->
                    return@mapCatching todaySession
                }

            sessionList.firstOrNull { session -> currentTime.isBefore(session.startTime) }
                ?.let { nextSession ->
                    return@mapCatching nextSession
                }
        }
    }
}
