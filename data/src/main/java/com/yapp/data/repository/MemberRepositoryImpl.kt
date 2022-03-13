package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.mapTo
import com.yapp.data.util.memberRef
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
        }
    }

    override fun getMember(id: Long): Flow<MemberEntity?> {
        return flow {
            val document = fireStore.memberRef()
                .document(id.toString())
                .get()
                .await()

            if (document.exists()) {
                emit(document.toObject(MemberModel::class.java)?.mapTo())
            }

            emit(null)
            return@flow
        }
    }

}