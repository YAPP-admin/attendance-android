package com.yapp.domain.usecases

import com.yapp.domain.DefaultDispatcher
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CheckLocalMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository,
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<Flow<TaskResult<Boolean>>, Unit>(coroutineDispatcher) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<Boolean>> {
        return localRepository.getMemberId()
            .map { id ->
                id != null
            }.toResult()
    }
}