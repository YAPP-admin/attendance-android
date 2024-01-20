package com.yapp.domain.usecases

import com.yapp.domain.repository.SessionRepository
import javax.inject.Inject

class CheckSessionPasswordUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(sessionId: Int, inputPassword: String): Result<Boolean> {
        return sessionRepository.getSession(id = sessionId).mapCatching { session ->
            check(session != null) { "${sessionId}에 해당하는 Session이 존재하지 않습니다" }

            inputPassword == session.code
        }
    }
}