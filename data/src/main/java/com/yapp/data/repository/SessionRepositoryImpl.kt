package com.yapp.data.repository

import com.yapp.data.datasource.SessionRemoteDataSource
import com.yapp.data.model.toDate
import com.yapp.data.model.toDomain
import com.yapp.domain.model.Session
import com.yapp.domain.repository.SessionRepository
import com.yapp.domain.util.DateParser
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val dateParser: DateParser
) : SessionRepository {
    override suspend fun setSession(session: Session): Result<Unit> {
        return runCatching {
            sessionRemoteDataSource.setSession(session.toDate(dateParser))
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
            sessionRemoteDataSource.getSession(id)?.toDomain(dateParser)
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
            sessionRemoteDataSource.getAllSession().map { entity -> entity.toDomain(dateParser) }
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
