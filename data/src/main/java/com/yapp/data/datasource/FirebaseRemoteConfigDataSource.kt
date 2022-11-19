package com.yapp.data.datasource

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity


interface FirebaseRemoteConfigDataSource {
    suspend fun getMaginotlineTime(): String
    suspend fun getSessionList(): List<SessionEntity>
    suspend fun getConfig(): ConfigEntity
    suspend fun getTeamList(): List<TeamEntity>
    suspend fun getQrPassword(): String
    suspend fun shouldShowGuestButton(): Boolean
}