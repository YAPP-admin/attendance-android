package com.yapp.domain.util

import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

typealias Minute = Long

class DateUtil @Inject constructor() {

    val currentTime: LocalDateTime
        get() = LocalDateTime.now()

    /**
     * [from] 에서 [this] 까지 시간이 얼마나 흘렀는지를 나타내는 메서드
     * 양수 (+) 일 경우 [from] 으로부터 시간이 이미 흐른상태, 음수 (-)인 경우 시간이 아직 [from] 까지 시간이 흐르지 않은상태
     */
    infix fun LocalDateTime.elapsedFrom(from: LocalDateTime): Minute {
        return Duration.between(from, this).toMinutes()
    }

    infix fun LocalDateTime.isBeforeFrom(from: LocalDateTime): Boolean {
        return this.isBefore(from)
    }

    infix fun LocalDateTime.isAfterFrom(from: LocalDateTime): Boolean {
        return this.isAfter(from)
    }
}