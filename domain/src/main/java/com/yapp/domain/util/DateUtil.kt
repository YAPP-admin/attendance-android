package com.yapp.domain.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    private const val oneSecond = 1000
    private const val oneMinute = 60 * oneSecond
    private const val oneHour = 60 * oneMinute
    private const val oneDay = 24 * oneHour

    // 현재 시각이 session 시작 시간에서 몇 분 지났는지 계산
    // 세션 시작 시간이 지난 경우 양수(+)
    // 세션 시작이 아직 남은 경우 음수(-)
    fun getElapsedTime(sessionDateStr: String): Long {
        val currentDate = System.currentTimeMillis()
        val sessionDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(sessionDateStr).time
        return (currentDate - sessionDate) / oneMinute
    }

    // 다가오는 세션인지 확인
    // 세션 날짜가 오늘 이후이면 true
    fun isUpcomingSession(sessionDateStr: String): Boolean {
        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(System.currentTimeMillis())
        val sessionDate = sessionDateStr.substring(0, 10)
        return currentDate <= sessionDate
    }

    // 지난 세션인지 확인
    // 세션 시작 시간이 지나면 true
    fun isPastSession(sessionDateStr: String): Boolean {
        return getElapsedTime(sessionDateStr) >= 0
    }
}