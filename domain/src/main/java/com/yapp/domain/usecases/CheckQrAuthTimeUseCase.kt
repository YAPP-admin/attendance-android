package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    companion object {
        private const val BEFORE_10_MINUTE = -10
        private const val AFTER_120_MINUTE = 120
    }

    suspend operator fun invoke(): Result<Boolean> {
        return remoteConfigRepository.getSessionList().mapCatching { sessionList: List<Session> ->
            val upComingSession = sessionList.firstOrNull { DateUtil.isUpcomingSession(it.date) } ?: return@mapCatching false
            val elapsedTime = DateUtil.getElapsedTime(upComingSession.date)

            return@mapCatching elapsedTime in BEFORE_10_MINUTE..AFTER_120_MINUTE
        }
    }

}
