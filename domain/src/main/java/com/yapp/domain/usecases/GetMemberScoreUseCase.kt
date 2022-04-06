package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetMemberScoreUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<Int?>>, Unit>() {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<Int?>> {
        return localRepository.getMemberId().flatMapConcat { memberId ->
            memberRepository.getMember(memberId ?: -1)
        }.zip(firebaseRemoteConfig.getSessionList()) { member, sessions ->
            var memberScore = 0
            sessions.forEach {
                // 지난 세션의 출석 점수 계산
                if (DateUtil.isPastSession(it.date)) {
                    memberScore += member?.attendances?.get(it.sessionId)?.type?.point ?: 0
                } else {
                    return@forEach
                }
            }

            memberScore
        }.toResult()
    }
}