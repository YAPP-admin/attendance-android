package com.yapp.domain.usecases

import com.yapp.domain.model.Team
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class GetTeamListUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(): Result<List<Team>> {
        return remoteConfigRepository.getTeamList()
    }

}