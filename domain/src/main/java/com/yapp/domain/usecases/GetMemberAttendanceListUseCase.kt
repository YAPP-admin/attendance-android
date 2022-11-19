package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class GetMemberAttendanceListUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(): Result<Pair<List<SessionEntity>, List<AttendanceEntity>>> {
        runCatching {
            val sessionList = remoteConfigRepository.getSessionList().getOrThrow()
            val currentMemberId = localRepository.getMemberId().getOrNull()

            require(currentMemberId != null)

            val currentMemberInfo = memberRepository.getMember(currentMemberId).getOrThrow()

            sessionList to currentMemberInfo!!.attendances
        }.fold(
            onSuccess = { memberAttendances ->
                return Result.success(memberAttendances)
            },
            onFailure = { exception ->
                return Result.failure(exception)
            }
        )
    }

}