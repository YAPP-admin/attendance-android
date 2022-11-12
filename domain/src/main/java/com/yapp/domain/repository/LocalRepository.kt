package com.yapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface LocalRepository {
    suspend fun getMemberId(): Result<Long?>
    suspend fun setMemberId(memberId: Long): Result<Unit>
    suspend fun deleteAllUserInfo(): Result<Unit>
}