package com.yapp.data.repository

import com.yapp.data.datasource.FirebaseRemoteConfigDataSource
import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


internal class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfigDataSource: FirebaseRemoteConfigDataSource
) : RemoteConfigRepository {

    override suspend fun getMaginotlineTime(): Result<String> {
        return runCatching {
            firebaseRemoteConfigDataSource.getMaginotlineTime()
        }.fold(
            onSuccess = { maginotlineTime: String ->
                Result.success(maginotlineTime)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getSessionList(): Result<List<SessionEntity>> {
        return runCatching {
            firebaseRemoteConfigDataSource.getSessionList()
        }.fold(
            onSuccess = { sessionList: List<SessionEntity> ->
                Result.success(sessionList)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getConfig(): Result<ConfigEntity> {
        return runCatching {
            firebaseRemoteConfigDataSource.getConfig()
        }.fold(
            onSuccess = { config: ConfigEntity ->
                Result.success(config)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getTeamList(): Result<List<TeamEntity>> {
        return runCatching {
            firebaseRemoteConfigDataSource.getTeamList()
        }.fold(
            onSuccess = { temaList: List<TeamEntity> ->
                Result.success(temaList)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getQrPassword(): Result<String> {
        return runCatching {
            firebaseRemoteConfigDataSource.getQrPassword()
        }.fold(
            onSuccess = { qrPassword: String ->
                Result.success(qrPassword)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun shouldShowGuestButton(): Result<Boolean> {
        return runCatching {
            firebaseRemoteConfigDataSource.shouldShowGuestButton()
        }.fold(
            onSuccess = { shouldShowGuestButton: Boolean ->
                Result.success(shouldShowGuestButton)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

}