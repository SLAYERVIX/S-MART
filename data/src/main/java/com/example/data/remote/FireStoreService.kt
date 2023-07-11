package com.example.data.remote

import android.util.Log
import com.example.data.Constants
import com.example.domain.entity.Category
import com.example.domain.entity.Client
import com.example.domain.entity.CoinsProduct
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.example.s_mart.core.utils.calcDiscount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class FireStoreService(
    private val categoryReference: CollectionReference,
    private val productReference: CollectionReference,
    private val clientReference: CollectionReference,
    private val voucherReference: CollectionReference,
    private val coinsProductReference: CollectionReference,
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

    fun retrieveDeals(): Flow<List<Product>?> = callbackFlow {
        productReference.whereEqualTo(Constants.DEAL_TYPE_FIELD, true).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val products = snapshot.toObjects(Product::class.java)
                    trySend(products)
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
                        "retrieveClient: ${error.message}"
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
                    Log.e(
                        FireStoreService::class.simpleName,
                        "retrieveCategorizedProducts: ${it.message}"
                    )
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

    fun updateAppliedVoucher(voucher: Voucher) {
        firebaseAuth.currentUser?.let { user ->
            clientReference.document(user.uid).get().addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val client = snapshot.toObject(Client::class.java)
                    client?.let {
                        it.cart.appliedVoucher = voucher
                        clientReference.document(user.uid).set(it)
                    }
                }
                task.addOnFailureListener { exception ->
                    Log.e(
                        FireStoreService::class.simpleName,
                        "updateClientVouchers: ${exception.message}"
                    )
                }
            }
        }
    }

    fun deleteProductFromCart(product: Product) {
        firebaseAuth.currentUser?.let { user ->
            clientReference.document(user.uid).get().addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val client = snapshot.toObject(Client::class.java)

                    client?.let {
                        it.cart.products.remove(product)
                        it.cart.totalPrice -= calcDiscount(
                            product.price,
                            product.discountPercentage
                        )

                        clientReference.document(user.uid).set(it)
                    }
                }
                task.addOnFailureListener {
                    Log.e(
                        FireStoreService::class.simpleName,
                        "deleteProductFromCart: ${it.message}"
                    )
                }
            }
        }
    }

    fun clearProductsFromCart() {
        firebaseAuth.currentUser?.let { user ->
            clientReference.document(user.uid).get().addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val client = snapshot.toObject(Client::class.java)
                    client?.let {
                        it.cart.products.clear()
                        it.cart.totalPrice = 0.0

                        clientReference.document(user.uid).set(it)
                    }
                }
                task.addOnFailureListener {
                    Log.e(
                        FireStoreService::class.simpleName,
                        "clearProductsFromCart: ${it.message}"
                    )
                }
            }
        }
    }

    fun retrieveProductByBarcode(barcode: String): Flow<Product> = callbackFlow {
        productReference.whereEqualTo("barcode", barcode).limit(1).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val products = snapshot.toObjects(Product::class.java)

                    if (products[0] != null) {
                        trySend(products[0])
                    }
                }
                task.addOnFailureListener {
                    Log.e(
                        FireStoreService::class.simpleName,
                        "retrieveProductByBarcode: ${it.message}"
                    )
                }
            }
        awaitClose()
    }

    fun addProductToCart(product: Product) {
        firebaseAuth.currentUser?.let { user ->
            clientReference.document(user.uid).get().addOnCompleteListener { task ->
                task.addOnSuccessListener { snapshot ->
                    val client = snapshot.toObject(Client::class.java)
                    val uuid = UUID.randomUUID().toString()

                    client?.let {
                        it.cart.products.add(product.copy(_id = uuid))
                        it.cart.totalPrice += calcDiscount(
                            product.price,
                            product.discountPercentage
                        )
                        clientReference.document(user.uid).set(it)
                    }
                }
                task.addOnFailureListener {
                    Log.e(
                        FireStoreService::class.simpleName,
                        "addProductToCart: ${it.message}"
                    )
                }
            }
        }
    }

    fun retrieveCoinsProducts() : Flow<List<CoinsProduct>> = callbackFlow {
        coinsProductReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                trySend(snapshot.toObjects(CoinsProduct::class.java))
            }
            task.addOnFailureListener {
                Log.e(FireStoreService::class.simpleName, "retrieveVouchers: ${it.message}")
            }
        }
        awaitClose()
    }
}