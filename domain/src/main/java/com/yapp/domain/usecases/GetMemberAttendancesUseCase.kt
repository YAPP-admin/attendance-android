package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.DispatcherProvider
import javax.inject.Inject

class GetMemberAttendancesUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
) {

    suspend fun invoke(params: Unit?): Result<List<AttendanceEntity>?> {
        return localRepository.getMemberId().mapCatching { currentMemberId: Long? ->
            require(currentMemberId != null)

            val currentMemberInfo = memberRepository.getMember(currentMemberId).getOrThrow()
            currentMemberInfo!!.attendances
        }
    }

}