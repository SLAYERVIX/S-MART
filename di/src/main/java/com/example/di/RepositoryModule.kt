package com.example.di

import com.example.data.bluetooth.BluetoothService
import com.example.data.remote.FireStoreService
import com.example.data.remote.FirebaseAuthService
import com.example.data.repo.BluetoothRepoImpl
import com.example.data.repo.FireStoreRepoImpl
import com.example.data.repo.FirebaseAuthRepoImpl
import com.example.domain.repo.BluetoothRepository
import com.example.domain.repo.FireStoreRepository
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

    @Provides
    fun provideFireStoreModule(fireStoreService: FireStoreService) : FireStoreRepository {
        return FireStoreRepoImpl(fireStoreService)
    }

    @Provides
    fun provideBluetoothRepository(bluetoothService: BluetoothService) : BluetoothRepository {
        return BluetoothRepoImpl(bluetoothService)
    }
}