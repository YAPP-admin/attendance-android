package com.yapp.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.data.BuildConfig
import com.yapp.data.model.SessionEntity
import com.yapp.data.util.sessionRef
import com.yapp.domain.firebase.RemoteConfigData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SessionRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : SessionRemoteDataSource {

    private val firebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
    }

    init {
        firebaseRemoteConfig.setDefaultsAsync(RemoteConfigData.defaultMaps)
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override suspend fun setSession(session: SessionEntity) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.sessionRef()
                .document(session.sessionId.toString())
                .set(session)
                .addOnSuccessListener {
                    cancellableContinuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getSession(id: Int): SessionEntity? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.sessionRef()
                .document(id.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        cancellableContinuation.resume(null)
                        return@addOnSuccessListener
                    }

                    document.toObject(SessionEntity::class.java).also { entity ->
                        cancellableContinuation.resume(entity)
                    }
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllSession(): List<SessionEntity> {
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
}
