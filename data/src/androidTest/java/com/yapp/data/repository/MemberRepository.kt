package com.yapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.model.Member


internal class MemberRepository(
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun addMember() {
        val ref = db.collection("members").document("member1")

        ref.get()
            .addOnSuccessListener { snapshot ->
                val member1 = snapshot.toObject(Member::class.java)
                println(member1?.position)
            }
    }
}

fun main() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val ref = db.collection("members").document("member1")

    ref.get()
        .addOnSuccessListener { snapshot ->
            val member1 = snapshot.toObject(Member::class.java)
            println(member1?.position)
        }

}