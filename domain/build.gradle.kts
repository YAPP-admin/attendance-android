plugins {
    id(Configs.KOTLIN)
    id(Configs.KOTLIN_KAPT)
}


dependencies {
    implementation(Dependencies.INJECT)
    implementation(Dependencies.Kotlin.COROUTINE_CORE)
}
