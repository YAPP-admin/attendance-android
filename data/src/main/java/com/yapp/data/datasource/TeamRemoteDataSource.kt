package com.yapp.data.datasource

import com.yapp.domain.model.MemberEntity


interface TeamRemoteDataSource {
    suspend fun getTeamMembers(team: String): List<MemberEntity>
}