package com.yapp.domain.usecases

import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    suspend fun invoke(): Flow<Result<List<MemberEntity>>> {
        return memberRepository.getAllMember()
    }

}