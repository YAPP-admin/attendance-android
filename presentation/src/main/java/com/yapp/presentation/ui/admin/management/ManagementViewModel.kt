package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_TITLE
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementSideEffect
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase,
    savedStateHandle: SavedStateHandle,

    ) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val DEFAULT_SESSION_ID = 1
        const val TEXT_LOAD_SESSION_TITLE_FAILED = "LOAD FAILED"
    }

    init {
        val sessionId = savedStateHandle.get<Int>(KEY_SESSION_ID) ?: error("세션 아이디를 불러올 수 없습니다.")
        val sessionTitle = savedStateHandle.get<String>(KEY_SESSION_TITLE) ?: TEXT_LOAD_SESSION_TITLE_FAILED
    }

    override suspend fun handleEvent(event: ManagementEvent) {

    }

}
