package com.yapp.data.repository

import com.yapp.data.remote.TeamRemoteDataSource
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.TeamRepository
import javax.inject.Inject


class TeamRepositoryImpl @Inject constructor(
    private val teamRemoteDataSource: TeamRemoteDataSource,
) : TeamRepository {

    override suspend fun getTeamMembers(team: String): Result<List<MemberEntity>> {
        return kotlin.runCatching {
            teamRemoteDataSource.getTeamMembers(team)
        }.fold(
            onSuccess = { entities ->
                Result.success(entities)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

}