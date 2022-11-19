package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject


class SetMemberAttendanceUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    suspend fun invoke(params: Params): Result<Unit> {
        return memberRepository.getMember(params.memberId).mapCatching { targetMemeber ->
            require(targetMemeber != null)

            val changedAttendances = targetMemeber.attendances.toMutableList().apply {
                this[params.sessionId] = params.changedAttendance
            }

            memberRepository.setMember(
                memberEntity = targetMemeber.copy(attendances = changedAttendances)
            )
        }
    }

    /**
     * @param memberId : 수정하고자 하는 멤버의 ID
     * @param sessionId : 수정하고자 하는 세션
     * @param changedAttendance : 수정하고자 하는 출석타입
     */
    class Params(
        val memberId: Long,
        val sessionId: Int,
        val changedAttendance: AttendanceEntity,
    )

}