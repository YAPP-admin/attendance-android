package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import javax.inject.Inject

class CheckLocalMemberUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(): Result<Boolean> {
        return localRepository.getMemberId().mapCatching { localMemberId ->
            localMemberId != null
        }
    }

}