package com.yapp.presentation.ui.admin.createsession

import java.time.LocalDate

data class CreateSessionDateWriterDialogState(
    val year: String,
    val month: String,
    val day: String,
    val hour: String,
    val minute: String,
) {

    val isValidYear = runCatching {
        year.toInt() in 1000..9999
    }.getOrDefault(false)

    val isValidMonth = runCatching {
        month.toInt() in 1..12
    }.getOrDefault(false)

    val isValidDay =
        runCatching {
            LocalDate.of(year.toInt(), month.toInt(), day.toInt())
            true
        }.getOrDefault(false)

    val isValidHour = runCatching {
        hour.toInt() in 0..23
    }.getOrDefault(false)

    val isValidMinute = runCatching {
        minute.toInt() in 0..59
    }.getOrDefault(false)

    val isValidDate = isValidYear and
            isValidMonth and
            isValidDay and
            isValidHour and
            isValidMinute
}
