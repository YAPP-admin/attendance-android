package com.yapp.domain.usecases

import com.yapp.domain.model.Member
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

class GetCurrentMemberInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(): Result<Member?> {
        return localRepository.getMemberId().mapCatching { currentMemberId ->
            if (currentMemberId == null) {
                return@mapCatching null
            }

            return@mapCatching memberRepository.getMember(currentMemberId).getOrThrow()
        }
    }

}