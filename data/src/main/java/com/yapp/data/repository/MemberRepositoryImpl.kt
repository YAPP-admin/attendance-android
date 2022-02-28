package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class MemberRepositoryImpl(
    private val fireStore: FirebaseFirestore
) : MemberRepository {

    override fun addMember(memberEntity: MemberEntity, generation: String): Flow<Boolean> {
        return flow {
            val memberReference = fireStore.collection("member").document(generation).collection("members")
            val snapshot = memberReference.add(memberEntity).await()

            emit(true)
        }
    }

    override fun getMember(id: Long, generation: String): Flow<MemberEntity?> {
        return flow {
            val memberReference = fireStore.collection("member").document(generation).collection("members")
            val snapshot = memberReference.whereEqualTo("id", id).get().await()

            if(snapshot.isEmpty) {
                emit(null)
                return@flow
            }

            emit(snapshot.first().toObject(MemberEntity::class.java))
        }
    }
}