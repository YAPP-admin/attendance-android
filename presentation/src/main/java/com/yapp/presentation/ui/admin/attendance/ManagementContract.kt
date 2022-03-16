package com.yapp.presentation.ui.admin.attendance

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState


class ManagementContract {
    data class ManagementState(
        val isLoading: Boolean = false,
        val title: String = ""
    ): UiState

    sealed class ManagementEvent : UiEvent

    sealed class ManagementSideEffect : UiSideEffect
}