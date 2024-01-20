package com.yapp.domain.usecases

import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateParser
import javax.inject.Inject

class SetSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dateParser: DateParser
) {
    suspend operator fun invoke(
        title: String,
        type: NeedToAttendType,
        rawDate: String,
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
                    startTime = dateParser.parse(rawDate = rawDate),
                    description = description,
                    code = code
                )
            )
        }
    }
}
