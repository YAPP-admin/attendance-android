plugins {
    kotlin("android")
    kotlin("kapt")
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_PARCELIZE)
}

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        minSdk        = Configs.MIN_SDK
        targetSdk     = Configs.TARGET_SDK
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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Configs.KOTLIN_COMPILER_EXTENSION
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependencies.Kakao.USER_LOGIN)

    implementation(Dependencies.CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.Ktx.LIFECYCLE)
    implementation(Dependencies.Google.MATERIAL)
    implementation(Dependencies.Kotlin.COROUTINE_CORE)
    implementation(Dependencies.Lottie.LOTTIE)

    //hilt
    implementation(Dependencies.Dagger.HILT_ANDROID)
    implementation(Dependencies.Dagger.HILT_NAVIGATION_COMPOSE)
    kapt(Dependencies.Dagger.HILT_COMPILER)

    //compose
    implementation(Dependencies.Compose.UI)
    implementation(Dependencies.Compose.LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.Compose.MATERIAL)
    implementation(Dependencies.Compose.TOOL_PREVIEW)
    implementation(Dependencies.Compose.ACTIVITY)
    implementation(Dependencies.Compose.CONSTRAINT_LAYOUT)
    implementation(Dependencies.Compose.NAVIGATION)
    androidTestImplementation(Dependencies.Compose.JUNIT)
    debugImplementation(Dependencies.Compose.TOOL_UI)

    //test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.ANDROID_JUNIT)
    androidTestImplementation(Dependencies.Test.ESPRESSO_CORE)
}
