package com.yapp.presentation.ui.member.qrcodescanner

import com.google.mlkit.vision.barcode.common.Barcode
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

object QrParserUtil {
    fun getSessionIdFromBarcode(barcode: Barcode): Int {
        barcode.rawValue?.let { jsonString ->
            return try {
                JSONObject(jsonString).getInt("session_id")
            } catch (e: JSONException) {
                throw Exception()
            }
        }
        throw Exception()
    }
}