package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.DateUtil
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(checkedSession: Session): Result<Unit> {
        return localRepository.getMemberId().mapCatching { currentUserId: Long? ->
            require(currentUserId != null)

            val currentMemberInfo = memberRepository.getMember(currentUserId).getOrThrow()

            currentMemberInfo!!.attendances.changeAttendanceType(
                sessionId = checkedSession.sessionId,
                changingAttendance = checkAttendanceState(checkedSession.date)
            ).also { updatedAttendanceList ->
                memberRepository.setMember(member = currentMemberInfo.copy(attendances = updatedAttendanceList))
            }
        }
    }

    private fun checkAttendanceState(sessionDate: String): Attendance.Status {
        return when (DateUtil.getElapsedTime(sessionDate)) {
            in -5..5 -> Attendance.Status.NORMAL
            in 6..30 -> Attendance.Status.LATE
            else -> Attendance.Status.ABSENT
        }
    }

}
