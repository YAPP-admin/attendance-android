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
        date: String,
        description: String,
        code: String,
    ): Result<Unit> {
        return sessionRepository.setSession(
            session = Session(
                sessionId = 6,
                title = title,
                type = type,
                date = date,
                description = description,
                code = code
            )
        )
    }
}
