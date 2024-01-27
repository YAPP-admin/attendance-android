package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dateUtil: DateUtil
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        return sessionRepository.getAllSession().mapCatching { sessionList: List<Session> ->
            val upComingSession = sessionList.firstOrNull { session ->
                with(dateUtil) { currentTime isBeforeFrom session.startTime }
            } ?: return@mapCatching false
            val elapsedTime = with(dateUtil) { currentTime elapsedFrom upComingSession.startTime }

            return@mapCatching elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
