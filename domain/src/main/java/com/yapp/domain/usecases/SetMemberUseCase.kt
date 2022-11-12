package com.yapp.domain.usecases

import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.DispatcherProvider
import javax.inject.Inject

class SetMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend fun invoke(params: MemberEntity?): Result<Unit> {
        return memberRepository.setMember(memberEntity = params!!)
    }
}