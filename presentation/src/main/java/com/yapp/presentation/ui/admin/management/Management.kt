package com.yapp.presentation.ui.admin.management

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.*
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayout
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayout
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemLayout
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Preview
@Composable
fun AttendanceManagement(
    viewModel: ManagementViewModel = hiltViewModel(),
    onBackButtonClicked: (() -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    when (uiState.loadState) {
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
internal fun ManagementScreen(
    uiState: ManagementContract.ManagementState,
    sheetState: ModalBottomSheetState,
    onBackButtonClicked: (() -> Unit),
    onBottomSheetDialogItemClicked: (Attendance.Status) -> Unit,
    onDropDownClicked: ((memberId: Long) -> Unit),
) {
    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetDialog(
                itemStates = uiState.bottomSheetDialogState,
                onClickItem = { attendanceType ->
                    onBottomSheetDialogItemClicked.invoke(
                        attendanceType
                    )
                }
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
                    modifier = Modifier
                        .background(AttendanceTheme.colors.backgroundColors.background)
                        .padding(horizontal = 4.dp),
                    title = uiState.topBarState.sessionTitle,
                    onClickBackButton = { onBackButtonClicked.invoke() }
                )
            },
            backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AttendanceTheme.colors.backgroundColors.background)
                    .padding(innerPadding)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        StatisticalTableLayout(state = uiState.attendanceStatisticalTableState)
                    }
                }

                itemsIndexed(
                    items = uiState.foldableItemStates,
                    key = { _, team -> team.headerState.label }
                ) { _, team ->
                    FoldableItemLayout(
                        state = team,
                        onDropDownClicked = { changedMember ->
                            onDropDownClicked.invoke(
                                changedMember
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    itemStates: List<AttendanceBottomSheetItemLayoutState>,
    onClickItem: (Attendance.Status) -> Unit = {},
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
                .background(color = AttendanceTheme.colors.backgroundColors.backgroundElevated)
        )

        for (itemState in itemStates) {
            AttendanceBottomSheetItemLayout(
                state = itemState,
                onClickItem = { onClickItem.invoke(itemState.onClickIcon) }
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(color = AttendanceTheme.colors.backgroundColors.backgroundElevated)
        )
    }
}


