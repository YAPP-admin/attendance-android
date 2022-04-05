package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetMemberAttendanceListUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<Pair<List<SessionEntity>, List<AttendanceEntity>?>>>, Unit>(coroutineDispatcher) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<Pair<List<SessionEntity>, List<AttendanceEntity>?>>> {
        val sessions = firebaseRemoteConfig.getSessionList()
        val memberId = localRepository.getMemberId().firstOrNull()
        val member = memberId?.let { id ->
            memberRepository.getMember(id)
        } ?: emptyFlow()

        return sessions.zip(member) { sessions, member ->
            sessions to member?.attendances
        }.toResult()
    }
}