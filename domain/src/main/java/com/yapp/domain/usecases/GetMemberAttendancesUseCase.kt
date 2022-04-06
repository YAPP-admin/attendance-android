package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemberAttendancesUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<List<AttendanceEntity>?>>, Unit>() {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<List<AttendanceEntity>?>> {
        var memberId: Long = -1
        localRepository.getMemberId().collect { memberId = it ?: -1 }

        return memberRepository.getMember(memberId).map { it?.attendances }.toResult()
    }
}