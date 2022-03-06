package com.yapp.domain.repository

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface TeamRepository {
    fun getTeamMembers(team: String): Flow<List<MemberEntity>>
}