package com.yapp.presentation.ui.admin.totalscore

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSBox
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.*
import kotlinx.coroutines.flow.collect

const val WARNING_ICON_PADDING = 5
const val SCORE_LIMIT = 70

@Composable
fun AdminTotalScore(
    viewModel: AdminTotalScoreViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = R.string.admin_total_score_title),
                onClickBackButton = { viewModel.setEvent(AdminTotalScoreUiEvent.OnBackArrowClick) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        val uiState = viewModel.uiState.collectAsState()

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AdminTotalScoreUiSideEffect.NavigateToPreviousScreen -> navigateToPreviousScreen()
                }
            }
        }

        when (uiState.value.loadState) {
            AdminTotalScoreUiState.LoadState.Loading -> YDSProgressBar()
            AdminTotalScoreUiState.LoadState.Idle -> AdminTotalScoreScreen(uiState = uiState.value)
            AdminTotalScoreUiState.LoadState.Error -> YDSEmptyScreen()
        }
    }
}

@Composable
fun AdminTotalScoreScreen(
    uiState: AdminTotalScoreUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        item {
            YDSBox(
                modifier = Modifier.padding(vertical = 28.dp),
                text = stringResource(id = R.string.admin_total_score_box_text)
            )
        }

        itemsIndexed(
            items = uiState.teamItemStates,
            key = { _, key -> key.teamName }
        ) { _, teamItemState ->
            TeamItem(
                teamItemState = teamItemState
            )
        }
    }
}

@Composable
fun TeamItem(teamItemState: AdminTotalScoreUiState.TeamItemState) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val iconResourceId =
        if (isExpanded) R.drawable.icon_chevron_up else R.drawable.icon_chevron_down

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 18.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = teamItemState.teamName,
                color = Gray_1200,
                style = AttendanceTypography.h3
            )

            Icon(
                painter = painterResource(id = iconResourceId),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(50)) +
                    expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
            exit = fadeOut(animationSpec = tween(50)) +
                    shrinkVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Gray_300)
                teamItemState.teamMembers.forEach { teamMember ->
                    MemberItem(memberWithTotal = teamMember)
                }
                Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Gray_300)
            }
        }
    }
}

@Composable
fun MemberItem(
    memberWithTotal: AdminTotalScoreUiState.MemberWithTotalScore
) {
    val startPadding =
        if (memberWithTotal.totalScore < SCORE_LIMIT) (32 - WARNING_ICON_PADDING) else 32
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 18.dp)
            .padding(start = startPadding.dp, end = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            if (memberWithTotal.totalScore < SCORE_LIMIT) {
                Icon(
                    modifier = Modifier.padding(end = (8 - WARNING_ICON_PADDING).dp),
                    painter = painterResource(id = R.drawable.icon_warning),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }

            Text(
                text = memberWithTotal.name,
                color = Gray_800,
                style = AttendanceTypography.body1
            )
        }

        Text(
            text = memberWithTotal.totalScore.toString(),
            color = Yapp_Orange,
            style = AttendanceTypography.subtitle1
        )
    }
}