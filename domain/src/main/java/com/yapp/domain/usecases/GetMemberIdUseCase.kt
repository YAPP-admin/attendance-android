package com.yapp.domain.usecases

import com.yapp.domain.DefaultDispatcher
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemberIdUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<Flow<TaskResult<Long?>>, Unit>(coroutineDispatcher) {
    override suspend fun invoke(params: Unit?): Flow<TaskResult<Long?>> {
        return localRepository.getMemberId().toResult()
    }
}