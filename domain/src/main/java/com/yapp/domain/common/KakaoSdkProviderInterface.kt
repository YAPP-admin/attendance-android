package com.yapp.domain.common

interface KakaoSdkProviderInterface {
    fun login(
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    )

    fun logout(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    )

    fun getUserAccountId(
        onSuccess: (Long) -> Unit,
        onFailed: () -> Unit,
    )

    fun validateAccessToken(onSuccess: (Long) -> Unit, onFailed: () -> Unit)
}