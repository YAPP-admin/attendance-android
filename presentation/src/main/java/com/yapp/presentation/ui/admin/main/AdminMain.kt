package com.yapp.presentation.ui.admin.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSButtonSmall
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.YdsButtonState
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.presentation.R.string
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiEvent
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiSideEffect
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiState

@Composable
fun AdminMain(
    viewModel: AdminMainViewModel = hiltViewModel(),
    navigateToAdminTotalScore: (Int) -> Unit,
    navigateToCreateSession: () -> Unit,
    navigateToManagement: (Int, String) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AdminMainUiSideEffect.NavigateToAdminTotalScore -> navigateToAdminTotalScore(
                    effect.lastSessionId
                )

                is AdminMainUiSideEffect.NavigateToCreateSession -> navigateToCreateSession()

                is AdminMainUiSideEffect.NavigateToManagement -> navigateToManagement(
                    effect.sessionId,
                    effect.sessionTitle
                )

                is AdminMainUiSideEffect.NavigateToLogin -> navigateToLogin()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
    ) { contentPadding ->
        when (uiState.loadState) {
            AdminMainUiState.LoadState.Loading -> YDSProgressBar()
            AdminMainUiState.LoadState.Idle -> AdminMainScreen(
                modifier = Modifier.padding(contentPadding),
                uiState = uiState,
                onUserScoreCardClicked = {
                    viewModel.setEvent(
                        AdminMainUiEvent.OnUserScoreCardClicked(uiState.lastSessionId)
                    )
                },
                onCreateSessionClicked = {
                     viewModel.setEvent(
                         AdminMainUiEvent.OnCreateSessionClicked
                     )
                },
                onSessionClicked = { sessionId, sessionTitle ->
                    viewModel.setEvent(
                        AdminMainUiEvent.OnSessionClicked(sessionId, sessionTitle)
                    )
                },
                onLogoutClicked = {
                    viewModel.setEvent(
                        AdminMainUiEvent.OnLogoutClicked
                    )
                }
            )

            AdminMainUiState.LoadState.Error -> YDSEmptyScreen()
        }
    }
}

@Composable
fun AdminMainScreen(
    modifier: Modifier,
    uiState: AdminMainUiState,
    onUserScoreCardClicked: () -> Unit,
    onCreateSessionClicked: () -> Unit,
    onSessionClicked: (Int, String) -> Unit,
    onLogoutClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .wrapContentHeight()
    ) {
        AdminTopBar(onLogoutClicked)
        YappuUserScoreCard(
            setOnUserScoreCardClickedEvent = { onUserScoreCardClicked() }
        )
        GraySpacing(Modifier.height(12.dp))
        ManagementTitleWithCreateSession(onCreateSessionClicked)
        uiState.upcomingSession?.let { UpcomingSession(it, onSessionClicked) }
            ?: FinishAllSessions()
        Spacing()
        GrayDivider(modifier = Modifier.height(1.dp))
        ManagementSubTitle()
        Sessions(
            sessions = uiState.sessions,
            onSessionItemClicked = onSessionClicked
        )
    }
}

fun LazyListScope.AdminTopBar(onLogoutClicked: () -> Unit) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.clickable { onLogoutClicked() },
                painter = painterResource(id = R.drawable.icon_logout),
                contentDescription = null,
                tint = AttendanceTheme.colors.grayScale.Gray600,
            )
        }
    }
}

fun LazyListScope.Spacing() {
    item {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(top = 28.dp))
    }
}

fun LazyListScope.Sessions(
    sessions: List<Session>,
    onSessionItemClicked: (Int, String) -> Unit,
) {
    items(sessions) { session ->
        SessionItem(
            session = session,
            onSessionItemClicked = onSessionItemClicked
        )
    }
}

fun LazyListScope.YappuUserScoreCard(
    setOnUserScoreCardClickedEvent: () -> Unit,
) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(top = 15.dp, bottom = 28.dp, start = 24.dp, end = 24.dp)
                .clickable { setOnUserScoreCardClickedEvent() },
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp,
            backgroundColor = AttendanceTheme.colors.grayScale.Gray200
        ) {
            Image(
                painter = painterResource(id = R.drawable.illust_manager_home),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = string.admin_main_all_score_text),
                    style = AttendanceTypography.h3,
                    color = AttendanceTheme.colors.grayScale.Gray1200
                )
                Text(
                    text = stringResource(id = string.admin_main_see_all_score_text),
                    modifier = Modifier.padding(top = 4.dp),
                    style = AttendanceTypography.body1,
                    color = AttendanceTheme.colors.mainColors.YappOrange
                )
            }
        }
    }
}

fun LazyListScope.ManagementTitleWithCreateSession(
    onCreateSessionClicked: () -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .background(AttendanceTheme.colors.backgroundColors.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = string.admin_main_attend_management_text),
                modifier = Modifier
                    .weight(1f),
                style = AttendanceTypography.h1,
                color = AttendanceTheme.colors.grayScale.Gray1200
            )

            // TODO 임시 버튼으로, 디자인 대기 중
            YDSButtonSmall(
                text = stringResource(id = string.create_session),
                state = YdsButtonState.ENABLED,
                onClick = onCreateSessionClicked
            )
        }
    }
}

fun LazyListScope.GraySpacing(modifier: Modifier) {
    item {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
        )
    }
}

fun LazyListScope.GrayDivider(modifier: Modifier) {
    item {
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(AttendanceTheme.colors.grayScale.Gray300)
        )
    }
}

fun LazyListScope.UpcomingSession(
    upcomingSession: Session,
    onManagementButtonClicked: (Int, String) -> Unit,
) {
    item {
        Column(
            modifier = Modifier
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = upcomingSession.monthAndDay,
                color = AttendanceTheme.colors.grayScale.Gray600,
                style = AttendanceTypography.body2,
            )

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AttendanceTheme.colors.backgroundColors.background)
                    .padding(top = 6.dp)
            ) {
                val (sessionTitle, managementButton) = createRefs()

                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .constrainAs(sessionTitle) {
                            start.linkTo(parent.start)
                            end.linkTo(managementButton.start)
                            top.linkTo(parent.top)
                            width = Dimension.fillToConstraints
                        },
                    text = upcomingSession.title,
                    style = AttendanceTypography.h3,
                    color = AttendanceTheme.colors.grayScale.Gray1200,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                YDSButtonSmall(
                    modifier = Modifier
                        .constrainAs(managementButton) {
                            end.linkTo(parent.end)
                            top.linkTo(sessionTitle.top)
                            bottom.linkTo(sessionTitle.bottom)
                            width = Dimension.preferredWrapContent
                        },
                    text = stringResource(id = string.admin_main_admin_button),
                    state = if (upcomingSession.type == NeedToAttendType.NEED_ATTENDANCE) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = {
                        onManagementButtonClicked(
                            upcomingSession.sessionId,
                            upcomingSession.title
                        )
                    }
                )
            }

        }
    }
}

fun LazyListScope.FinishAllSessions() {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(id = string.admin_main_finish_all_sessions_text),
            style = AttendanceTypography.h3
        )
    }
}

fun LazyListScope.ManagementSubTitle() {
    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 4.dp),
            text = stringResource(id = string.admin_main_see_all_session_text),
            color = AttendanceTheme.colors.grayScale.Gray600,
            style = AttendanceTypography.body2
        )
    }
}

@Composable
private fun SessionItem(
    session: Session,
    onSessionItemClicked: (Int, String) -> Unit,
) {
    val MONTH_RANGE = 5..6
    val DAY_RANGE = 8..9

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .clickable {
                if (session.type == NeedToAttendType.NEED_ATTENDANCE)
                    onSessionItemClicked(session.sessionId, session.title)
            }
            .padding(vertical = 18.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textColor =
            if (session.type == NeedToAttendType.NEED_ATTENDANCE) AttendanceTheme.colors.grayScale.Gray1200 else AttendanceTheme.colors.grayScale.Gray400

        Text(
            modifier = Modifier.width(64.dp),
            text = session.monthAndDay,
            color = textColor,
            style = AttendanceTypography.body1,
        )

        Text(
            modifier = Modifier
                .padding(end = 12.dp)
                .weight(1F),
            text = session.title,
            color = textColor,
            maxLines = 1,
            style = AttendanceTypography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left
        )

        if (session.type == NeedToAttendType.NEED_ATTENDANCE) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.icon_chevron_right),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}
