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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinLibraries.KOTLIN_VERSION}")
        //hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.35.1")
        //channel
        classpath("com.leon.channel:plugin:2.0.3")
        //DexCount requires Java 8 or higher, Gradle 6.0 or higher, and Android Gradle Plugin 3.4.0 or higher.
        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:2.0.0")
    }
}

allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://dl.bintray.com/umsdk/release") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        google()
        mavenCentral()

        flatDir { dirs(rootProject.projectDir.absolutePath + "/repo/aar") }

        /*configurations.all {
            resolutionStrategy.force(
                    //choose the  latest version.
                    //kotlin
                    //"org.jetbrains.kotlin:kotlin-stdlib:1.4.31",
                    //"org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
            )
        }*/

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}