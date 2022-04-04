package com.yapp.domain.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    // 현재 시각이 session 시작 시간에서 몇 분 지났는지 계산
    // 세션 시작 시간이 지난 경우 양수(+)
    // 세션 시작이 아직 남은 경우 음수(-)
    fun getElapsedTime(sessionDateStr: String): Long {
        val currentDate = System.currentTimeMillis()
        val sessionDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(sessionDateStr).time
        return (currentDate - sessionDate) / 1000 / 60
    }

    // 지난 세션인지 확인
    // 세션 다음날부터 true
    fun isPastSession(sessionDateStr: String): Boolean {
        val currentDate = System.currentTimeMillis()
        // TODO 지금 당일부터 true인걸로 되어있음
        val sessionDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(sessionDateStr).time
        return currentDate > sessionDate
    }
}