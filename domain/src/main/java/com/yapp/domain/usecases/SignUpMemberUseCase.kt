package com.yapp.domain.usecases

import com.yapp.domain.model.*
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.model.types.PositionTypeEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class SignUpMemberUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend fun invoke(params: Params): Result<Unit> {
        return localRepository.getMemberId().mapCatching { currentMemberId: Long? ->
            require(currentMemberId != null)

            val sessionList = remoteConfigRepository.getSessionList().getOrThrow()
            val editedSessionList = createAttendanceEntities(sessionList)

            val memberEntity = createMemberEntity(memberId = currentMemberId, params = params, attendanceEntities = editedSessionList)

            memberRepository.setMember(memberEntity)
        }
    }

    private fun createAttendanceEntities(sessionList: List<SessionEntity>): List<AttendanceEntity> {
        return sessionList.map { session ->
            AttendanceEntity(
                sessionId = session.sessionId,
                type = when (session.type) {
                    NeedToAttendType.NEED_ATTENDANCE -> AttendanceTypeEntity.Absent
                    NeedToAttendType.DONT_NEED_ATTENDANCE -> AttendanceTypeEntity.Normal
                    NeedToAttendType.DAY_OFF -> AttendanceTypeEntity.Normal
                }
            )
        }
    }

    private fun createMemberEntity(
        memberId: Long,
        params: Params,
        attendanceEntities: List<AttendanceEntity>,
    ): MemberEntity {
        return MemberEntity(
            id = memberId,
            name = params.memberName,
            position = params.memberPosition,
            team = params.teamEntity,
            attendances = attendanceEntities
        )
    }

    /**
     * @param memberName 회원가입 이름입력 화면에서 입력된 Member 이름
     * @param memberPosition 회원가입 포지션/팀 화면의 선택된 Member 포지션
     * @param teamEntit: 회원가입 포지션/팀 화면의 선택된 Member Team
     * */
    class Params(
        val memberName: String,
        val memberPosition: PositionTypeEntity,
        val teamEntity: TeamEntity,
    )

}