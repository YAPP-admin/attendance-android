package com.yapp.attendance.di

import com.yapp.common.util.KakaoSdkProvider
import com.yapp.domain.common.KakaoSdkProviderInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KakaoSdkModule {

    @Binds
    @Singleton
    abstract fun bindsKakaoSdkModule(
        kakaoSdkProvider: KakaoSdkProvider
    ): KakaoSdkProviderInterface

}