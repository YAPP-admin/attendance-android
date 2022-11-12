package com.yapp.data.repository

import com.yapp.data.remote.MemberRemoteDataSource
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {

    override suspend fun setMember(memberEntity: MemberEntity): Result<Unit> {
        return kotlin.runCatching {
            memberRemoteDataSource.setMember(memberEntity)
        }.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getMember(id: Long): Result<MemberEntity?> {
        return kotlin.runCatching {
            memberRemoteDataSource.getMember(id)
        }.fold(
            onSuccess = { memberEntity ->
                Result.success(memberEntity)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun deleteMember(id: Long): Result<Unit> {
        return kotlin.runCatching {
            memberRemoteDataSource.deleteMember(id)
        }.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getAllMember(): Flow<Result<List<MemberEntity>>> {
        return memberRemoteDataSource.getAllMember()
            .map { Result.success(it) }
    }

}