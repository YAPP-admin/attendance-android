package com.yapp.presentation.ui.member.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.*
import com.yapp.presentation.R.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@Composable
fun MemberSetting(
    viewModel: MemberSettingViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen -> {
                    navigateToLogin()
                }
                is MemberSettingContract.MemberSettingUiSideEffect.NavigateToPrivacyPolicyScreen -> {
                    navigateToPrivacyPolicy()
                }
                is MemberSettingContract.MemberSettingUiSideEffect.ShowToast -> {
                    toastVisible = true
                    delay(1000L)
                    toastVisible = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = string.member_setting_title),
                onClickBackButton = navigateToPreviousScreen
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GroupInfo(uiState.generation)
            Profile(uiState.memberName)
            Divide()
            MenuList(viewModel)
        }
    }

    if (uiState.loadState == MemberSettingContract.LoadState.Loading) {
        YDSProgressBar()
    } else if (uiState.loadState == MemberSettingContract.LoadState.Error) {
        YDSEmptyScreen()
    }

    AnimatedVisibility(
        visible = toastVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 110.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            YDSToast(text = stringResource(id = string.member_setting_error_message))
        }
    }
}

@Composable
private fun GroupInfo(generation: Int) {
    Text(
        text = stringResource(id = string.member_setting_generation, generation),
        color = Yapp_Orange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Yapp_OrangeAlpha)
            .padding(18.dp),
        textAlign = TextAlign.Center,
        style = AttendanceTypography.body1
    )
}

@Composable
private fun Profile(name: String) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.illust_profile),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = stringResource(id = string.member_setting_name, name),
            color = Gray_800,
            style = AttendanceTypography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Divide() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(Gray_200)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MenuList(viewModel: MemberSettingViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        YDSPopupDialog(
            title = stringResource(id = string.member_setting_withdraw_dialog_title_text),
            content = stringResource(id = string.member_setting_withdraw_dialog_content_text),
            negativeButtonText = stringResource(id = string.member_setting_withdraw_dialog_negative_button),
            positiveButtonText = stringResource(id = string.member_setting_withdraw_dialog_positive_button),
            onClickPositiveButton = {
                viewModel.setEvent(MemberSettingContract.MemberSettingUiEvent.OnWithdrawButtonClicked)
                showDialog = !showDialog
            },
            onClickNegativeButton = { showDialog = !showDialog },
            onDismiss = { showDialog = !showDialog }
        )
    }


    Column(
        modifier = Modifier.padding(top = 28.dp, bottom = 28.dp)
    ) {
        val context = LocalContext.current
        val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = string.member_setting_version_info_text),
                color = Gray_1200,
                style = AttendanceTypography.subtitle1
            )
            Text(
                text = versionName,
                color = Gray_600,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = AttendanceTypography.subtitle1
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.setEvent(MemberSettingContract.MemberSettingUiEvent.OnPrivacyPolicyButtonClicked)
                }
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = string.member_setting_privacy_policy_text),
                color = Gray_1200,
                style = AttendanceTypography.subtitle1
            )
            Image(
                painter = painterResource(id = R.drawable.icon_chevron_right),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically),
                alignment = Alignment.CenterEnd
            )
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(id = string.member_setting_logout_text),
            color = Gray_400,
            style = AttendanceTypography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.setEvent(MemberSettingContract.MemberSettingUiEvent.OnLogoutButtonClicked)
                }
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Text(
            text = stringResource(id = string.member_setting_withdraw_text),
            color = Gray_400,
            style = AttendanceTypography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDialog = !showDialog
                }
                .padding(horizontal = 24.dp, vertical = 16.dp),
        )
    }
}