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

    private val dealOfTheDayCollection = fireStore.collection(Constants.TODAY_DEAL_REF)
    val dealOfTheDayDocument = dealOfTheDayCollection.document(Constants.DEAL)

    val categoryCollection = fireStore.collection(Constants.CATEGORIES_REF)

    val clientCollection = fireStore.collection(Constants.CLIENTS_REF)
    var clientDocument : DocumentReference = clientCollection.document(firebaseAuth.currentUser!!.uid)

    fun reInitializeDocuments() {
        clientDocument  = clientCollection.document(firebaseAuth.currentUser!!.uid)
    }
}
