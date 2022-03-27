package com.yapp.domain.usecases

import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) : BaseUseCase<Flow<TaskResult<Unit>>, MemberEntity>() {
    override suspend fun invoke(params: MemberEntity?): Flow<TaskResult<Unit>> {
        return memberRepository.setMember(memberEntity = params!!)
            .toResult()
    }
}