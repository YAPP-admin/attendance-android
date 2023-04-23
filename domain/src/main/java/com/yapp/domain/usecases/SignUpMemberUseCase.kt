package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Member
import com.yapp.domain.model.Session
import com.yapp.domain.model.Team
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.model.types.PositionType
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class SignUpMemberUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
) {

    suspend operator fun invoke(params: Params): Result<Unit> {
        return localRepository.getMemberId().mapCatching { currentMemberId: Long? ->
            require(currentMemberId != null)

            val sessionList = remoteConfigRepository.getSessionList().getOrThrow()
            val editedSessionList = createAttendanceEntities(sessionList)

            val memberEntity = createMember(memberId = currentMemberId, params = params, attendanceEntities = editedSessionList)

            memberRepository.setMember(memberEntity)
        }
    }

    private fun createAttendanceEntities(sessionList: List<Session>): List<Attendance> {
        return sessionList.map { session ->
            Attendance(
                sessionId = session.sessionId,
                status = when (session.type) {
                    NeedToAttendType.NEED_ATTENDANCE -> Attendance.Status.ABSENT
                    NeedToAttendType.DONT_NEED_ATTENDANCE -> Attendance.Status.NORMAL
                    NeedToAttendType.DAY_OFF -> Attendance.Status.NORMAL
                }
            )
        }
    }

    private fun createMember(
        memberId: Long,
        params: Params,
        attendanceEntities: List<Attendance>,
    ): Member {
        return Member(
            id = memberId,
            name = params.memberName,
            position = params.memberPosition,
            team = params.team,
            attendances = AttendanceList.from(attendanceEntities)
        )
    }

    /**
     * @param memberName 회원가입 이름입력 화면에서 입력된 Member 이름
     * @param memberPosition 회원가입 포지션/팀 화면의 선택된 Member 포지션
     * @param team: 회원가입 포지션/팀 화면의 선택된 Member Team
     * */
    class Params(
        val memberName: String,
        val memberPosition: PositionType,
        val team: Team,
    )

}
