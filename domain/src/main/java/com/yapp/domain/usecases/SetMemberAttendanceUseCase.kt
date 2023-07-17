package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.repository.MemberRepository
import javax.inject.Inject


class SetMemberAttendanceUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(params: Params): Result<Unit> {
        return memberRepository.getMember(params.memberId).mapCatching { targetMemeber ->
            require(targetMemeber != null)

            if (targetMemeber.attendances[params.sessionId].status == params.changedAttendance) {
                return@mapCatching
            }

            val changedAttendances =  targetMemeber.attendances.changeAttendanceType(
                sessionId = params.sessionId,
                changingAttendance = params.changedAttendance
            )

            memberRepository.setMember(member = targetMemeber.copy(attendances = changedAttendances))
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
        val changedAttendance: Attendance.Status,
    )

}