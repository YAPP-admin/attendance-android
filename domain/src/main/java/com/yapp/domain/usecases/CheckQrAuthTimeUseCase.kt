package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val dateUtil: DateUtil
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        return remoteConfigRepository.getSessionList().mapCatching { sessionList: List<Session> ->
            val upComingSession = sessionList.firstOrNull { dateUtil.isUpcomingDate(it.startTime) } ?: return@mapCatching false
            val elapsedTime = dateUtil.getElapsedTimeInMinute(target = upComingSession.startTime)

            return@mapCatching elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
