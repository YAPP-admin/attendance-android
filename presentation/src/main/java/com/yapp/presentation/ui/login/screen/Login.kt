package com.yapp.presentation.ui.login.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.presentation.R
import com.yapp.presentation.ui.login.LoginViewModel
import com.yapp.presentation.ui.login.state.LoginContract.*
@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        ConstraintLayout {
            val (skipButton, kakaoTalkLoginButton, introduce) = createRefs()

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(introduce) {
                        start.linkTo(parent.start)
                        bottom.linkTo(skipButton.top, 60.dp)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.login_attendance_introduce_text)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(skipButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(kakaoTalkLoginButton.top, 8.dp)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnSkipButtonClicked)
                }
            ) {
                Text("넘어가기")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(kakaoTalkLoginButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.login_kakao_login_text)
                )
            }
        }
    }
}
