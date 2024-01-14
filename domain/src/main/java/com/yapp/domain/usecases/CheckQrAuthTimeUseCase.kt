package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.RemoteConfigRepository
import com.yapp.domain.util.RenewDateUtil
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val dateUtil: RenewDateUtil
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        return remoteConfigRepository.getSessionList().mapCatching { sessionList: List<Session> ->
            val upComingSession = sessionList.firstOrNull { dateUtil.isUpcomingDate(it.date) } ?: return@mapCatching false
            val elapsedTime = dateUtil.getElapsedTimeInMinute(target = upComingSession.date)

            return@mapCatching elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
