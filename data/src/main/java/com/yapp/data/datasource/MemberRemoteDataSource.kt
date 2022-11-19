package com.yapp.data.datasource

import com.yapp.data.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface MemberRemoteDataSource {
    suspend fun setMember(member: MemberEntity)
    suspend fun getMember(id: Long): MemberEntity?
    suspend fun deleteMember(id: Long)
    suspend fun getAllMember(): Flow<List<MemberEntity>>
}