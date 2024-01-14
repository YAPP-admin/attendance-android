package com.yapp.data.datasource

import com.yapp.data.model.SessionEntity

interface SessionRemoteDataSource {
    suspend fun setSession(session: SessionEntity)
    suspend fun getSession(id: Long): SessionEntity?
    suspend fun getAllSession(): List<SessionEntity>
}
