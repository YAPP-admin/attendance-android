package com.yapp.presentation.ui.member.score

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.member.score.MemberScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,

    ) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(
    MemberScoreUiState()
) {
    init {
        //제거하기
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMemberAttendanceListUseCase()
                    .collectWithCallback(
                        onSuccess = { entities ->
                            val session = entities.first.map { entity -> entity.mapTo() }
                            val attendance = entities.second?.map { entity -> entity.mapTo() }
                            if (!attendance.isNullOrEmpty()) {
                                val attendanceList = session zip attendance
                                setState {
                                    copy(
                                        isLoading = false,
                                        attendanceList = attendanceList,
                                        lastAttendanceList = attendanceList.filter {
                                            DateUtil.isPastSession(
                                                it.first.date
                                            )
                                        })
                                }
                            }
                        },
                        onFailed = {
                        }
                    )
            }
        }
    }

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override suspend fun handleEvent(event: MemberScoreUiEvent) {
        TODO("Not yet implemented")
    }
}