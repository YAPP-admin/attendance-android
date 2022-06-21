package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject


class SetMemberAttendanceUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseUseCase<Flow<TaskResult<Unit>>, SetMemberAttendanceUseCase.Params>(dispatcherProvider) {

    override suspend fun invoke(params: Params?): Flow<TaskResult<Unit>> {
        return memberRepository.getMember(params!!.memberId)
            .flatMapConcat { member ->
                val editableAttendances = member!!.attendances.toMutableList().apply {
                    this[params.sessionId] = params.changedAttendance
                }

                memberRepository.setMember(
                    memberEntity = MemberEntity(
                        id = member.id,
                        name = member.name,
                        position = member.position,
                        team = member.team,
                        attendances = editableAttendances.toList()
                    )
                )
            }.toResult()
    }

    class Params(
        val memberId: Long,
        val sessionId: Int,
        val changedAttendance: AttendanceEntity
    )
}