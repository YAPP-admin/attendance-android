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
}