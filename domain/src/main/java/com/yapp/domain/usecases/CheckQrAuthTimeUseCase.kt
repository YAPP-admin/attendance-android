package com.yapp.domain.usecases

import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject


class CheckQrAuthTimeUseCase @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase
) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    suspend operator fun invoke(): Result<Boolean> {
        val currentTime = LocalDateTime.now()

        return getUpcomingSessionUseCase().mapCatching { upComingSession ->
            if (upComingSession == null) {
                return@mapCatching false
            }

            val elapsedTimeInMinutes = Duration.between(upComingSession.startTime, currentTime).toMinutes()

            elapsedTimeInMinutes in BEFORE_5_MINUTE..AFTER_30_MINUTE
        }
    }

}
