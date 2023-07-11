package com.example.di

import com.example.data.Constants
import com.example.data.remote.FireStoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CategoryCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ProductCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ClientCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CoinsProductsCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class VoucherCollectionReference

    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @CategoryCollectionReference
    @Singleton
    fun provideCategoryCollectionRef(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.CATEGORIES_REF)
    }

    @Provides
    @CoinsProductsCollectionReference
    @Singleton
    fun provideCoinsProductsCollectionRef(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.COINS_PRODUCTS)
    }

    @Provides
    @ProductCollectionReference
    @Singleton
    fun provideProductReference(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.PRODUCTS_REF)
    }

    @Provides
    @ClientCollectionReference
    @Singleton
    fun provideClientReference(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.CLIENTS_REF)
    }

    @Provides
    @VoucherCollectionReference
    @Singleton
    fun provideVoucherReference(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.VOUCHERS_REF)
    }

    @Provides
    @Singleton
    fun provideFireStoreService(
        @CategoryCollectionReference categoryCollectionReference: CollectionReference,
        @ProductCollectionReference productCollectionReference: CollectionReference,
        @ClientCollectionReference clientCollectionReference: CollectionReference,
        @VoucherCollectionReference voucherCollectionReference: CollectionReference,
        @CoinsProductsCollectionReference coinsProductsCollectionReference: CollectionReference,
        firebaseAuth: FirebaseAuth
    ): FireStoreService {
        return FireStoreService(
            categoryCollectionReference,
            productCollectionReference,
            clientCollectionReference,
            voucherCollectionReference,
            coinsProductsCollectionReference,
            firebaseAuth
        )
    }
}