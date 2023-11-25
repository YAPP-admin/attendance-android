package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

/**
 * 멤버 **스스로** 회원 탈퇴 기능을 사용할때의 UseCase
 *
 * 로컬에 저장된 Kakao MemberId를 제거 -> FireStore에 존재하는 데이터를 제거합니다.
 */
class WithdrawMemberInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository
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