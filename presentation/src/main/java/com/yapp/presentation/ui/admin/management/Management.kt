package com.yapp.presentation.ui.admin.management

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Error
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Idle
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Loading
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayout
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemLayout
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayout
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayout
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Preview
@Composable
fun AttendanceManagement(
    viewModel: ManagementViewModel = hiltViewModel(),
    onBackButtonClicked: (() -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.loadState) {
        Loading -> YDSProgressBar()

        Idle -> {
            ManagementScreen(
                uiState = uiState,
                onEvent = { viewModel.setEvent(it) },
                onBackButtonClicked = { onBackButtonClicked?.invoke() }
            )
        }

        Error -> YDSEmptyScreen()
    }

    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = viewModel.effect, key2 = lifeCycleOwner) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
internal fun ManagementScreen(
    uiState: ManagementContract.ManagementState,
    onEvent: (ManagementEvent) -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var selectedMemberId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(key1 = sheetState) {
        if (sheetState.isVisible.not()) {
            selectedMemberId = null
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetDialog(
                itemStates = uiState.bottomSheetDialogState,
                onClickItem = { attendanceType ->
                    onEvent(ManagementEvent.OnAttendanceTypeChanged(selectedMemberId!!, attendanceType))
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

                stickyHeader {
                    Column(modifier = Modifier.fillMaxWidth()
                        .background(color = AttendanceTheme.colors.backgroundColors.background)) {
                        Spacer(
                            modifier = Modifier.fillMaxWidth().height(20.dp)
                        )
                        YDSTabLayout(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            tabItems = uiState.tabLayoutState.items,
                            selectedIndex = uiState.tabLayoutState.selectedIndex,
                            onTabSelected = { selectedTabIndex ->
                                onEvent(ManagementEvent.OnTabItemSelected(selectedTabIndex))
                            }
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 28.dp)) {
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
                            selectedMemberId = changedMember
                            coroutineScope.launch { sheetState.show() }
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
