package com.yapp.domain.firebase

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfig {
    suspend fun getMaginotlineTime(): Flow<String>
    suspend fun getSessionList(): Flow<List<SessionEntity>>
    suspend fun getConfig(): Flow<ConfigEntity>
    suspend fun getTeamList(): Flow<List<TeamEntity>>
}