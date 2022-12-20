package com.yapp.data.repository

import com.yapp.data.datasource.MemberRemoteDataSource
import com.yapp.data.model.toData
import com.yapp.data.model.toDomain
import com.yapp.domain.model.Member
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {

    override suspend fun setMember(member: Member): Result<Unit> {
        return runCatching {
            memberRemoteDataSource.setMember(member.toData())
        }.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getMember(id: Long): Result<Member?> {
        return runCatching {
            memberRemoteDataSource.getMember(id)?.toDomain()
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
        return runCatching {
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

    override suspend fun getAllMember(): Flow<Result<List<Member>>> {
        return memberRemoteDataSource.getAllMember()
            .map { entities ->
                Result.success(entities.map { it.toDomain() })
            }
    }

}