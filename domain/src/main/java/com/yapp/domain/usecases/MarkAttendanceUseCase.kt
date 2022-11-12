package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository
) {
    suspend fun invoke(checkedSession: SessionEntity?): Result<Unit> {
        return localRepository.getMemberId().mapCatching { currentUserId: Long? ->
            require(currentUserId != null)

            val markedAttendanceState = AttendanceEntity(
                sessionId = checkedSession!!.sessionId,
                type = checkAttendanceState(checkedSession.date)
            )

            val currentMemberInfo = memberRepository.getMember(currentUserId).getOrThrow()

            val markedAttendanceList = currentMemberInfo!!.attendances.toMutableList()
                .apply { this[checkedSession.sessionId] = markedAttendanceState }

            memberRepository.setMember(
                memberEntity = currentMemberInfo.copy(attendances = markedAttendanceList)
            )
        }
    }

    private fun checkAttendanceState(sessionDate: String): AttendanceTypeEntity {
        return when (DateUtil.getElapsedTime(sessionDate)) {
            in -5..5 -> AttendanceTypeEntity.Normal
            in 6..30 -> AttendanceTypeEntity.Late
            else -> AttendanceTypeEntity.Absent
        }
    }
}