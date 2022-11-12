package com.yapp.domain.repository

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity


interface RemoteConfigRepository {
    suspend fun getMaginotlineTime(): Result<String>
    suspend fun getSessionList(): Result<List<SessionEntity>>
    suspend fun getConfig(): Result<ConfigEntity>
    suspend fun getTeamList(): Result<List<TeamEntity>>
    suspend fun getQrPassword(): Result<String>
    suspend fun shouldShowGuestButton(): Result<Boolean>
}