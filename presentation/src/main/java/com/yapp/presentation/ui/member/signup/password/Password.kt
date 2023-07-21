@file:OptIn(ExperimentalComposeUiApi::class)

package com.yapp.presentation.ui.member.signup.password

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Light_Gray_200
import com.yapp.common.util.rememberKeyboardVisible
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R
import com.yapp.presentation.ui.member.signup.name.OnKeyboardNextButton
import kotlinx.coroutines.flow.collectLatest

internal const val PasswordDigit = 4

@Composable
internal fun Password(
    viewModel: PassWordViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
    onClickNextButton: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardVisible by rememberKeyboardVisible()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is PasswordContract.PasswordSideEffect.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                PasswordContract.PasswordSideEffect.KeyboardHide -> {
                    keyboardController?.hide()
                }

                PasswordContract.PasswordSideEffect.NavigateToNextScreen -> {
                    onClickNextButton()
                }

                PasswordContract.PasswordSideEffect.NavigateToPreviousScreen -> {
                    onClickBackButton()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                onClickBackButton = onClickBackButton,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsWithImePadding()
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.padding(top = 40.dp))
                Text(
                    text = stringResource(id = R.string.member_signup_password_title),
                    color = AttendanceTheme.colors.grayScale.Gray1200,
                    style = AttendanceTypography.h1
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text(
                    text = stringResource(id = R.string.member_signup_password_subtitle),
                    color = AttendanceTheme.colors.grayScale.Gray800,
                    style = AttendanceTypography.body1
                )
                Spacer(modifier = Modifier.padding(top = 28.dp))
                PasswordTextField(
                    modifier = Modifier
                        .wrapContentSize()
                        .focusRequester(focusRequester),
                    value = uiState.inputPassword,
                    onValueChange = {
                        viewModel.setEvent(PasswordContract.PasswordUiEvent.InputPassword(it))
                    },
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                AnimatedVisibility(
                    visible = uiState.isWrong,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(id = R.string.member_signup_password_wrong_code),
                            color = AttendanceTheme.colors.etcColors.EtcRed,
                            style = AttendanceTypography.subtitle1,
                        )
                    }
                }
            }
            if (keyboardVisible) {
                OnKeyboardNextButton(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enabled = uiState.inputPassword.length == PasswordDigit,
                    onClickNextBtn = {
                        viewModel.setEvent(PasswordContract.PasswordUiEvent.OnNextButtonClick)
                    },
                )
            }
        }
    }
}

@Composable
private fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            ) {
                value.forEachIndexed { index, _ ->
                    PasswordBox(
                        resId = getPasswordImageByIndex(index)
                    )
                }
                repeat(PasswordDigit - value.length) {
                    GrayBox()
                }
            }
        },
    )
}

private fun getPasswordImageByIndex(index: Int): Int {
    return when (index) {
        0 -> com.yapp.common.R.drawable.illust_password_1
        1 -> com.yapp.common.R.drawable.illust_password_2
        2 -> com.yapp.common.R.drawable.illust_password_3
        else -> com.yapp.common.R.drawable.illust_password_4
    }
}

@Composable
@NonRestartableComposable
private fun PasswordBox(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
) {
    GrayBox(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = resId),
            contentDescription = null,
        )
    }
}

@Composable
@NonRestartableComposable
private fun GrayBox(
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Light_Gray_200),
    ) {
        content?.invoke()
    }
}