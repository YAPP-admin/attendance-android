package com.yapp.data.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.BuildConfig


fun FirebaseFirestore.memberRef(): CollectionReference {
    return this.collection(BuildConfig.COLLECTION_NAME)
}
