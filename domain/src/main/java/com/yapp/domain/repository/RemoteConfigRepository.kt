package com.yapp.domain.repository

import com.yapp.domain.model.Config
import com.yapp.domain.model.Session
import com.yapp.domain.model.Team
import com.yapp.domain.model.Version


interface RemoteConfigRepository {
    suspend fun getMaginotlineTime(): Result<String>
    suspend fun getSessionList(): Result<List<Session>>
    suspend fun getConfig(): Result<Config>
    suspend fun getTeamList(): Result<List<Team>>
    suspend fun getQrPassword(): Result<String>
    suspend fun shouldShowGuestButton(): Result<Boolean>
    suspend fun getVersionInfo(): Result<Version>
    suspend fun getSignUpPassword(): Result<String>
}
