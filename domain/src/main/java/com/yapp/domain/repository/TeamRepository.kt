package com.yapp.domain.repository

import com.yapp.domain.model.Member


interface TeamRepository {
    suspend fun getTeamMembers(team: String): Result<List<Member>>
}