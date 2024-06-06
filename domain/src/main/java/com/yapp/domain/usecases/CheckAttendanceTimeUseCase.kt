package com.yapp.domain.usecases

import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject


class CheckAttendanceTimeUseCase @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase
) {

    companion object {
        private const val BEFORE_10_MINUTE = -10
        private const val AFTER_120_MINUTE = 120
    }

    suspend operator fun invoke(): Result<Boolean> {
        val currentTime = LocalDateTime.now()

        return getUpcomingSessionUseCase().mapCatching { upComingSession ->
            if (upComingSession == null) {
                return@mapCatching false
            }

            val elapsedTimeInMinutes = Duration.between(upComingSession.startTime, currentTime).toMinutes()

            elapsedTimeInMinutes in BEFORE_10_MINUTE..AFTER_120_MINUTE
        }
    }

}
