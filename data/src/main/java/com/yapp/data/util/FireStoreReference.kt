package com.yapp.data.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


fun FirebaseFirestore.memberRef(generation: String, memberId: Long): DocumentReference {
    return this.collection("member")
        .document(generation)
        .collection("members")
        .document(memberId.toString())
}

fun FirebaseFirestore.adminRef(generation: String): CollectionReference {
    return this.collection("admin")
        .document(generation)
        .collection("members")
}