package com.yapp.presentation.ui.admin.totalscore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSBox
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiEvent
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiSideEffect
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiState

const val WARNING_ICON_PADDING = 5
const val SCORE_LIMIT = 70

@Composable
fun AdminTotalScore(
    viewModel: AdminTotalScoreViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = stringResource(id = R.string.admin_total_score_title),
                onClickBackButton = { viewModel.setEvent(AdminTotalScoreUiEvent.OnBackArrowClick) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
            .systemBarsPadding(),
    ) { contentPadding ->
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AdminTotalScoreUiSideEffect.NavigateToPreviousScreen -> navigateToPreviousScreen()
                }
            }
        }

        Crossfade(targetState = uiState.value.loadState) {
            when (it) {
                AdminTotalScoreUiState.LoadState.Loading -> YDSProgressBar()
                AdminTotalScoreUiState.LoadState.Idle -> AdminTotalScoreScreen(
                    modifier = Modifier.padding(contentPadding),
                    uiState = uiState.value,
                    onClickTeamToggle = {
                        viewModel.setEvent(AdminTotalScoreUiEvent.OnSectionTypeChange(it))
                    },
                )

                AdminTotalScoreUiState.LoadState.Error -> YDSEmptyScreen()
            }
        }
    }
}

@Composable
fun AdminTotalScoreScreen(
    modifier: Modifier = Modifier,
    uiState: AdminTotalScoreUiState,
    onClickTeamToggle: (AdminTotalScoreUiState.SectionType) -> Unit,
) {
    var toggle by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.background)
    ) {
        // TODO (EvergreenTree97) : 정렬 UI 나올시 제거
        item {
            YDSButtonLarge(text = uiState.sectionType.name, state = YdsButtonState.ENABLED) {
                toggle = !toggle
                onClickTeamToggle(if (toggle) AdminTotalScoreUiState.SectionType.Team else AdminTotalScoreUiState.SectionType.Position)
            }
        }
        item {
            YDSBox(
                modifier = Modifier.padding(vertical = 28.dp),
                text = stringResource(id = R.string.admin_total_score_box_text)
            )
        }

        itemsIndexed(
            items = uiState.sectionItemStates,
            key = { _, key -> key.sectionName }
        ) { _, teamItemState ->
            TeamItem(
                teamItemState = teamItemState
            )
        }
    }
}

@Composable
fun TeamItem(teamItemState: AdminTotalScoreUiState.SectionItemState) {
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
                .background(AttendanceTheme.colors.backgroundColors.background)
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 18.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = teamItemState.sectionName,
                color = AttendanceTheme.colors.grayScale.Gray1200,
                style = AttendanceTypography.h3
            )

            Icon(
                painter = painterResource(id = iconResourceId),
                tint = AttendanceTheme.colors.grayScale.Gray600,
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
                    .background(AttendanceTheme.colors.backgroundColors.background)
            ) {
                Divider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = AttendanceTheme.colors.grayScale.Gray300
                )
                teamItemState.members.forEach { teamMember ->
                    MemberItem(memberWithTotal = teamMember)
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = AttendanceTheme.colors.grayScale.Gray300
                )
            }
        }
    }
}

@Composable
fun MemberItem(
    memberWithTotal: AdminTotalScoreUiState.MemberState,
) {
    val startPadding =
        if (memberWithTotal.totalScore < SCORE_LIMIT) (32 - WARNING_ICON_PADDING) else 32
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AttendanceTheme.colors.backgroundColors.background)
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
                color = AttendanceTheme.colors.grayScale.Gray800,
                style = AttendanceTypography.body1
            )
        }

        Text(
            text = memberWithTotal.totalScore.toString(),
            color = AttendanceTheme.colors.mainColors.YappOrange,
            style = AttendanceTypography.subtitle1
        )
    }
}