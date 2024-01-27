package com.yapp.domain.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

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