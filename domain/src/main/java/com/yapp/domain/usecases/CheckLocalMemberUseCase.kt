package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.DispatcherProvider
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