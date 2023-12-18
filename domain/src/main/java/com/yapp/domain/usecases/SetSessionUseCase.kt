package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.repository.SessionRepository
import javax.inject.Inject

class SetSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(
        title: String,
        type: NeedToAttendType,
        startTime: String,
        description: String,
        code: String,
    ): Result<Unit> {
        return sessionRepository.setSession(
            session = Session(
                sessionId = 6,
                title = title,
                type = type,
                startTime = startTime,
                description = description,
                code = code
            )
        )
    }
}
