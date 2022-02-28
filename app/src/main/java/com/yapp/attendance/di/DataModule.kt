package com.yapp.attendance.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.repository.LocalRepositoryImpl
import com.yapp.data.repository.MemberRepositoryImpl
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideLocalRepository(
        @ApplicationContext context: Context
    ): LocalRepository {
        return LocalRepositoryImpl(context)
    }

    @Provides
    fun provideMemberRepository(
        fireStore: FirebaseFirestore
    ): MemberRepository {
        return MemberRepositoryImpl(fireStore)
    }

}