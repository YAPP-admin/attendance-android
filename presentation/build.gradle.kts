plugins {
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_ANDROID)
    id(Configs.KOTLIN_PARCELIZE)
    id(Configs.KOTLIN_KAPT)
    id(Configs.HILT_ANDROID_PLUGIN)
    id(Configs.GOOGLE_SERVICE)
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

    //firebase
    implementation(platform(Dependencies.Firebase.BOM))
    implementation(Dependencies.Firebase.ANALYTICS_KTX)
    implementation(Dependencies.Firebase.CONFIG_KTX)

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

    //coil
    implementation(Dependencies.Coil.COIL)

    //test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.ANDROID_JUNIT)
    androidTestImplementation(Dependencies.Test.ESPRESSO_CORE)

    // accompanist
    implementation(Dependencies.Accompanist.FLOW_LAYOUT)
}
