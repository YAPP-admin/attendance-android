package com.yapp.domain.usecases

import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemberAttendanceListUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(): Flow<Result<Pair<List<Session>, List<Attendance>>>> {
        return flow {
            runCatching {
                val sessionList = remoteConfigRepository.getSessionList().getOrThrow()
                val currentMemberId = requireNotNull(localRepository.getMemberId().getOrThrow())
                memberRepository.getMemberWithFlow(currentMemberId)
                    .map { memberInfo ->
                        val attendances = memberInfo.getOrThrow().attendances
                        Result.success(sessionList to attendances)
                    }
                    .catch { exception ->
                        emit(Result.failure(exception))
                    }.collect { result ->
                        emit(result)
                    }
            }.onFailure { exception ->
                emit(Result.failure(exception))
            }
        }
    }
}
