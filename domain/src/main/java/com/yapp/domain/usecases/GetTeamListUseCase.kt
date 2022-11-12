package com.yapp.domain.usecases

import com.yapp.domain.model.TeamEntity
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class GetTeamListUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend fun invoke(params: Unit?): Result<List<TeamEntity>> {
        return remoteConfigRepository.getTeamList()
    }

}