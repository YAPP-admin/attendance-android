package com.yapp.domain.repository

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface TeamRepository {
    suspend fun getTeamMembers(team: String): Result<List<MemberEntity>>
}