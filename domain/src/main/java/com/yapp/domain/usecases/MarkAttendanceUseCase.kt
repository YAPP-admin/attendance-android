package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.AttendanceType
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

            val markedAttendanceState = Attendance(
                sessionId = checkedSession.sessionId,
                type = checkAttendanceState(checkedSession.date)
            )

            val currentMemberInfo = memberRepository.getMember(currentUserId).getOrThrow()

            currentMemberInfo!!.attendances.changeAttendanceType(
                sessionId = checkedSession.sessionId,
                changingAttendance = markedAttendanceState
            ).also { updatedAttendanceList ->
                memberRepository.setMember(member = currentMemberInfo.copy(attendances = updatedAttendanceList))
            }
        }
    }

    private fun checkAttendanceState(sessionDate: String): AttendanceType {
        return when (DateUtil.getElapsedTime(sessionDate)) {
            in -5..5 -> AttendanceType.Normal
            in 6..30 -> AttendanceType.Late
            else -> AttendanceType.Absent
        }
    }

}