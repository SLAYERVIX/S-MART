package com.example.data.remote

import android.util.Log
import com.example.data.Constants
import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FireStoreService(
    private val categoryReference: CollectionReference,
    private val productReference: CollectionReference,
    private val clientReference: CollectionReference,
    private val voucherReference: CollectionReference,
    private val firebaseAuth: FirebaseAuth,
) {
    fun retrieveCategories(): Flow<List<Category>> = callbackFlow {
        categoryReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val categories = snapshot.toObjects(Category::class.java)

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

    fun retrieveClient(): Flow<Client?> = callbackFlow {
        firebaseAuth.currentUser?.let { user ->
            clientReference.document(user.uid).addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(
                        FireStoreService::class.simpleName,
                        "retrieveDealOfTheDay: ${error.message}"
                    )
                }
                value?.let {
                    val client = value.toObject(Client::class.java)
                    client?.let {
                        trySend(it)
                    }
                }
            }
        }
        awaitClose()
    }

    fun retrieveVouchers(): Flow<List<Voucher>> = callbackFlow {
        voucherReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                trySend(snapshot.toObjects(Voucher::class.java))
            }
            task.addOnFailureListener {
                Log.e(FireStoreService::class.simpleName, "retrieveVouchers: ${it.message}")
            }
        }
        awaitClose()
    }

    fun retrieveCategorizedProducts(categoryName: String): Flow<List<Product>> = callbackFlow {
        productReference.whereEqualTo(Constants.PRODUCT_CATEGORY_NAME, categoryName).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    trySend(snapshot.toObjects(Product::class.java))
                }
                task.addOnFailureListener {
                    Log.e(FireStoreService::class.simpleName, "retrieveVouchers: ${it.message}")
                }
            }

        awaitClose()
    }

    fun createClientInstance() {
        firebaseAuth.currentUser?.let {
            clientReference.document(it.uid).set(Client())
        }
    }

    fun updateClient(client: Client) {
        firebaseAuth.currentUser?.let {
            clientReference.document(it.uid).set(client)
        }
    }
}