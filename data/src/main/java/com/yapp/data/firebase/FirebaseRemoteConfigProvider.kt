package com.yapp.data.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

    override suspend fun <T> getValue(value: RemoteConfigData<T>): Flow<T> {
        return flow{
            val isActivate = firebaseRemoteConfig.fetchAndActivate().await()

            if(isActivate) {
                when (value.defaultValue) {
                    is String -> {
                        emit(firebaseRemoteConfig.getString(value.key) as T)
                    }
                    is Long -> {
                        emit(firebaseRemoteConfig.getLong(value.key) as T)
                    }
                    //todo: 우선 임시로 이렇게 두고, 자료형 생기면 계속 추가하기
                    else -> throw IllegalAccessError()
                }
            }
        }
    }
}
