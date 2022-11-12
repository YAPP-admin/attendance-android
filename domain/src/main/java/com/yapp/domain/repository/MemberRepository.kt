package com.yapp.domain.repository

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface MemberRepository {
    suspend fun setMember(memberEntity: MemberEntity): Result<Unit>
    suspend fun getMember(id: Long): Result<MemberEntity?>
    suspend fun deleteMember(id: Long): Result<Unit>
    suspend fun getAllMember(): Flow<Result<List<MemberEntity>>>
}