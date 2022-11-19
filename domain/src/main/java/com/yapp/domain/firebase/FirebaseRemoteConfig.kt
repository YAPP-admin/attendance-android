package com.yapp.domain.firebase

import com.yapp.domain.model.Config
import com.yapp.domain.model.Session
import com.yapp.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfig {
    suspend fun getMaginotlineTime(): Flow<String>
    suspend fun getSessionList(): Flow<List<Session>>
    suspend fun getConfig(): Flow<Config>
    suspend fun getTeamList(): Flow<List<Team>>
    suspend fun getQrPassword(): Flow<String>
    suspend fun shouldShowGuestButton(): Flow<Boolean>
}
