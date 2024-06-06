package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository
) {

    suspend operator fun invoke(checkedSession: Session): Result<Unit> {
        return localRepository.getMemberId().mapCatching { currentUserId: Long? ->
            require(currentUserId != null)

            val currentTime = LocalDateTime.now()
            val currentMemberInfo = memberRepository.getMember(currentUserId).getOrThrow()

            currentMemberInfo!!.attendances.changeAttendanceType(
                sessionId = checkedSession.sessionId,
                changingAttendance = checkAttendanceState(
                    elapsedTime = Duration.between(checkedSession.startTime, currentTime).toMinutes()
                )
            ).also { updatedAttendanceList ->
                memberRepository.setMember(member = currentMemberInfo.copy(attendances = updatedAttendanceList))
            }
        }
    }

    private fun checkAttendanceState(elapsedTime: Long): Attendance.Status {
        return when (elapsedTime) {
            in -10..10 -> Attendance.Status.NORMAL
            in 11..120 -> Attendance.Status.LATE
            else -> Attendance.Status.ABSENT
        }
    }
}
