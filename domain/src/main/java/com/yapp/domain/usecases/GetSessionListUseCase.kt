package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import javax.inject.Inject

class GetSessionListUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(): Result<List<Session>> {
        return sessionRepository.getAllSession()
    }

}
