package com.yapp.domain.usecases

import com.yapp.domain.model.Member
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(): Flow<Result<List<Member>>> {
        return memberRepository.getAllMember()
    }

}