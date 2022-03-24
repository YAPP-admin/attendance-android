package com.yapp.common.util

import org.json.JSONException
import org.json.JSONObject

object AttendanceQrCodeParser {
    private const val SESSION_ID_KEY = "session_id"
    fun getSessionIdFromBarcode(value: String?): Int {
        value?.let {
            return try {
                JSONObject(value).getInt(SESSION_ID_KEY)
            } catch (e: JSONException) {
                throw Exception()
            }
        }
        throw Exception()
    }
}