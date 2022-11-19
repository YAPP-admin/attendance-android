package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import javax.inject.Inject

class SetMemberIdUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(params: Long) {
        localRepository.setMemberId(params)
    }

}