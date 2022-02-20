package com.yapp.presentation.ui.login.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yapp.presentation.ui.login.LoginViewModel
import com.yapp.presentation.ui.login.state.LoginContract.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            Button(onClick = {
                viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
            }) {
                Text("카카오 로그인")
            }
        }

        val context = LocalContext.current

        scope.launch {
            viewModel.effect.collect {
                when (it) {
                    is LoginUiSideEffect.ShowKakaoTalkLoginPage -> {

                        // 로그인 조합 예제
// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
                        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                            if (error != null) {
                                Log.e("####", "카카오계정으로 로그인 실패", error)
                            } else if (token != null) {
                                Log.i("####", "카카오계정으로 로그인 성공 ${token.accessToken}")
                            }
                        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                                if (error != null) {
                                    Log.e("####", "카카오톡으로 로그인 실패", error)

                                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                        return@loginWithKakaoTalk
                                    }

                                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                                } else if (token != null) {
                                    Log.i("####", "카카오톡으로 로그인 성공 ${token.accessToken}")
                                }
                            }
                        } else {
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                        }
                    }
                }
            }
        }
    }
}
