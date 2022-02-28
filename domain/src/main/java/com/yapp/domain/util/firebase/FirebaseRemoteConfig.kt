package com.yapp.domain.util.firebase

import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfig {
    suspend fun <T> getValue(value: RemoteConfigData<T>): Flow<T>
}