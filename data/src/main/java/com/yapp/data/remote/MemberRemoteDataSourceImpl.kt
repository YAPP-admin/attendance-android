package com.yapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.mapToEntity
import com.yapp.data.util.memberRef
import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


internal class MemberRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : MemberRemoteDataSource {

    override suspend fun setMember(memberEntity: MemberEntity) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .document(memberEntity.id.toString())
                .set(memberEntity)
                .addOnSuccessListener {
                    cancellableContinuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getMember(id: Long): MemberEntity? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .document(id.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists() == false) {
                        cancellableContinuation.resume(null)
                        return@addOnSuccessListener
                    }

                    document.toObject(MemberModel::class.java)?.mapToEntity()
                        .also { entity -> cancellableContinuation.resume(entity) }
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun deleteMember(id: Long) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .document(id.toString())
                .delete()
                .addOnSuccessListener {
                    cancellableContinuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getAllMember(): Flow<List<MemberEntity>> {
        return callbackFlow<List<MemberEntity>> {
            fireStore.memberRef()
                .addSnapshotListener { snapshot, error ->
                    require(error == null)

                    if (snapshot == null || snapshot.documents.isEmpty()) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val entities = snapshot.documents.map { document ->
                        document.toObject(MemberModel::class.java)!!.mapToEntity()
                    }

                    trySend(entities)
                }
        }
    }
}