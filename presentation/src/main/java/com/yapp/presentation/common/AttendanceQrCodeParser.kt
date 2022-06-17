package com.yapp.presentation.common

import com.yapp.presentation.model.QrInformation
import org.json.JSONException
import org.json.JSONObject

object AttendanceQrCodeParser {
    private const val SESSION_ID_KEY = "session_id"
    private const val QR_PASSWORD = "password"

    fun getSessionInformationFromBarcode(value: String?): QrInformation {
        value?.let {
            return try {
                val jsonObject = JSONObject(value)
                QrInformation(
                    sessionId = jsonObject.getInt(SESSION_ID_KEY),
                    password = jsonObject.getString(QR_PASSWORD)
                )
            } catch (e: JSONException) {
                throw Exception()
            }
        }
        throw Exception()
    }
}