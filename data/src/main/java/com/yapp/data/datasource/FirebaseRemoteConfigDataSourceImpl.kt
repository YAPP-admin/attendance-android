package com.yapp.data.datasource

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.data.BuildConfig
import com.yapp.data.model.ConfigEntity
import com.yapp.data.model.SessionEntity
import com.yapp.data.model.TeamEntity
import com.yapp.data.model.VersionEntity
import com.yapp.domain.firebase.RemoteConfigData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseRemoteConfigDataSourceImpl @Inject constructor() : FirebaseRemoteConfigDataSource {

    private val firebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
    }

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
                        Json.decodeFromString<List<SessionEntity>>(jsonString)
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
                        Json.decodeFromString<ConfigEntity>(jsonString)
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
                        Json.decodeFromString<List<TeamEntity>>(jsonString)
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

    override suspend fun getVersionInfo(): VersionEntity {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val version = firebaseRemoteConfig.getString(RemoteConfigData.VersionInfo.key)
                    .let { jsonString ->
                        Json.decodeFromString<VersionEntity>(jsonString)
                    }

                cancellableContinuation.resume(version, null)
            }
        }
    }

    override suspend fun getSignUpPassword(): String {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val password = firebaseRemoteConfig.getString(RemoteConfigData.SignUpPassword.key)
                cancellableContinuation.resume(password, null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }

    override suspend fun getSessionPassword(): String {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val sessionPassword = firebaseRemoteConfig.getString(RemoteConfigData.SessionPassword.key)
                cancellableContinuation.resume(sessionPassword, null)
            }.addOnFailureListener { exception ->
                cancellableContinuation.resumeWithException(exception)
            }
        }
    }


}
