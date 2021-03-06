package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTeamListUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<List<TeamEntity>>>, Unit>(coroutineDispatcher) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<List<TeamEntity>>> {
        return firebaseRemoteConfig.getTeamList()
            .toResult()
    }

}