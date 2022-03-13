package com.yapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface LocalRepository {
    fun getMemberId(): Flow<Long?>
    fun setMemberId(memberId: Long): Flow<Unit>
}