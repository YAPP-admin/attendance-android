package com.yapp.attendance.di

import com.yapp.data.firebase.FirebaseRemoteConfigProvider
import com.yapp.domain.firebase.FirebaseRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class FirebaseModule {

    @Binds
    @Singleton
    abstract fun provideFirebaseRemoteConfig(
        firebaseRemoteConfigProvider: FirebaseRemoteConfigProvider
    ): FirebaseRemoteConfig

}