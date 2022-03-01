package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.util.adminRef
import com.yapp.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AdminRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : AdminRepository {

    override suspend fun checkMemberIsAdmin(memberId: Long): Flow<Boolean> {
        return flow {
            runCatching {
                fireStore
                    .adminRef(memberId)
                    .get()
                    .await()
            }.fold(
                onSuccess = { document ->
                    if (document.exists()) {
                        emit(true)
                    } else {
                        emit(false)
                        return@flow
                    }
                },
                onFailure = {
                    emit(false)
                    return@flow
                }
            )
        }
    }

    override suspend fun setAdmin(memberId: Long): Flow<Boolean> {
        return flow {
            fireStore.adminRef(memberId)
                .set(memberId)
                .await()

            emit(true)
        }
    }
}