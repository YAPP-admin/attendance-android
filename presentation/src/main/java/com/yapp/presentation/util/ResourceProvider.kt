package com.yapp.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getVersionCode(): Long {
        val versionCode = context.run {
            with(packageManager) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L)).longVersionCode
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        getPackageInfo(packageName, 0).longVersionCode
                    } else {
                        getPackageInfo(packageName, 0).versionCode.toLong()
                    }
                }
            }
        }

        return versionCode
    }
}
