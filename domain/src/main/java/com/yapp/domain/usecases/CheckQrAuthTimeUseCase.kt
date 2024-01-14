package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        return sessionRepository.getAllSession().mapCatching { sessionList: List<Session> ->
            val upCommingSession = sessionList.firstOrNull { DateUtil.isUpcomingSession(it.startTime) } ?: return@mapCatching false
            val elapsedTime = DateUtil.getElapsedTime(upCommingSession.startTime)

            return@mapCatching elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
