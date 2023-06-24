package com.example.data.remote

import android.util.Log
import com.example.data.Constants
import com.example.domain.entity.Category
import com.example.domain.entity.Product
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class FireStoreService(
    private val categoryReference: CollectionReference,
    private val productReference: CollectionReference
) {
    fun retrieveCategories(): Flow<List<Category>> = callbackFlow {
        categoryReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val categories = mutableListOf<Category>()

                snapshot.forEach { document ->
                    val category = document.toObject(Category::class.java)
                    categories.add(category)
                }

                trySend(categories)
            }
            task.addOnFailureListener {
                Log.e(FireStoreService::class.simpleName, "retrieveCategories: ${it.message}")
            }
        }
        awaitClose()
    }

    fun retrieveDealOfTheDay(): Flow<Product?> = callbackFlow {
        productReference.whereEqualTo(Constants.DEAL_TYPE_FIELD, true).limit(1).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val product = snapshot.documents.first().toObject(Product::class.java)

                    trySend(product)
                }
                task.addOnFailureListener {
                    Log.e(FireStoreService::class.simpleName, "retrieveDealOfTheDay: ${it.message}")
                }
            }
        awaitClose()
    }
}