package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.MemberModel
import com.yapp.data.model.MemberModel.Companion.toEntity
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

    override fun setMember(memberEntity: MemberEntity, generation: String): Flow<Boolean> {
        return flow {
            runCatching {
                fireStore
                    .memberRef(generation, memberEntity.id!!)
                    .set(memberEntity)
                    .await()
            }.fold(
                onSuccess = {
                    emit(true)
                },
                onFailure = {
                    emit(false)
                }
            )
        }
    }

    override fun getMember(id: Long, generation: String): Flow<MemberEntity?> {
        return flow {
            runCatching {
                fireStore
                    .memberRef(generation, id)
                    .get()
                    .await()
            }.fold(
                onSuccess = { document ->
                    if (document.exists()) {
                        emit(document.toObject(MemberModel::class.java)?.toEntity())
                    } else {
                        emit(null)
                        return@flow
                    }
                },
                onFailure = {
                    emit(null)
                    return@flow
                }
            )
        }
    }
}