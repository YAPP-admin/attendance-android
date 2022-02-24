package com.yapp.attendance.di

import com.yapp.common.util.FirebaseRemoteConfigProvider
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
internal abstract class FirebaseModule {

    @Binds
    abstract fun provideFirebaseRemoteConfig(
        firebaseRemoteConfigProvider: FirebaseRemoteConfigProvider
    ): FirebaseRemoteConfig

}