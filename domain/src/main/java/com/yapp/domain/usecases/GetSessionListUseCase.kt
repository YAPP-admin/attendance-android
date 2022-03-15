package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSessionListUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseUseCase<Flow<TaskResult<List<SessionEntity>>>, GetSessionListUseCase.Params>() {

    override suspend fun invoke(params: Params?): Flow<TaskResult<List<SessionEntity>>> {
        return firebaseRemoteConfig.getSessionList()
            .toResult()
    }

    class Params
}