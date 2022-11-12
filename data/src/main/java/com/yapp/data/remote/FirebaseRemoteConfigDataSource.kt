package com.yapp.data.remote

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity
import kotlinx.coroutines.flow.Flow


interface FirebaseRemoteConfigDataSource {
    suspend fun getMaginotlineTime(): String
    suspend fun getSessionList(): List<SessionEntity>
    suspend fun getConfig(): ConfigEntity
    suspend fun getTeamList(): List<TeamEntity>
    suspend fun getQrPassword(): String
    suspend fun shouldShowGuestButton(): Boolean
}