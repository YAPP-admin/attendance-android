plugins {
    id(Configs.LIBRARY)
    id(Configs.KOTLIN_ANDROID)
    id(Configs.KOTLIN_PARCELIZE)
    id(Configs.KOTLIN_KAPT)
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

    api(Dependencies.Room.ROOM)
    implementation(Dependencies.Room.KTX)
    kapt(Dependencies.Room.COMPILER)

    api(Dependencies.Square.RETROFIT)
    implementation(Dependencies.Square.RETROFIT_MOSHI)
    implementation(Dependencies.Square.OKHTTP_LOGGING)
    implementation(Dependencies.Square.SERIALIZATION_CONVERTER)
    androidTestImplementation(project(mapOf("path" to ":")))
    platform("com.google.firebase:firebase-bom:29.1.0")
    implementation("com.google.firebase:firebase-firestore:24.0.1")
    implementation ("com.google.firebase:firebase-firestore-ktx")
}