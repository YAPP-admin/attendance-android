package com.yapp.presentation.ui.member.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jshme.common.theme.AttendanceTheme
import com.yapp.presentation.ui.member.AttendanceScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //firebase remote config test
        val firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        firebaseRemoteConfig.setDefaultsAsync(mapOf("attendance_maginotline_time" to "fail"))
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        var time = ""

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful) {
                time = firebaseRemoteConfig.getString("attendance_maginotline_time")
            }
        }

        setContent {
            AttendanceTheme {
                AttendanceScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AttendanceTheme {
        Greeting("")
    }
}
