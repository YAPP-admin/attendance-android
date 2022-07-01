package com.yapp.common.util

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yapp.domain.common.KakaoSdkProviderInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class KakaoSdkProvider @Inject constructor(
    @ApplicationContext
    val context: Context
) : KakaoSdkProviderInterface {

    override fun login(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    ) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    // 의도적인 로그인 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        onFailed()
                        return@loginWithKakaoTalk
                    }

                    loginWithKakaoAccount(onSuccess, onFailed)
                } else if (token != null) {
                    onSuccess()
                }
            }
        } else {
            loginWithKakaoAccount(onSuccess, onFailed)
        }
    }

    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
    private fun loginWithKakaoAccount(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                onFailed()
            } else if (token != null) {
                onSuccess()
            }
        }
    }

    override fun logout(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    ) {
        UserApiClient.instance.logout { error ->
            error?.let {
                onFailed()
            } ?: run {
                onSuccess()
            }
        }
    }

    override fun withdraw(onSuccess: () -> Unit, onFailed: () -> Unit) {
        return UserApiClient.instance.unlink { error ->
            error?.let {
                onFailed()
            } ?: run {
                onSuccess()
            }
        }
    }

    override fun validateAccessToken(onSuccess: (Long) -> Unit, onFailed: () -> Unit) {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error == null) {
                    // 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    getUserAccountId(onSuccess, onFailed)
                } else {
                    onFailed()
                }
            }
        } else {
            onFailed()
        }
    }

    override fun getUserAccountId(
        onSuccess: (Long) -> Unit,
        onFailed: () -> Unit,
    ) {
        UserApiClient.instance.me { user, error ->
            error?.let {
                onFailed()
            }
            if (user != null) {
                user.id?.let { id ->
                    onSuccess(id)
                } ?: run {
                    onFailed()
                }
            }
        }
    }
}