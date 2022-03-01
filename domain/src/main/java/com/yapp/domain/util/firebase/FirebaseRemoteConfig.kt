package com.yapp.domain.util.firebase

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfig {
    suspend fun getMaginotlineTime(): Flow<String>
    suspend fun getSessionList(): Flow<List<SessionEntity>>
    suspend fun getConfig(): Flow<ConfigEntity>
    suspend fun <T> getValue(value: RemoteConfigData<T>): Flow<Any>
}