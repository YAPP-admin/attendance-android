package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemberAttendanceListUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): Flow<Result<Pair<List<Session>, List<Attendance>>>> {
        return flow {
            runCatching {
                val sessionList = sessionRepository.getAllSession().getOrThrow()
                val currentMemberId = requireNotNull(localRepository.getMemberId().getOrThrow())

                memberRepository.getMemberWithFlow(currentMemberId)
                    .map { memberInfo ->
                        val attendances = memberInfo.getOrThrow().attendances
                        sessionList to attendances
                    }
                    .catch { exception ->
                        emit(Result.failure(exception))
                    }
                    .collect { result ->
                        emit(Result.success(result))
                    }
            }.onFailure { exception ->
                emit(Result.failure(exception))
            }
        }
    }
}
