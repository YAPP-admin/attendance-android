package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSessionListUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<List<SessionEntity>>>, Unit>() {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<List<SessionEntity>>> {
        return firebaseRemoteConfig.getSessionList()
            .toResult()
    }

}