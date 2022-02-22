package com.yapp.attendance

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.yapp.presentation.ui.member.main.QRMainActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        initKakao()
    }

    private fun initKakao() {
        KakaoSdk.init(this, "72806b8322ff87c0ab3ec377e4f99e6a")

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e("####", "Login Required")
                    } else {
                        Log.e("####", "Error")
                    }
                } else {
                    startActivity(
                        Intent(this, QRMainActivity::class.java)
                    )
                }
            }
        } else {
            Log.e("####", "Error")
        }
    }
}
