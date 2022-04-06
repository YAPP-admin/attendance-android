package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemberScoreUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<Int?>>, Unit>() {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<Int?>> {
        var memberId: Long = -1
        localRepository.getMemberId().collect { memberId = it ?: -1 }
        var member: MemberEntity? = null
        memberRepository.getMember(memberId).collect { member = it }
        val sessions = firebaseRemoteConfig.getSessionList()

        var memberScore = 0
        sessions.map { list ->
            list.forEach {
                // 지난 세션의 출석 점수 계산
                if (DateUtil.isPastSession(it.date)) {
                    memberScore += member?.attendances?.get(it.sessionId)?.type?.point ?: 0
                } else {
                    return@forEach
                }
            }
        }

        return flow { emit(memberScore) }.toResult()
    }
}