object Dependencies {
    const val CORE = "androidx.core:core-ktx:1.7.0"
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.4.1"
    const val INJECT = "javax.inject:javax.inject:1"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:4.3.10"
    const val DATA_STORE = "androidx.datastore:datastore-preferences:1.0.0"

    object Kotlin {
        private const val version = "1.6.10"
        private const val coroutineVersion = "1.5.1"
        private const val ImmutableCollections = "0.3.5"

        const val GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
        const val SERIALIZATION_PLUGIN = "org.jetbrains.kotlin:kotlin-serialization:$version"

        const val COROUTINE_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
        const val COROUTINE_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val COROUTINE_PLAY_SERVICE = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.2.1"

        const val IMMUTABLE_COLLECTIONS = "org.jetbrains.kotlinx:kotlinx-collections-immutable:${ImmutableCollections}"
    }

    object Compose {
        private const val compose_version = "1.1.0"
        private const val lifecycle_viewmodel = "2.5.0"
        const val UI = "androidx.compose.ui:ui:$compose_version"
        const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_viewmodel"
        const val MATERIAL = "androidx.compose.material:material:$compose_version"
        const val TOOL_PREVIEW = "androidx.compose.ui:ui-tooling:$compose_version"
        const val JUNIT = "androidx.compose.ui:ui-test-junit4:$compose_version"
        const val TOOL_UI = "androidx.compose.ui:ui-tooling:$compose_version"
        const val ACTIVITY = "androidx.activity:activity-compose:1.4.0"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
        const val NAVIGATION = "androidx.navigation:navigation-compose:2.5.0-alpha01"
    }

    object Dagger {
        private const val dagger_version = "2.40.1"
        const val HILT_ANDROID = "com.google.dagger:hilt-android:$dagger_version"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:$dagger_version"
        const val HILT_GRADLE_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:$dagger_version"
        const val HILT_LIFECYCLE_VIEWMODEL = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val HILT_NAVIGATION_COMPOSE = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    }

    object Room {
        private const val room_version = "2.4.0"
        const val ROOM = "androidx.room:room-runtime:$room_version"
        const val KTX = "androidx.room:room-ktx:$room_version"
        const val COMPILER = "androidx.room:room-compiler:$room_version"
    }

    object Ktx {
        const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    }

    object Google {
        const val MATERIAL = "com.google.android.material:material:1.5.0"
        const val GSON = "com.google.code.gson:gson:2.8.5"
        const val SYSTEM_UI_CONTROLLER =
            "com.google.accompanist:accompanist-systemuicontroller:0.17.0"
        const val SYSTEM_WINDOW_INSET = "com.google.accompanist:accompanist-insets:0.14.0"
    }

    object Square {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
        const val RETROFIT_MOSHI = "com.squareup.retrofit2:converter-moshi:2.9.0"
        const val OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:4.9.1"

        const val SERIALIZATION_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Coil {
        const val COIL = "io.coil-kt:coil-compose:1.4.0"
    }

    object Test {
        const val JUNIT = "junit:junit:4.+"
        const val ANDROID_JUNIT = "androidx.test.ext:junit:1.1.3"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object Firebase {
        const val CRASHLYTICS_GRADLE = "com.google.firebase:firebase-crashlytics-gradle:2.8.1"
        const val COMMON = "com.google.firebase:firebase-common-ktx:20.0.0"
        const val BOM = "com.google.firebase:firebase-bom:29.1.0"
        const val CONFIG_KTX = "com.google.firebase:firebase-config-ktx"
        const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics"
        const val ANALYTICS_KTX = "com.google.firebase:firebase-analytics-ktx"
        const val FIRESTORE = "com.google.firebase:firebase-firestore:24.0.1"
        const val FIRESTORE_KTX = "com.google.firebase:firebase-firestore-ktx"
    }

    object Kakao {
        const val USER_LOGIN = "com.kakao.sdk:v2-user:2.8.6"
    }

    object Lottie {
        const val LOTTIE = "com.airbnb.android:lottie-compose:5.0.1"
    }

    object CameraX {
        private const val camerax_version = "1.0.2"
        const val CORE = "androidx.camera:camera-core:${camerax_version}"
        const val CAMERA2 = "androidx.camera:camera-camera2:${camerax_version}"
        const val LIFECYCLE = "androidx.camera:camera-lifecycle:${camerax_version}"
        const val VIDEO = "androidx.camera:camera-video:1.1.0-beta02"
        const val VIEW = "androidx.camera:camera-view:1.1.0-beta02"
        const val EXTENSION = "androidx.camera:camera-extensions:1.1.0-beta02"
    }

    object MlKit {
        const val ML_KIT = "com.google.mlkit:barcode-scanning:17.0.2"
    }
}

