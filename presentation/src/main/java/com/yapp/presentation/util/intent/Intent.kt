package com.yapp.presentation.util.intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.yapp.presentation.BuildConfig

fun intentToPlayStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)

    var packageName = context.packageName

    if (BuildConfig.DEBUG) {
        packageName = packageName.replace(".debug", "")
    }

    intent.data =  Uri.parse("market://details?id=$packageName")
    ContextCompat.startActivity(context, intent, null)
}
