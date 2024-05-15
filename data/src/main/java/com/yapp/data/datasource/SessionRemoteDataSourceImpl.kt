package com.yapp.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.SessionEntity
import com.yapp.data.util.sessionRef
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SessionRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : SessionRemoteDataSource {

    override suspend fun setSession(session: SessionEntity) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.sessionRef()
                .document(session.sessionId.toString())
                .set(session)
                .addOnSuccessListener {
                    cancellableContinuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getSession(id: Int): SessionEntity? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.sessionRef()
                .document(id.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        cancellableContinuation.resume(null)
                        return@addOnSuccessListener
                    }

                    document.toObject(SessionEntity::class.java).also { entity ->
                        cancellableContinuation.resume(entity)
                    }
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getAllSession(): List<SessionEntity> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.sessionRef()
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        cancellableContinuation.resume(emptyList())
                        return@addOnSuccessListener
                    }

                    documents.toObjects(SessionEntity::class.java).also { entity ->
                        cancellableContinuation.resume(entity)
                    }
                }
                .addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }
}
