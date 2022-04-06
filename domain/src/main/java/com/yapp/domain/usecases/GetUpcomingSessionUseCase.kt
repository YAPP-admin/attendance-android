package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUpcomingSessionUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<SessionEntity?>>, Unit>() {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<SessionEntity?>> {
        // 세션 당일 밤 12시까지
        return firebaseRemoteConfig.getSessionList()
            .map { list ->
                list.firstOrNull { DateUtil.isUpcomingSession(it.date) }
            }.toResult()
    }
}