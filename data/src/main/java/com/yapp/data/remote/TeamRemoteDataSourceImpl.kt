package com.yapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.mapToEntity
import com.yapp.data.util.memberRef
import com.yapp.domain.model.MemberEntity
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


internal class TeamRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : TeamRemoteDataSource {

    override suspend fun getTeamMembers(team: String): List<MemberEntity> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            fireStore.memberRef()
                .whereEqualTo("team", team)
                .get()
                .addOnSuccessListener { documents ->
                    if(documents.isEmpty) {
                        cancellableContinuation.resume(emptyList())
                        return@addOnSuccessListener
                    }

                    documents.toObjects(MemberModel::class.java).map { it.mapToEntity() }
                        .also { entities -> cancellableContinuation.resume(entities) }

                }.addOnFailureListener { exception ->
                    cancellableContinuation.resumeWithException(exception)
                }
        }
    }

}