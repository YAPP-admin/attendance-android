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
        return sessionRepository.getAllSession().mapCatching { sessions: List<Session> ->
            val lastSessionId = sessions.last().sessionId
            val newSessionId = lastSessionId + 1

            // 새롭게 생성할 세션의 ID는 현재 DB 내에 존재하지 않아야 한다.
            check(sessions.any { it.sessionId == newSessionId }.not())

            sessionRepository.setSession(
                session = Session(
                    sessionId = newSessionId,
                    title = title,
                    type = type,
                    startTime = startTime,
                    description = description,
                    code = code
                )
            )
        }
    }
}
