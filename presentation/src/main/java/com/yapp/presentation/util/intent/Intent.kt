package com.yapp.presentation.util.intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.yapp.presentation.BuildConfig

fun Context.intentToPlayStore() {
    val packageName =
        packageName.takeIf { BuildConfig.DEBUG.not() } ?: packageName.replace(".debug", "")
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=$packageName")
    }.also { intent ->
        ContextCompat.startActivity(this, intent, null)
    }
}
