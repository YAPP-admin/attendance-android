package com.yapp.data.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import javax.inject.Inject

class FirebaseRemoteConfigProvider @Inject constructor() : FirebaseRemoteConfig {
    //firebase remote config test
    private val firebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    init {
        firebaseRemoteConfig.setDefaultsAsync(RemoteConfigData.defaultMaps)
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun <T> getValue(value: RemoteConfigData<T>, callback: (T) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                when (value.defaultValue) {
                    is String -> {
                        callback(firebaseRemoteConfig.getString(value.key) as T)
                    }
                    is Long -> {
                        callback(firebaseRemoteConfig.getLong(value.key) as T)
                    }
                    //todo: 우선 임시로 이렇게 두고, 자료형 생기면 계속 추가하기
                    else -> throw IllegalAccessError()
                }
            }
        }
    }
}
