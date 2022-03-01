package com.yapp.attendance

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        initKakao()
    }

    private fun initKakao() {
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}