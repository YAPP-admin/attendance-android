package com.yapp.common.util

import android.content.Context
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class KakaoTalkLoginProvider @Inject constructor(
    @ActivityContext
    val context: Context
) {
    fun login(
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
}