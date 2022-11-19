package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberAttendancesUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(): Result<List<Attendance>?> {
        return localRepository.getMemberId().mapCatching { currentMemberId: Long? ->
            require(currentMemberId != null)

            val currentMemberInfo = memberRepository.getMember(currentMemberId).getOrThrow()
            currentMemberInfo!!.attendances
        }
    }

}