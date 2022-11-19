package com.yapp.data.datasource

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.data.model.ConfigModel
import com.yapp.data.model.ConfigModel.Companion.mapToEntity
import com.yapp.data.model.SessionModel
import com.yapp.data.model.SessionModel.Companion.mapToEntity
import com.yapp.data.model.TeamModel
import com.yapp.data.model.TeamModel.Companion.mapToEntity
import com.yapp.domain.firebase.RemoteConfigData
import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class FirebaseRemoteConfigDataSourceImpl @Inject constructor() : FirebaseRemoteConfigDataSource {

    private val firebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }

    init {
        firebaseRemoteConfig.setDefaultsAsync(RemoteConfigData.defaultMaps)
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override suspend fun getMaginotlineTime(): String {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val maginotlineTime = firebaseRemoteConfig.getString(RemoteConfigData.MaginotlineTime.key)

                cancellableContinuation.resume(value = maginotlineTime, onCancellation = null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun getSessionList(): List<SessionEntity> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val entities = firebaseRemoteConfig.getString(RemoteConfigData.SessionList.key)
                    .let { jsonString ->
                        Json.decodeFromString<List<SessionModel>>(jsonString)
                            .map { model -> model.mapToEntity() }
                    }

                cancellableContinuation.resume(value = entities, onCancellation = null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun getConfig(): ConfigEntity {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val config = firebaseRemoteConfig.getString(RemoteConfigData.Config.key)
                    .let { jsonString ->
                        Json.decodeFromString<ConfigModel>(jsonString)
                            .mapToEntity()
                    }

                cancellableContinuation.resume(value = config, onCancellation = null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun getTeamList(): List<TeamEntity> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val entities = firebaseRemoteConfig.getString(RemoteConfigData.AttendanceSelectTeams.key)
                    .let { jsonString ->
                        Json.decodeFromString<List<TeamModel>>(jsonString)
                            .map { it.mapToEntity() }
                    }

                cancellableContinuation.resume(value = entities, onCancellation = null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun getQrPassword(): String {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val password = firebaseRemoteConfig.getString(RemoteConfigData.QrPassword.key)
                cancellableContinuation.resume(password, null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun shouldShowGuestButton(): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val shouldShowGuest = firebaseRemoteConfig.getString(RemoteConfigData.ShouldShowGuestButton.key)
                    .toBooleanStrict()

                cancellableContinuation.resume(shouldShowGuest)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

}