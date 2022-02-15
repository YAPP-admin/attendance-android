plugins {
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_ANDROID)
    id(Configs.KOTLIN_PARCELIZE)
    id(Configs.KOTLIN_KAPT)
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
    implementation(Dependencies.CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.Ktx.LIFECYCLE)
    implementation(Dependencies.Google.MATERIAL)

    //compose
    implementation(Dependencies.Compose.UI)
    implementation(Dependencies.Compose.MATERIAL)
    implementation(Dependencies.Compose.TOOL_PREVIEW)
    implementation(Dependencies.Compose.ACTIVITY)
    implementation(Dependencies.Compose.CONSTRAINT_LAYOUT)
    implementation(Dependencies.Compose.NAVIGATION)
    androidTestImplementation(Dependencies.Compose.JUNIT)
    debugImplementation(Dependencies.Compose.TOOL_UI)
}
