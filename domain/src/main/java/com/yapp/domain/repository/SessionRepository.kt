package com.yapp.domain.repository

import com.yapp.domain.model.Session

interface SessionRepository {
    suspend fun setSession(session: Session): Result<Unit>
    suspend fun getSession(id: Long): Result<Session?>
    suspend fun getAllSession(): Result<List<Session>>
}
