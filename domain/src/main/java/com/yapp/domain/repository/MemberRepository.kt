package com.yapp.domain.repository

import com.yapp.domain.model.Member
import kotlinx.coroutines.flow.Flow


interface MemberRepository {
    suspend fun setMember(member: Member): Result<Unit>
    suspend fun getMember(id: Long): Result<Member?>
    suspend fun getMemberWithFlow(id: Long): Flow<Result<Member>>
    suspend fun deleteMember(id: Long): Result<Unit>
    suspend fun getAllMember(): Flow<Result<List<Member>>>
}
