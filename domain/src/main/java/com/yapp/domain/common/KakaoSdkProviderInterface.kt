package com.yapp.domain.common

import kotlinx.coroutines.flow.Flow

// todo sehee coroutine 으로 변경
interface KakaoSdkProviderInterface {
    fun login(
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    )

    fun logout(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    )

    fun withdraw(): Flow<Boolean>

    fun getUserAccountId(
        onSuccess: (Long) -> Unit,
        onFailed: () -> Unit,
    )

    fun validateAccessToken(onSuccess: (Long) -> Unit, onFailed: () -> Unit)
}