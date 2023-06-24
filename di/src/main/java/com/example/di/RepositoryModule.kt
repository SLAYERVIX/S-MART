package com.example.di

import com.example.data.remote.FirebaseAuthService
import com.example.data.repo.FirebaseAuthRepoImpl
import com.example.domain.repo.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideFirebaseAuthRepository(firebaseAuthService: FirebaseAuthService) : FirebaseAuthRepository {
        return FirebaseAuthRepoImpl(firebaseAuthService)
    }
}