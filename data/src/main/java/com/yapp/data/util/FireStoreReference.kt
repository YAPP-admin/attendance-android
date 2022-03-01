package com.yapp.data.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


fun FirebaseFirestore.memberRef(memberId: Long): DocumentReference {
    return this.collection("member")
        .document(memberId.toString())
}

fun FirebaseFirestore.adminRef(memberId: Long): DocumentReference {
    return this.collection("admin")
        .document(memberId.toString())
}