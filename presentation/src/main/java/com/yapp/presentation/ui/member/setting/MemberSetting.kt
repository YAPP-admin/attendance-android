package com.yapp.presentation.ui.member.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R.*

@Composable
fun MemberSetting(viewModel: MemberSettingViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = string.member_setting_title),
                onClickBackButton = {}
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box()
            Profile()
            Divide()
            MenuList()
        }
    }
}

@Composable
private fun Box() {
    Text(
        text = "YAPP 20기 회원",
        color = Yapp_Orange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Yapp_OrangeAlpha)
            .padding(18.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Profile() {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.illust_setting),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "YAPP@gmail.com 님",
            color = Gray_800,
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

@Composable
private fun MenuList() {
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
            )
            Text(
                text = versionName,
                color = Gray_600,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = string.member_setting_privacy_policy_text),
                color = Gray_1200,
            )
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_right),
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Text(
            text = stringResource(id = string.member_setting_withdraw_text),
            color = Gray_400,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}