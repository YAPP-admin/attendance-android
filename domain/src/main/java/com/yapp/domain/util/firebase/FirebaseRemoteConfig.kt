package com.yapp.domain.util.firebase

import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfig {
    fun <T> getValue(value: RemoteConfigData<T>, callback: (T) -> Unit)
}