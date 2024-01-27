package com.yapp.presentation.ui.admin.management

import FoldableHeaderItemState
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Attendance
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Error
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Idle
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.LoadState.Loading
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayout
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentButtonTypeItem
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableHeaderItem.FoldableHeaderItem
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayout
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayout
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Preview
@Composable
fun AttendanceManagement(
    viewModel: ManagementViewModel = hiltViewModel(),
    onBackButtonClicked: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
                if (effect is ManagementContract.ManagementSideEffect.ShowToast) {
                    Toast.makeText(context, effect.description, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

internal typealias MemberItemLongPressCallback = (memberId: Long) -> Unit

internal val LocalMemberItemLongPressCallback = compositionLocalOf<MemberItemLongPressCallback?>(defaultFactory = { null })

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
internal fun ManagementScreen(
    uiState: ManagementContract.ManagementState,
    onEvent: (ManagementEvent) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var selectedMemberId by remember { mutableStateOf<Long?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    val itemLongPressCallback by remember {
        mutableStateOf<MemberItemLongPressCallback>({ memberId ->
            selectedMemberId = memberId
            showDialog = true
        })
    }

    /**
     * 임시로 추가한 회원삭제 다이얼로그로
     * (기획 / 디자인)이 변경될 시 수정 예정
     */
    if (showDialog) {
        YDSPopupDialog(
            title = "해당 회원을 삭제하시겠습니까?",
            content = "삭제하면 모든 세션에서 해당 회원이 사라져요",
            negativeButtonText = stringResource(id = R.string.member_setting_withdraw_dialog_negative_button),
            positiveButtonText = "삭제하기",
            onClickPositiveButton = {
                selectedMemberId?.let { onEvent(ManagementEvent.OnDeleteMemberClicked(it)) }
                showDialog = !showDialog
            },
            onClickNegativeButton = {
                selectedMemberId = null
                showDialog = !showDialog
            },
            onDismiss = { showDialog = !showDialog }
        )
    }

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
                    coroutineScope.launch { sheetState.hide() }
                    onEvent(
                        ManagementEvent.OnAttendanceTypeChanged(
                            selectedMemberId!!,
                            attendanceType
                        )
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
                .systemBarsPadding()
                .background(AttendanceTheme.colors.backgroundColors.background),
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
        ) {  _ ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AttendanceTheme.colors.backgroundColors.background)
                    .padding(horizontal = 24.dp)
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = AttendanceTheme.colors.backgroundColors.background)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        YDSTabLayout(
                            tabItems = uiState.tabLayoutState.items,
                            selectedIndex = uiState.tabLayoutState.selectedIndex,
                            onTabSelected = { selectedTabIndex ->
                                onEvent(ManagementEvent.OnTabItemSelected(selectedTabIndex))
                            }
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background)) {
                        Spacer(modifier = Modifier.height(24.dp))
                        StatisticalTableLayout(state = uiState.attendanceStatisticalTableState)
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }

                itemsIndexed(
                    items = uiState.foldableItemStates.flatMap { it.flatten },
                    key = { _, itemState ->
                        when (itemState) {
                            is FoldableHeaderItemState -> {
                                itemState.label
                            }

                            is FoldableContentItemWithButtonState -> {
                                itemState.memberId
                            }

                            else -> itemState.hashCode()
                        }
                    }
                ) { _, itemState ->
                    CompositionLocalProvider(LocalMemberItemLongPressCallback provides itemLongPressCallback) {
                        when (itemState) {
                            is FoldableHeaderItemState.TeamType -> {
                                FoldableHeaderItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    state = itemState,
                                    onExpandClicked = { _, teamName, teamNumber ->
                                        onEvent(ManagementEvent.OnTeamTypeHeaderItemClicked(teamName, teamNumber))
                                    }
                                )
                                if (itemState.isExpanded) {
                                    Divider(color = AttendanceTheme.colors.grayScale.Gray300, thickness = 1.dp)
                                }
                            }

                            is FoldableHeaderItemState.PositionType -> {
                                FoldableHeaderItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    state = itemState,
                                    onExpandClicked = { _, position ->
                                        onEvent(ManagementEvent.OnPositionTypeHeaderItemClicked(positionName = position))
                                    }
                                )
                                if (itemState.isExpanded) {
                                    Divider(color = AttendanceTheme.colors.grayScale.Gray300, thickness = 1.dp)
                                }
                            }

                            is FoldableContentItemWithButtonState -> {
                                FoldableContentButtonTypeItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    state = itemState,
                                    onDropDownClicked = {
                                        selectedMemberId = it
                                        coroutineScope.launch {
                                            sheetState.show()
                                        }
                                    }
                                )
                            }
                        }
                    }
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
    onClickItem: (Attendance.Status) -> Unit = {}
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
