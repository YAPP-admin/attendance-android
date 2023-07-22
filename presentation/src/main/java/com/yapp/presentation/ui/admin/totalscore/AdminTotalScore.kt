package com.yapp.presentation.ui.admin.totalscore

import FoldableHeaderItemState
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSBox
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithScoreState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentScoreTypeItem
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableHeaderItem.FoldableHeaderItem
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayout


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
                onClickBackButton = { viewModel.setEvent(AdminTotalScoreContract.AdminTotalScoreUiEvent.OnBackArrowClick) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
    ) { contentPadding ->
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AdminTotalScoreContract.AdminTotalScoreUiSideEffect.NavigateToPreviousScreen -> navigateToPreviousScreen()
                }
            }
        }

        Crossfade(targetState = uiState.value.loadState) {
            when (it) {
                AdminTotalScoreContract.AdminTotalScoreUiState.LoadState.Loading -> YDSProgressBar()
                AdminTotalScoreContract.AdminTotalScoreUiState.LoadState.Idle -> AdminTotalScoreScreen2(
                    modifier = Modifier.padding(contentPadding),
                    uiState = uiState.value,
                    onEvent = { event -> viewModel.dispatchEvent(event) }
                )

                AdminTotalScoreContract.AdminTotalScoreUiState.LoadState.Error -> YDSEmptyScreen()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminTotalScoreScreen2(
    modifier: Modifier = Modifier,
    uiState: AdminTotalScoreContract.AdminTotalScoreUiState,
    onEvent: (AdminTotalScoreContract.AdminTotalScoreUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
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
                        onEvent(AdminTotalScoreContract.AdminTotalScoreUiEvent.OnTabItemSelected(selectedTabIndex))
                    }
                )
            }
        }

        item {
            YDSBox(
                modifier = Modifier.padding(vertical = 28.dp),
                text = stringResource(id = R.string.admin_total_score_box_text)
            )
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

                    is FoldableContentItemWithScoreState -> {
                        itemState.memberId
                    }

                    else -> itemState.hashCode()
                }
            }
        ) { _, itemState ->
            when (itemState) {
                is FoldableHeaderItemState.TeamType -> {
                    FoldableHeaderItem(
                        modifier = Modifier.animateItemPlacement(),
                        state = itemState,
                        onExpandClicked = { isExpanded, teamName, teamNumber ->
                            onEvent(AdminTotalScoreContract.AdminTotalScoreUiEvent.OnTeamTypeHeaderItemClicked(teamName, teamNumber))
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
                        onExpandClicked = { isExpanded, position ->
                            onEvent(AdminTotalScoreContract.AdminTotalScoreUiEvent.OnPositionTypeHeaderItemClicked(positionName = position))
                        }
                    )
                    if (itemState.isExpanded) {
                        Divider(color = AttendanceTheme.colors.grayScale.Gray300, thickness = 1.dp)
                    }
                }

                is FoldableContentItemWithScoreState -> {
                    FoldableContentScoreTypeItem(state = itemState)
                }
            }
        }
    }
}
