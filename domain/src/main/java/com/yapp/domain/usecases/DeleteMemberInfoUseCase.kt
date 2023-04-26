package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

class DeleteMemberInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(memberId: Long): Result<Boolean> {
        return runCatching {
            localRepository.deleteAllUserInfo()
            memberRepository.deleteMember(memberId)
        }.fold(
            onSuccess = {
                Result.success(true)
            },
            onFailure = {
                Result.success(false)
            }
        )
    }

}