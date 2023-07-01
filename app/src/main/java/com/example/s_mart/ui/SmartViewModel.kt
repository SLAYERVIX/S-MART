package com.example.s_mart.ui

import androidx.lifecycle.ViewModel
import com.example.data.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SmartViewModel : ViewModel() {
    val firebaseAuth = FirebaseAuth.getInstance()

    private val fireStore = FirebaseFirestore.getInstance()

    val productCollection = fireStore.collection(Constants.PRODUCTS_REF)

    val clientCollection = fireStore.collection(Constants.CLIENTS_REF)
    var clientDocument : DocumentReference = clientCollection.document(firebaseAuth.currentUser!!.uid)
}
