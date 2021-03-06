package com.yapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.mapToEntity
import com.yapp.data.util.memberRef
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject


class MemberRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : MemberRepository {

    override fun setMember(memberEntity: MemberEntity): Flow<Unit> {
        return flow {
            fireStore.memberRef()
                .document(memberEntity.id.toString())
                .set(memberEntity)
                .await()
            emit(Unit)
        }
    }

    override fun getMember(id: Long): Flow<MemberEntity?> {
        return flow {
            val document = fireStore.memberRef()
                .document(id.toString())
                .get()
                .await()

            if (document.exists()) {
                emit(document.toObject(MemberModel::class.java)?.mapToEntity())
                return@flow
            }

            emit(null)
        }
    }

    override fun deleteMember(id: Long): Flow<Boolean> {
        return callbackFlow {
            fireStore.memberRef()
                .document(id.toString())
                .delete()
                .addOnCompleteListener {
                    trySend(it.isSuccessful)
                }

            awaitClose {
                close()
            }
        }
    }

    override fun getAllMember(): Flow<List<MemberEntity>> {
        return flow {
            val snapshot = fireStore.memberRef()
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                val entities = snapshot.documents.map { document ->
                    val model = document.toObject(MemberModel::class.java)
                    val entity = model!!.mapToEntity()
                    entity
                }
                emit(entities)
                return@flow
            }

            emit(emptyList())
        }
    }

}