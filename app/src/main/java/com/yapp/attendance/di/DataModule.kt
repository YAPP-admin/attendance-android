package com.yapp.attendance.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.yapp.data.datasource.*
import com.yapp.data.repository.LocalRepositoryImpl
import com.yapp.data.repository.MemberRepositoryImpl
import com.yapp.data.repository.RemoteConfigRepositoryImpl
import com.yapp.data.repository.TeamRepositoryImpl
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.repository.RemoteConfigRepository
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
        @ApplicationContext context: Context,
    ): LocalRepository {
        return LocalRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideMemberRepository(
        memberRemoteDataSource: MemberRemoteDataSource
    ): MemberRepository {
        return MemberRepositoryImpl(memberRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(
        teamDataSource: TeamRemoteDataSource
    ): TeamRepository {
        return TeamRepositoryImpl(teamDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteConfigRepository(
        remoteConfigDataSource: FirebaseRemoteConfigDataSource,
    ): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(remoteConfigDataSource)
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource {
        return FirebaseRemoteConfigDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideMemberRemoteDataSource(
        fireStore: FirebaseFirestore
    ): MemberRemoteDataSource {
        return MemberRemoteDataSourceImpl(fireStore)
    }

    @Provides
    @Singleton
    fun provideTeamRemoteDataSource(
        fireStore: FirebaseFirestore
    ): TeamRemoteDataSource {
        return TeamRemoteDataSourceImpl(fireStore)
    }

}