package com.yapp.presentation.ui.member.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar

@Composable
fun MemberSetting(viewModel: MemberSettingViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                title = "설정",
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
                .padding(16.dp),
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
        modifier = Modifier.padding(top = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "버전 정보",
                color = Gray_1200,
            )
            Text(
                text = "1.01.1",
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
                text = "개인정보 처리방침",
                color = Gray_1200,
            )
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().align(Alignment.CenterVertically),
                alignment = Alignment.CenterEnd
            )
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = "로그아웃",
            color = Gray_400,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Text(
            text = "회원탈퇴",
            color = Gray_400,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}