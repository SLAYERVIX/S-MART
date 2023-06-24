package com.example.di

import com.example.data.Constants
import com.example.data.remote.FireStoreService
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
    @ProductCollectionReference
    @Singleton
    fun provideDealCollectionRef(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(Constants.PRODUCTS_REF)
    }

    @Provides
    @Singleton
    fun provideFireStoreService(
        @CategoryCollectionReference categoryCollectionReference: CollectionReference,
        @ProductCollectionReference productCollectionReference: CollectionReference
    ): FireStoreService {
        return FireStoreService(categoryCollectionReference,productCollectionReference)
    }
}