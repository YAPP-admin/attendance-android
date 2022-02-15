buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath(Dependencies.Kotlin.GRADLE_PLUGIN)
        classpath(Dependencies.Kotlin.SERIALIZATION_PLUGIN)
        classpath(Dependencies.Dagger.HILT_GRADLE_PLUGIN)
        classpath(Dependencies.GOOGLE_SERVICES)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
