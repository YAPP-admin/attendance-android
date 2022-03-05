package com.yapp.domain.repository

import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow


interface MemberRepository {
    fun setMember(memberEntity: MemberEntity): Flow<Boolean>
    fun getMember(id: Long): Flow<MemberEntity?>
}