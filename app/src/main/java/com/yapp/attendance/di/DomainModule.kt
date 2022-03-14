package com.yapp.attendance.di

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.usecases.GetTeamListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetTeamListUseCase(firebaseRemoteConfig: FirebaseRemoteConfig): GetTeamListUseCase {
        return GetTeamListUseCase(firebaseRemoteConfig)
    }

}