package com.yapp.attendance

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.yapp.attendance.BuildConfig.KAKAO_NATIVE_API_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        initKakao()
    }

    private fun initKakao() {
        KakaoSdk.init(this, KAKAO_NATIVE_API_KEY)
    }
}