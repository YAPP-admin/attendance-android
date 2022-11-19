package com.yapp.data.datasource

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface MemberRemoteDataSource {
    suspend fun setMember(memberEntity: MemberEntity)
    suspend fun getMember(id: Long): MemberEntity?
    suspend fun deleteMember(id: Long): Unit
    suspend fun getAllMember(): Flow<List<MemberEntity>>
}