plugins {
    id(Configs.APPLICATION)
    id(Configs.KOTLIN_ANDROID)
    id(Configs.KOTLIN_KAPT)
    id(Configs.HILT_ANDROID_PLUGIN)
    id(Configs.GOOGLE_SERVICE)
}

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        applicationId = Configs.APPLICATION_ID
        minSdk        = Configs.MIN_SDK
        targetSdk     = Configs.TARGET_SDK
        versionCode   = Configs.VERSION_CODE
        versionName   = Configs.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("com.kakao.sdk:v2-user:2.8.6")

    implementation(Dependencies.CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.Ktx.LIFECYCLE)

    implementation(Dependencies.Google.MATERIAL)
    implementation(Dependencies.Dagger.HILT_ANDROID)
    implementation(Dependencies.Dagger.HILT_LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.Dagger.HILT_NAVIGATION_COMPOSE)
    kapt(Dependencies.Dagger.HILT_COMPILER)

    implementation(Dependencies.Kotlin.COROUTINE_CORE)
    implementation(Dependencies.Kotlin.COROUTINE_ANDROID)

    //firebase
    implementation(platform(Dependencies.Firebase.BOM))
    implementation(Dependencies.Firebase.ANALYTICS_KTX)
    implementation(Dependencies.Firebase.CONFIG_KTX)
}
