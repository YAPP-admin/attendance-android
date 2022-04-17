package com.yapp.presentation.ui.member.score

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.domain.usecases.GetSessionListUseCase
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
    private val getSessionListUseCase: GetSessionListUseCase,
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
                                setState { copy(isLoading = false, attendanceList = attendanceList) }
                            }
                        },
                        onFailed = {
                            Log.d("####", it.message.toString())
                        }
                    )
            }

//            //제거하기
//            getSessionListUseCase()
//                .collectWithCallback(
//                    onSuccess = { entities ->
//                        val sessions = entities.map { entity -> entity.mapTo() }
//                        setState { copy(isLoading = false, sessions = sessions) }
//                    },
//                    onFailed = {
//                        // TODO 에러핸들링 필요합니다.
//                    }
//                )
        }
    }

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: MemberScoreUiEvent) {
        TODO("Not yet implemented")
    }
}