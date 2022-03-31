package com.yapp.presentation.ui.admin.totalscore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSBox
import com.yapp.common.yds.YDSProgressBar
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiEvent

const val WARNING_ICON_PADDING = 5
const val SCORE_LIMIT = 70

@Composable
fun AdminTotalScore(
    viewModel: AdminTotalScoreViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = R.string.admin_total_score_title),
                onClickBackButton = { onClickBackButton() }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        val uiState = viewModel.uiState.collectAsState()

        if (uiState.value.isLoading) {
            YDSProgressBar()
        } else {
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

                itemsIndexed(uiState.value.teamItemStates) { index, teamItemState ->
                    TeamItem(
                        teamItemState = teamItemState,
                        onTeamItemClicked = {
                            viewModel.setEvent(AdminTotalScoreUiEvent.ClickTeamItem(index))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamItem(
    teamItemState: TeamItemState,
    onTeamItemClicked: () -> Unit
) {
    val iconResourceId =
        if (teamItemState.isExpanded) R.drawable.icon_chevron_up else R.drawable.icon_chevron_down

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onTeamItemClicked() }
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

    if (teamItemState.isExpanded) {
        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Gray_300)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            for (i in 0 until teamItemState.teamMembers.size) {
                MemberItem(member = teamItemState.teamMembers[i])
            }
        }
        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Gray_300)
    }
}

@Composable
fun MemberItem(
    member: MemberScore
) {
    val startPadding = if (member.attendances < SCORE_LIMIT) (32 - WARNING_ICON_PADDING) else 32
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
            if (member.attendances < SCORE_LIMIT) {
                Icon(
                    modifier = Modifier.padding(end = (8 - WARNING_ICON_PADDING).dp),
                    painter = painterResource(id = R.drawable.icon_warning),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }

            Text(
                text = member.name,
                color = Gray_800,
                style = AttendanceTypography.body1
            )
        }

        Text(
            text = member.attendances.toString(),
            color = Yapp_Orange,
            style = AttendanceTypography.subtitle1
        )
    }
}