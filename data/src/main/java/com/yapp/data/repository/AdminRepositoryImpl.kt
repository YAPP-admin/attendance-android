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

    override suspend fun checkAdmin(memberId: Long): Flow<Boolean> {
        return flow {
            val document = fireStore.adminRef()
                .document(memberId.toString())
                .get()
                .await()

            emit(document.exists())
        }
    }

    override suspend fun setAdmin(memberId: Long): Flow<Unit> {
        return flow {
            fireStore.adminRef()
                .document(memberId.toString())
                .set(memberId)
                .await()
        }
    }
}