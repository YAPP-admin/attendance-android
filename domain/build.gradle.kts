plugins {
    kotlin("jvm")
    kotlin("kapt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(Dependencies.INJECT)
    implementation(Dependencies.Kotlin.COROUTINE_CORE)

    testImplementation(Dependencies.Test.KOTEST_RUNNER)
    testImplementation(Dependencies.Test.KOTEST_ASSERTION)
    testImplementation(Dependencies.Test.KOTEST_PROPERTY)
}
