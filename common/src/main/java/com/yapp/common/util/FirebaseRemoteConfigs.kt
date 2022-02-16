package com.yapp.common.util

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class FirebaseRemoteConfigs {
    //firebase remote config test
    private val firebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    init {
        firebaseRemoteConfig.setDefaultsAsync(RemoteConfigData.defaultMaps)
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun <T> getValue(value: RemoteConfigData<T>, callback: (T) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                when (value.defaultValue) {
                    is String -> {
                        callback(firebaseRemoteConfig.getString(value.key) as T)
                    }
                    is Long -> {
                        callback(firebaseRemoteConfig.getLong(value.key) as T)
                    }
                    else -> throw IllegalAccessError()
                }
            }
        }
    }
}

sealed class RemoteConfigData<T> {
    abstract val key: String
    abstract val defaultValue: T

    object MaginotlineTime : RemoteConfigData<String>() {
        override val key: String = ATTENDANCE_MAGINOTLINE_TIME
        override val defaultValue: String = "fail"
    }

    companion object {
        const val ATTENDANCE_MAGINOTLINE_TIME = "attendance_maginotline_time"

        val defaultMaps = mapOf(
            MaginotlineTime.defaultValue to MaginotlineTime.key
        )
    }
}
