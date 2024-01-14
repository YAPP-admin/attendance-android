package com.yapp.domain.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

typealias Minute = Long

class RenewDateUtil @Inject constructor() {

    fun getCurrentTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    /**
     * [startTime] 에서 [target] 까지 시간이 얼마나 흘렀는지를 나타내는 메서드
     * 양수 (+) 일 경우 [target] 으로부터 시간이 이미 흐른상태, 음수 (-)인 경우 시간이 아직 [target] 까지 시간이 흐르지 않은상태
     */
    fun getElapsedTimeInMinute(
        target: LocalDateTime,
        current: LocalDateTime = getCurrentTime()
    ): Minute {
        return Duration.between(target, current).toMinutes()
    }

    fun isUpcomingDate(
        target: LocalDateTime,
        current: LocalDateTime = getCurrentTime()
    ): Boolean {
        return current <= target
    }

    fun isPastDate(
        target: LocalDateTime,
        current: LocalDateTime = getCurrentTime()
    ): Boolean {
        return getElapsedTimeInMinute(target, current) >= 0
    }
}

class DateParser @Inject constructor() {

    fun parse(
        rawDate: String,
        formatter: DateTimeFormatter = DATE_FORMAT_WITH_TIME
    ): LocalDateTime {
        require(rawDate.isNotBlank())

        return LocalDateTime.parse(rawDate, formatter)
    }

    fun format(
        date: LocalDateTime,
        formatter: DateTimeFormatter = DATE_FORMAT_WITH_TIME
    ): String {
        return formatter.format(date)
    }

    fun format(
        date: LocalDateTime,
        format: String
    ): String {
        val formatter = DateTimeFormatter.ofPattern(format)

        return formatter.format(date)
    }

    companion object {
        private val DATE_FORMAT_WITH_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
