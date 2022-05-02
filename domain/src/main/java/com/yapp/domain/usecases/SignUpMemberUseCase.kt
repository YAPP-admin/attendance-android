package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.model.types.PositionTypeEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SignUpMemberUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<Flow<TaskResult<Unit>>, SignUpMemberUseCase.Params>(dispatcherProvider) {

    override suspend fun invoke(params: Params?): Flow<TaskResult<Unit>> {
        return firebaseRemoteConfig.getSessionList().map { sessionList ->
            return@map sessionList.map { session ->
                AttendanceEntity(
                    sessionId = session.sessionId,
                    type = when (session.type) {
                        NeedToAttendType.DONT_NEED_ATTENDANCE, NeedToAttendType.DAY_OFF -> AttendanceTypeEntity.Normal
                        NeedToAttendType.NEED_ATTENDANCE -> AttendanceTypeEntity.Absent
                    }
                )
            }
        }.flatMapConcat { editedSessionList ->
            localRepository.getMemberId()
                .map { memberId ->
                    MemberEntity(
                        id = memberId!!,
                        name = params!!.memberName,
                        position = params.memberPosition,
                        team = params.teamEntity,
                        attendances = editedSessionList
                    )
                }
        }.flatMapConcat { memberEntity ->
            memberRepository.setMember(memberEntity)
        }.toResult()
    }

    class Params(
        val memberName: String,
        val memberPosition: PositionTypeEntity,
        val teamEntity: TeamEntity
    )

}