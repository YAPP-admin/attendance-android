package com.yapp.presentation.ui.member.setting

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
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
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.*
import com.yapp.common.yds.*
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.TeamType
import com.yapp.presentation.R.*
import kotlinx.coroutines.delay

private fun Team.hasTeam() = type != TeamType.NONE

@Composable
fun MemberSetting(
    viewModel: MemberSettingViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToSelectTeamScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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

                MemberSettingContract.MemberSettingUiSideEffect.NavigateToSelectTeamScreen -> {
                    navigateToSelectTeamScreen()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = stringResource(id = string.member_setting_title),
                onClickBackButton = navigateToPreviousScreen
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
            .systemBarsPadding(),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            GroupInfo(uiState.generation)
            Profile(
                name = uiState.memberName,
                position = uiState.memberPosition,
                team = uiState.memberTeam,
            )
            SelectTeam(
                hasTeam = uiState.memberTeam.hasTeam(),
                onClick = { viewModel.setEvent(MemberSettingContract.MemberSettingUiEvent.OnSelectTeamButtonClicked) }
            )
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
        color = AttendanceTheme.colors.mainColors.YappOrange,
        modifier = Modifier
            .fillMaxWidth()
            .background(AttendanceTheme.colors.mainColors.YappOrangeAlpha)
            .padding(18.dp),
        textAlign = TextAlign.Center,
        style = AttendanceTypography.body1
    )
}

@Composable
private fun Profile(
    name: String,
    position: String,
    team: Team,
) {
    val subInfo = if (team.hasTeam()) {
        "$position · $team"
    } else {
        position
    }
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
            color = AttendanceTheme.colors.grayScale.Gray800,
            style = AttendanceTypography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            color = AttendanceTheme.colors.grayScale.Gray600,
            style = AttendanceTypography.body2,
            text = subInfo,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ColumnScope.SelectTeam(
    hasTeam: Boolean,
    onClick: () -> Unit,
) {
    if (hasTeam.not()) {
        YDSButtonSmall(
            modifier = Modifier
                .padding(bottom = 28.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = string.member_setting_select_team),
            state = YdsButtonState.ENABLED,
            onClick = onClick,
        )
    }
}

@Composable
private fun Divide() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
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
        val versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0)).versionName
        } else {
            @Suppress("DEPRECATION") context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = string.member_setting_version_info_text),
                color = AttendanceTheme.colors.grayScale.Gray1200,
                style = AttendanceTypography.subtitle1
            )
            Text(
                text = versionName,
                color = AttendanceTheme.colors.grayScale.Gray600,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = AttendanceTypography.subtitle1
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    viewModel.setEvent(MemberSettingContract.MemberSettingUiEvent.OnPrivacyPolicyButtonClicked)
                }
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = stringResource(id = string.member_setting_privacy_policy_text),
                color = AttendanceTheme.colors.grayScale.Gray1200,
                style = AttendanceTypography.subtitle1
            )
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.icon_chevron_right),
                contentDescription = null,
                tint = AttendanceTheme.colors.grayScale.Gray600
            )
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(id = string.member_setting_logout_text),
            color = AttendanceTheme.colors.grayScale.Gray400,
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
            color = AttendanceTheme.colors.grayScale.Gray400,
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