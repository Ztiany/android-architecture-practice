buildscript {

    repositories {
        //remote
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        google()
        mavenCentral()
        //local
        maven { url = uri("repo") }
    }

    dependencies {
        //android
        classpath("com.android.tools.build:gradle:4.1.3")
        //kotlin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        //hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
        //channel
        classpath("com.leon.channel:plugin:2.0.3")
    }
}

allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        google()
        mavenCentral()
        maven { url = uri("https://dl.bintray.com/umsdk/release") }

        flatDir { dirs(rootProject.projectDir.absolutePath + "/repo/aar") }

        configurations.all {
            resolutionStrategy.force(
               AndroidLibraries.activityKtx,
               AndroidLibraries.fragmentKtx,
               AndroidLibraries.annotations
            )
        }

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}