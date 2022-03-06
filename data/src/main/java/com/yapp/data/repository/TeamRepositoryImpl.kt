package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.mapTo
import com.yapp.data.util.memberRef
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class TeamRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : TeamRepository {

    override fun getTeamMembers(team: String): Flow<List<MemberEntity>> {
        return flow {
            val documents = fireStore.memberRef()
                .whereEqualTo("team", team)
                .get()
                .await()

            if (documents.isEmpty) {
                emit(emptyList())
                return@flow
            }

            emit(documents.toObjects(MemberModel::class.java).map { it.mapTo() })
        }
    }

}