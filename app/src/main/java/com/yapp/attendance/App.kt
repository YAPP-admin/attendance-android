package com.yapp.attendance

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.yapp.presentation.ui.MainActivity
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

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e("####", "Login Required")
                    } else {
                        Log.e("####", "Error")
                    }
                } else {
                    // Main 이 스플래시일 때 적용하기
//                    startActivity(
//                        Intent(this, MainActivity::class.java).apply {
//                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        }
//                    )
                }
            }
        } else {
            Log.e("####", "Error")
        }
    }
}
