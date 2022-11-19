package com.yapp.domain.usecases

import com.yapp.domain.model.Member
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

class SetMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(params: Member): Result<Unit> {
        return memberRepository.setMember(member = params)
    }

}