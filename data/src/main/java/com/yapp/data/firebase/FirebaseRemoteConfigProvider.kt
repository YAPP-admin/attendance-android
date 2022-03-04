package com.yapp.data.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.data.model.ConfigModel
import com.yapp.data.model.ConfigModel.Companion.mapToEntity
import com.yapp.data.model.SessionModel
import com.yapp.data.model.SessionModel.Companion.mapToEntity
import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getMaginotlineTime(): Flow<String> {
        return flow {
            val hadActive = firebaseRemoteConfig.fetchAndActivate().await()

            if (hadActive) {
                emit(firebaseRemoteConfig.getString(RemoteConfigData.MaginotlineTime.key))
            }
        }
    }

    override suspend fun getSessionList(): Flow<List<SessionEntity>> {
        return flow {
            val hadActive = firebaseRemoteConfig.fetchAndActivate().await()

            if (hadActive) {
                emit(firebaseRemoteConfig.getString(RemoteConfigData.Config.key))
            }
        }.map { jsonString ->
            Json.decodeFromString<List<SessionModel>>(jsonString)
                .map { model -> model.mapToEntity() }
        }
    }

    override suspend fun getConfig(): Flow<ConfigEntity> {
        return flow {
            val hadActive = firebaseRemoteConfig.fetchAndActivate().await()

            if (hadActive) {
                emit(firebaseRemoteConfig.getString(RemoteConfigData.Config.key))
            }
        }.map { jsonString ->
            Json.decodeFromString<ConfigModel>(jsonString)
                .mapToEntity()
        }
    }

}
