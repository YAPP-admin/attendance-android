package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        val currentTime = LocalDateTime.now()

        return sessionRepository.getAllSession().mapCatching { sessionList: List<Session> ->
            val upComingSession = sessionList.firstOrNull { session -> currentTime.isBefore(session.startTime) } ?: return@mapCatching false
            val elapsedTimeInMinutes = Duration.between(upComingSession.startTime, currentTime).toMinutes()

            return@mapCatching elapsedTimeInMinutes in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
