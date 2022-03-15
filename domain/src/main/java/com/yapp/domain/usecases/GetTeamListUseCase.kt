package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTeamListUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseUseCase<Flow<TaskResult<List<TeamEntity>>>, GetTeamListUseCase.Params>() {

    override suspend fun invoke(params: Params?): Flow<TaskResult<List<TeamEntity>>> {
        return firebaseRemoteConfig.getTeamList()
            .toResult()
    }

    class Params

}