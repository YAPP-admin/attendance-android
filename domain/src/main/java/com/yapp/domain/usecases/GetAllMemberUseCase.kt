package com.yapp.domain.usecases

import com.yapp.domain.model.Member
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    operator fun invoke(): Flow<List<Member>> {
        return memberRepository.getAllMember()
    }

}