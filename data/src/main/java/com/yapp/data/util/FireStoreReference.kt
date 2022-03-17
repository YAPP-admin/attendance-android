package com.yapp.data.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


fun FirebaseFirestore.memberRef(): CollectionReference {
    return this.collection("member")
}