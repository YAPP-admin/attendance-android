plugins {
    kotlin("android")
    kotlin("kapt")
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_PARCELIZE)
    id(Configs.KOTLINX_SERIALIZATION)
    id(Configs.HILT_ANDROID_PLUGIN)
}

android {
    compileSdk = Configs.COMPILE_SDK
}

dependencies {
    implementation(project(":domain"))
    implementation(Dependencies.Dagger.HILT_ANDROID)
    kapt(Dependencies.Dagger.HILT_COMPILER)

    implementation(Dependencies.Kotlin.COROUTINE_CORE)
    implementation(Dependencies.Kotlin.SERIALIZATION)

    implementation(Dependencies.DATA_STORE)

    //firebase
    implementation(Dependencies.Firebase.COMMON)
    implementation(platform(Dependencies.Firebase.BOM))
    implementation(Dependencies.Firebase.ANALYTICS_KTX)
    implementation(Dependencies.Firebase.CONFIG_KTX)
    implementation(Dependencies.Firebase.FIRESTORE)
    implementation(Dependencies.Firebase.FIRESTORE_KTX)
    implementation(Dependencies.Kotlin.COROUTINE_PLAY_SERVICE)

    api(Dependencies.Square.RETROFIT)
    implementation(Dependencies.Square.RETROFIT_MOSHI)
    implementation(Dependencies.Square.OKHTTP_LOGGING)
    implementation(Dependencies.Square.SERIALIZATION_CONVERTER)

    testImplementation(Dependencies.Test.JUNIT)
}