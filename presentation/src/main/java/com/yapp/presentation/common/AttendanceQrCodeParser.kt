package com.yapp.presentation.common

import com.yapp.presentation.model.QrInformation
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object AttendanceQrCodeParser {
    fun getSessionInformationFromQrcode(value: String?): QrInformation {
        return value?.let { Json.decodeFromString<QrInformation>(it) } ?: throw Exception()
    }
}