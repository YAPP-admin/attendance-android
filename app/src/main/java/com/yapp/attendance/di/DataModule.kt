package com.yapp.attendance.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.repository.LocalRepositoryImpl
import com.yapp.data.repository.MemberRepositoryImpl
import com.yapp.data.repository.TeamRepositoryImpl
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.TeamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(
        @ApplicationContext context: Context
    ): LocalRepository {
        return LocalRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideMemberRepository(
        fireStore: FirebaseFirestore
    ): MemberRepository {
        return MemberRepositoryImpl(fireStore)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(
        fireStore: FirebaseFirestore
    ) : TeamRepository {
        return TeamRepositoryImpl(fireStore)
    }

}