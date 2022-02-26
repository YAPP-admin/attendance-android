package com.yapp.domain.util.firebase

interface FirebaseRemoteConfig {
    fun <T> getValue(value: RemoteConfigData<T>, callback: (T) -> Unit)
}