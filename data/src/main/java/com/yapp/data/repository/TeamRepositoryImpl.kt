package com.yapp.data.repository

import com.yapp.data.datasource.TeamRemoteDataSource
import com.yapp.data.model.toDomain
import com.yapp.domain.model.Member
import com.yapp.domain.repository.TeamRepository
import javax.inject.Inject


class TeamRepositoryImpl @Inject constructor(
    private val teamDataSource: TeamRemoteDataSource,
) : TeamRepository {

    override suspend fun getTeamMembers(team: String): Result<List<Member>> {
        return runCatching {
            teamDataSource.getTeamMembers(team)
        }.fold(
            onSuccess = { entities ->
                Result.success(entities.map { it.toDomain() })
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

}