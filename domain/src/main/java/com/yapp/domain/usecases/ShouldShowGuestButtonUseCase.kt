package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 스토어 게시를 위한 테스트 계정관련 remote config
 */
class ShouldShowGuestButtonUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<Boolean>>, Unit>(coroutineDispatcher) {
    override suspend fun invoke(params: Unit?): Flow<TaskResult<Boolean>> {
        return firebaseRemoteConfig.shouldShowGuestButton().toResult()
    }
}
