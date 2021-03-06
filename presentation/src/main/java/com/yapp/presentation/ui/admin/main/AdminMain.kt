package com.yapp.presentation.ui.admin.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSButtonSmall
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.YdsButtonState
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.presentation.R.string
import com.yapp.presentation.model.Session
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import kotlinx.coroutines.flow.collect

@Composable
fun AdminMain(
    viewModel: AdminMainViewModel = hiltViewModel(),
    navigateToAdminTotalScore: (Int) -> Unit,
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
            .systemBarsPadding()
    ) {
        when (uiState.loadState) {
            AdminMainUiState.LoadState.Loading -> YDSProgressBar()
            AdminMainUiState.LoadState.Idle -> AdminMainScreen(
                uiState = uiState,
                onUserScoreCardClicked = {
                    viewModel.setEvent(
                        AdminMainUiEvent.OnUserScoreCardClicked(uiState.lastSessionId)
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
    uiState: AdminMainUiState,
    onUserScoreCardClicked: () -> Unit,
    onSessionClicked: (Int, String) -> Unit,
    onLogoutClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AdminTopBar(onLogoutClicked)
        YappuUserScoreCard(
            setOnUserScoreCardClickedEvent = { onUserScoreCardClicked() }
        )
        GraySpacing(Modifier.height(12.dp))
        ManagementTitle()
        uiState.upcomingSession?.let { UpcomingSession(it, onSessionClicked) }
            ?: FinishAllSessions()
        Spacing()
        GraySpacing(
            Modifier
                .height(1.dp)
                .padding(horizontal = 24.dp)
        )
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.clickable { onLogoutClicked() },
                painter = painterResource(id = R.drawable.icon_logout),
                contentDescription = null,
                tint = Gray_600,
            )
        }
    }
}

fun LazyListScope.Spacing() {
    item {
        Spacer(modifier = Modifier.padding(top = 28.dp))
    }
}

fun LazyListScope.Sessions(
    sessions: List<Session>,
    onSessionItemClicked: (Int, String) -> Unit
) {
    items(sessions) { session ->
        SessionItem(
            session = session,
            onSessionItemClicked = onSessionItemClicked
        )
    }
}

fun LazyListScope.YappuUserScoreCard(
    setOnUserScoreCardClickedEvent: () -> Unit
) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 15.dp, bottom = 28.dp, start = 24.dp, end = 24.dp)
                .clickable { setOnUserScoreCardClickedEvent() },
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp,
            backgroundColor = Gray_200
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
                    color = Gray_1200
                )
                Text(
                    text = stringResource(id = string.admin_main_see_all_score_text),
                    modifier = Modifier.padding(top = 4.dp),
                    style = AttendanceTypography.body1,
                    color = Yapp_Orange
                )
            }
        }
    }
}

fun LazyListScope.ManagementTitle() {
    item {
        Text(
            text = stringResource(id = string.admin_main_attend_management_text),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            style = AttendanceTypography.h1
        )
    }
}

fun LazyListScope.GraySpacing(modifier: Modifier) {
    item {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .background(Gray_200)
        )
    }
}

fun LazyListScope.UpcomingSession(
    upcomingSession: Session,
    onManagementButtonClicked: (Int, String) -> Unit
) {
    val MONTH_RANGE = 5..6
    val DAY_RANGE = 8..9

    item {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = upcomingSession.date.substring(MONTH_RANGE) +
                        "." + upcomingSession.date.substring(DAY_RANGE),
                color = Gray_600,
                style = AttendanceTypography.body2
            )

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
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
            text = stringResource(id = string.admin_main_see_all_session_text),
            color = Gray_600,
            style = AttendanceTypography.body2,
            modifier = Modifier.padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 4.dp)
        )
    }
}

@Composable
private fun SessionItem(
    session: Session,
    onSessionItemClicked: (Int, String) -> Unit
) {
    val MONTH_RANGE = 5..6
    val DAY_RANGE = 8..9

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                if (session.type == NeedToAttendType.NEED_ATTENDANCE)
                    onSessionItemClicked(session.sessionId, session.title)
            }
            .padding(vertical = 18.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textColor =
            if (session.type == NeedToAttendType.NEED_ATTENDANCE) Gray_1200 else Gray_400

        Text(
            modifier = Modifier.width(64.dp),
            text = "${session.date.substring(MONTH_RANGE)}.${session.date.substring(DAY_RANGE)}",
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
