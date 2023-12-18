package com.yapp.data.repository

import com.yapp.data.datasource.SessionRemoteDataSource
import com.yapp.data.model.toData
import com.yapp.data.model.toDomain
import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
) : SessionRepository {
    override suspend fun setSession(session: Session): Result<Unit> {
        return runCatching {
            sessionRemoteDataSource.setSession(session.toData())
        }.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getSession(id: Long): Result<Session?> {
        return runCatching {
            sessionRemoteDataSource.getSession(id)?.toDomain()
        }.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getAllSession(): Result<List<Session>> {
        return runCatching {
            sessionRemoteDataSource.getAllSession().map {
                it.toDomain()
            }
        }.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}
