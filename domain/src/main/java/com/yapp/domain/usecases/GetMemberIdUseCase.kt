package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import javax.inject.Inject

class GetMemberIdUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(): Result<Long?> {
        return localRepository.getMemberId()
    }

}