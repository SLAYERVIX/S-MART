package com.example.s_mart.di

import android.content.Context
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.local.CartDao
import com.example.data.repo.LocalRepoImpl
import com.example.domain.repo.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLocalRepoImpl(cartDao: CartDao): LocalRepository {
        return LocalRepoImpl(cartDao)
    }
}