package com.yapp.domain.repository

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface MemberRepository {
    fun setMember(memberEntity: MemberEntity): Flow<Unit>
    fun getMember(id: Long): Flow<MemberEntity?>
    fun getAllMember(): Flow<List<MemberEntity>>
}