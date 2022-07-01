package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseUseCase<Flow<TaskResult<Unit>>, SessionEntity>(dispatcherProvider) {
    override suspend fun invoke(params: SessionEntity?): Flow<TaskResult<Unit>> {
        val attendanceState = AttendanceEntity(
            sessionId = params!!.sessionId,
            type = checkAttendanceState(params.date)
        )
        val userId = localRepository.getMemberId().firstOrNull()

        return memberRepository.getMember(userId!!)
            .flatMapConcat { member ->
                val attendances = member!!.attendances.toMutableList().apply {
                    this[params.sessionId] = attendanceState
                }

                memberRepository.setMember(
                    memberEntity = com.yapp.domain.model.MemberEntity(
                        id = member.id,
                        name = member.name,
                        position = member.position,
                        team = member.team,
                        attendances = attendances.toList()
                    )
                )
            }.toResult()
    }

    private fun checkAttendanceState(sessionDate: String): AttendanceTypeEntity {
        return when (DateUtil.getElapsedTime(sessionDate)) {
            in -5..5 -> AttendanceTypeEntity.Normal
            in 6..30 -> AttendanceTypeEntity.Late
            else -> AttendanceTypeEntity.Absent
        }
    }
}