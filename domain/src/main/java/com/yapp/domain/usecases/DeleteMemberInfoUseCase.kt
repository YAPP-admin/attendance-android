package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

class DeleteMemberInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend fun invoke(memberId: Long): Result<Boolean> {
        return kotlin.runCatching {
            localRepository.deleteAllUserInfo()
            memberRepository.deleteMember(memberId)
        }.fold(
            onSuccess = {
                Result.success(true)
            },
            onFailure = { exception ->
                Result.success(false)
            }
        )
    }

}