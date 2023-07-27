package com.yapp.presentation.util.intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.yapp.presentation.BuildConfig

fun Context.intentToPlayStore() {
    val packageName = packageName.takeIf { BuildConfig.DEBUG.not() } ?: packageName.replace(".debug", "")
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=$packageName")
    }.also { intent ->
        ContextCompat.startActivity(this, intent, null)
    }
}
    val intent = Intent(Intent.ACTION_VIEW)

    var packageName = context.packageName

    if (BuildConfig.DEBUG) {
        packageName = packageName.replace(".debug", "")
    }

    intent.data =  Uri.parse("market://details?id=$packageName")
    ContextCompat.startActivity(context, intent, null)
}
