package com.yapp.presentation.ui.admin.management

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.*
import com.yapp.common.yds.*
import com.yapp.presentation.R
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.*
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.MemberState
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.TeamState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Preview
@Composable
fun AttendanceManagement(
    viewModel: ManagementViewModel = hiltViewModel(),
    onBackButtonClicked: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    when(uiState.loadState) {
        Loading -> YDSProgressBar()

        Idle -> {
            ManagementScreen(
                uiState = uiState,
                sheetState = sheetState,
                onBackButtonClicked = { onBackButtonClicked?.invoke() },
                onBottomSheetDialogItemClicked = { attendanceType ->
                    viewModel.setEvent(ManagementEvent.OnAttendanceTypeChanged(attendanceType))
                    coroutineScope.launch { sheetState.hide() }
                },
                onDropDownClicked = { changedMember ->
                    viewModel.setEvent(ManagementEvent.OnDropDownButtonClicked(changedMember))
                }
            )
        }

        Error -> YDSEmptyScreen()
    }

    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = viewModel.effect, key2 = lifeCycleOwner) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ManagementContract.ManagementSideEffect.OpenBottomSheetDialog -> sheetState.show()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ManagementScreen(
    uiState: ManagementContract.ManagementState,
    sheetState: ModalBottomSheetState,
    onBackButtonClicked: (() -> Unit),
    onBottomSheetDialogItemClicked: (AttendanceType) -> Unit,
    onDropDownClicked: ((MemberState) -> Unit)
) {
    ModalBottomSheetLayout(
        modifier = Modifier,
        sheetContent = {
            BottomSheetDialog(
                attendanceTypes = AttendanceType.getAllTypes(),
                onClickItem = { attendanceType -> onBottomSheetDialogItemClicked.invoke(attendanceType) }
            )
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            topBar = {
                YDSAppBar(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    title = uiState.sessionTitle,
                    onClickBackButton = { onBackButtonClicked.invoke() }
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {

                item {
                    Column {
                        Spacer(modifier = Modifier.height(28.dp))
                        AttendCountText(memberCount = uiState.memberCount)
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }

                itemsIndexed(
                    items = uiState.teams,
                    key = { _, team -> team.teamName }
                ) { _, team ->
                    ExpandableTeam(
                        state = team,
                        onDropDownClicked = { changedMember -> onDropDownClicked.invoke(changedMember) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AttendCountText(
    modifier: Modifier = Modifier,
    memberCount: Int = 0
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10F))
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Yapp_OrangeAlpha)
    ) {
        Crossfade(targetState = memberCount) { count ->
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                text = "${count}명이 출석했어요",
                textAlign = TextAlign.Center,
                style = AttendanceTypography.body1,
                color = Yapp_Orange
            )
        }
    }
}

@Preview
@Composable
fun ExpandableTeam(
    modifier: Modifier = Modifier,
    state: TeamState = TeamState(),
    onDropDownClicked: ((MemberState) -> Unit)? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TeamHeader(
            state = state,
            expanded = expanded,
            onExpandClicked = { changedState -> expanded = changedState }
        )

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(50)) +
                    expandVertically(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
                    ),
            exit = fadeOut(animationSpec = tween(50)) +
                    shrinkVertically(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
                    )
        ) {
            Column {
                Divider(modifier = Modifier.height(1.dp), color = Gray_300)
                for (member in state.members) {
                    MemberContent(
                        state = member,
                        onDropDownClicked = { clickedMember ->
                            onDropDownClicked?.invoke(clickedMember)
                        }
                    )
                }
                Divider(modifier = Modifier.height(1.dp), color = Gray_300)
            }
        }
    }
}

@Preview
@Composable
fun TeamHeader(
    modifier: Modifier = Modifier,
    state: TeamState = TeamState(),
    expanded: Boolean = false,
    onExpandClicked: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .height(62.dp)
            .fillMaxWidth()
            .clickable { onExpandClicked(!expanded) }
    ) {
        Text(
            modifier = Modifier
                .width(0.dp)
                .weight(0.9F)
                .fillMaxHeight()
                .padding(vertical = 18.dp),
            text = state.teamName,
            textAlign = TextAlign.Start,
            style = AttendanceTypography.h3,
            color = Gray_1200
        )

        IconButton(
            modifier = Modifier
                .weight(0.1F)
                .fillMaxHeight()
                .width(0.dp),
            onClick = { onExpandClicked(!expanded) }
        ) {
            Icon(
                painter = if (expanded) painterResource(id = R.drawable.icon_chevron_up) else painterResource(id = R.drawable.icon_chevron_down),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun MemberContent(
    modifier: Modifier = Modifier,
    state: MemberState = MemberState(),
    onDropDownClicked: (MemberState) -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(59.dp)
    ) {
        val (ydsDropdownButton, nameText) = createRefs()

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(nameText) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 17.5.dp)
                    bottom.linkTo(parent.bottom, margin = 17.5.dp)
                },
            text = state.name,
            textAlign = TextAlign.Start,
            style = AttendanceTypography.body1,
            color = Gray_800
        )

        YDSDropDownButton(
            modifier = Modifier
                .wrapContentHeight()
                .animateContentSize()
                .constrainAs(ydsDropdownButton) {
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(parent.top, margin = 13.dp)
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                },
            text = state.attendance.attendanceType.text,
            onClick = { onDropDownClicked.invoke(state) }
        )
    }

}

@Composable
fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    attendanceTypes: List<AttendanceType>,
    onClickItem: (AttendanceType) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsWithImePadding()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(color = Color.White)
        )

        for (type in attendanceTypes) {
            BottomSheetDialogItem(
                attendanceType = type,
                onClickItem = { onClickItem.invoke(type) }
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(color = Color.White)
        )
    }
}

@Preview
@Composable
fun BottomSheetDialogItem(
    modifier: Modifier = Modifier,
    attendanceType: AttendanceType = AttendanceType.Normal,
    onClickItem: (AttendanceType) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color.White)
            .clickable { onClickItem.invoke(attendanceType) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = 14.dp),
            text = attendanceType.text,
            style = AttendanceTypography.subtitle1,
            color = Gray_1200,
            textAlign = TextAlign.Center,
        )
    }
}