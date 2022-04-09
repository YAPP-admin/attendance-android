package com.yapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface LocalRepository {
    suspend fun getMemberId(): Flow<Long?>
    suspend fun setMemberId(memberId: Long)
}