package com.yapp.data.datasource

import com.yapp.data.model.ConfigEntity
import com.yapp.data.model.SessionEntity
import com.yapp.data.model.TeamEntity
import com.yapp.data.model.VersionEntity


interface FirebaseRemoteConfigDataSource {
    suspend fun getMaginotlineTime(): String
    suspend fun getSessionList(): List<SessionEntity>
    suspend fun getConfig(): ConfigEntity
    suspend fun getTeamList(): List<TeamEntity>
    suspend fun getQrPassword(): String
    suspend fun shouldShowGuestButton(): Boolean
    suspend fun getVersionInfo(): VersionEntity
    suspend fun getSignUpPassword(): String
}
