package com.yapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface AdminRepository {
    suspend fun checkAdmin(memberId: Long): Flow<Boolean>
    suspend fun setAdmin(memberId: Long): Flow<Boolean>
}