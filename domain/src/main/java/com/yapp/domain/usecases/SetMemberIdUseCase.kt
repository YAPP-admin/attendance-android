package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import javax.inject.Inject

class SetMemberIdUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<Unit, Long?>(coroutineDispatcher) {
    override suspend fun invoke(params: Long?) {
        localRepository.setMemberId(params!!)
    }
}