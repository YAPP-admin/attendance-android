package com.yapp.presentation.ui

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase
) : BaseViewModel<MainContract.MainUiState, MainContract.MainUiSideEffect, MainContract.MainUiEvent>(
    MainContract.MainUiState
) {
    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    fun validateQRScreen() {
        viewModelScope.launch {
//            getUpcomingSessionUseCase()
//                .collectWithCallback(
//                    onSuccess = { session ->
//                        지온님 작업 머지되면 주석 풀기
//                        session?.date?.let {
//                            val elapsedTime = DateUtil.getElapsedTime(it)
//                            if (elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE) {
//                                setEffect(MainContract.MainUiSideEffect.NavigateToQRScreen)
//                            } else {
//                                Log.e("###", "인증할 수 없는 시간.")
//                                setEffect(MainContract.MainUiSideEffect.ShowToast)
//                            }
//                        }
//                    },
//                    onFailed = {
//                        // TODO 에러핸들링 필요합니다.
//                    }
//                )
        }
    }

    override suspend fun handleEvent(event: MainContract.MainUiEvent) {
        when(event) {
            MainContract.MainUiEvent.OnClickQrAuthButton -> {
                checkAttendanceValidate()
                if(uiState.value.is)
            }
        }
    }

    private suspend fun checkAttendanceValidate() {

    }
}