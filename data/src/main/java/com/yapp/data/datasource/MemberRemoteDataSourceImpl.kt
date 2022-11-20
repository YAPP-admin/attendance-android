package com.yapp.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberEntity
import com.yapp.data.util.memberRef
import com.yapp.domain.model.Member
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class MemberRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : MemberRemoteDataSource {

    override suspend fun setMember(member: MemberEntity) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .document(member.id.toString())
                .set(member)
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

                    document.toObject(MemberEntity::class.java).also { entity ->
                        cancellableContinuation.resume(entity)
                    }
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
                        document.toObject(MemberEntity::class.java)!!
                    }

                    trySend(entities)
                }
            awaitClose()
        }
    }
}