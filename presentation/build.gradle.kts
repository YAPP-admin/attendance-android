plugins {
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_ANDROID)
    id(Configs.KOTLIN_PARCELIZE)
    id(Configs.KOTLIN_KAPT)
    id(Configs.KOTLINX_SERIALIZATION)
    id(Configs.HILT_ANDROID_PLUGIN)
    id(Configs.GOOGLE_SERVICE)
    id(Configs.CRASHLYTICS)
}

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$rootDir/report/compose-metrics",
        )
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$rootDir/report/compose-reports",
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Configs.COMPOSE_VERSION
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(Dependencies.CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.Ktx.LIFECYCLE)
    implementation(Dependencies.Google.MATERIAL)
    implementation(Dependencies.Google.GSON)
    implementation(Dependencies.Google.SYSTEM_UI_CONTROLLER)
    implementation(Dependencies.Google.SYSTEM_WINDOW_INSET)

    implementation(Dependencies.Kakao.USER_LOGIN)
    implementation(Dependencies.Lottie.LOTTIE)

    //firebase
    implementation(platform(Dependencies.Firebase.BOM))
    implementation(Dependencies.Firebase.ANALYTICS_KTX)
    implementation(Dependencies.Firebase.CONFIG_KTX)
    implementation(Dependencies.Firebase.CRASHLYTICS)

    //compose
    implementation(Dependencies.Compose.UI)
    implementation(Dependencies.Compose.MATERIAL)
    implementation(Dependencies.Compose.TOOL_PREVIEW)
    implementation(Dependencies.Compose.ACTIVITY)
    implementation(Dependencies.Compose.CONSTRAINT_LAYOUT)
    implementation(Dependencies.Compose.NAVIGATION)
    androidTestImplementation(Dependencies.Compose.JUNIT)
    debugImplementation(Dependencies.Compose.TOOL_UI)

    //hilt
    implementation(Dependencies.Dagger.HILT_ANDROID)
    implementation(Dependencies.Dagger.HILT_LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.Dagger.HILT_NAVIGATION_COMPOSE)
    kapt(Dependencies.Dagger.HILT_COMPILER)

    //coroutine
    implementation(Dependencies.Kotlin.COROUTINE_CORE)
    implementation(Dependencies.Kotlin.COROUTINE_ANDROID)
    implementation(Dependencies.Kotlin.SERIALIZATION)
    implementation(Dependencies.Kotlin.IMMUTABLE_COLLECTIONS)

    //coil
    implementation(Dependencies.Coil.COIL)

    // CameraX
    implementation(Dependencies.CameraX.CORE)
    implementation(Dependencies.CameraX.CAMERA2)
    implementation(Dependencies.CameraX.LIFECYCLE)
    implementation(Dependencies.CameraX.VIDEO)
    implementation(Dependencies.CameraX.VIEW)
    implementation(Dependencies.CameraX.EXTENSION)

    //ML Kit
    implementation(Dependencies.MlKit.ML_KIT)

    //test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.ANDROID_JUNIT)
    androidTestImplementation(Dependencies.Test.ESPRESSO_CORE)

}