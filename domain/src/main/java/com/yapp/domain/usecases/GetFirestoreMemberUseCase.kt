package com.yapp.domain.usecases

import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetFirestoreMemberUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<MemberEntity?>>, Unit>(coroutineDispatcher) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<MemberEntity?>> {
        val userId = localRepository.getMemberId().firstOrNull()

        return userId?.let { id ->
            memberRepository.getMember(id).toResult()
        } ?: emptyFlow()
    }
}