package com.yapp.data.repository

import com.yapp.data.datasource.FirebaseRemoteConfigDataSource
import com.yapp.data.model.ConfigEntity
import com.yapp.data.model.SessionEntity
import com.yapp.data.model.TeamEntity
import com.yapp.data.model.toDomain
import com.yapp.domain.model.Config
import com.yapp.domain.model.Session
import com.yapp.domain.model.Team
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject


class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfigDataSource: FirebaseRemoteConfigDataSource,
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

    override suspend fun getSessionList(): Result<List<Session>> {
        return runCatching {
            firebaseRemoteConfigDataSource.getSessionList()
        }.fold(
            onSuccess = { entities: List<SessionEntity> ->
                Result.success(entities.map { it.toDomain() })
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getConfig(): Result<Config> {
        return runCatching {
            firebaseRemoteConfigDataSource.getConfig()
        }.fold(
            onSuccess = { entity: ConfigEntity ->
                Result.success(entity.toDomain())
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getTeamList(): Result<List<Team>> {
        return runCatching {
            firebaseRemoteConfigDataSource.getTeamList()
        }.fold(
            onSuccess = { entities: List<TeamEntity> ->
                Result.success(entities.map { it.toDomain() })
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

    override suspend fun getSignUpPassword(): Result<String> {
        return runCatching {
            firebaseRemoteConfigDataSource.getSignUpPassword()
        }.fold(
            onSuccess = { signUpPassword: String ->
                Result.success(signUpPassword)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}