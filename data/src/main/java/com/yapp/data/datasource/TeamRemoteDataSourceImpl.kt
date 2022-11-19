package com.yapp.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberEntity
import com.yapp.data.util.memberRef
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class TeamRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : TeamRemoteDataSource {

    override suspend fun getTeamMembers(team: String): List<MemberEntity> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .whereEqualTo("team", team)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        cancellableContinuation.resume(emptyList())
                        return@addOnSuccessListener
                    }

                    documents.toObjects(MemberEntity::class.java)
                        .also { entities -> cancellableContinuation.resume(entities) }

                }.addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

}