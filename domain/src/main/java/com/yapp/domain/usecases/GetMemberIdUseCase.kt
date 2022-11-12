package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.Resources
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemberIdUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend fun invoke(params: Unit?): Result<Long?> {
        return localRepository.getMemberId()
    }

}