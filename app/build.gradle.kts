plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("channel")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

rebuildChannel {
    baseReleaseApk = file("build/outputs/apk/release/app-release-${AppConfig.versionName}.apk")
    releaseOutputDir = file("apk/${AppConfig.versionName}/release/")
    //快速模式：生成渠道包时不进行校验（速度可以提升10倍以上）
    isFastMode = false
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    signingConfigs {
        create("release") {
            isV1SigningEnabled = true
            isV2SigningEnabled = true
            keyAlias = AppConfig.releaseKeyAlias
            keyPassword = AppConfig.releaseKeyPassword
            storeFile = rootProject.file(AppConfig.releaseKeyFileName)
            storePassword = AppConfig.releaseKeyPassword
        }
    }

    defaultConfig {
        applicationId = "com.android.architecture"
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode(AppConfig.versionCode)
        versionName(AppConfig.versionName)
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        resConfigs("zh", "en")//只需要中文和英文资源
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "x86"))
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    dexOptions {
        //jumboMode = true 忽略方法数限制（这样做的缺点是apk无法在低版本的设备上安装，会出现错误：INSTALL_FAILED_DEXOPT），如果APP只兼容到19则不会有影响
        javaMaxHeapSize = "8g"
    }

    buildTypes {

        //测试
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            addManifestPlaceholders(mapOf("screenOrientation" to "unspecified"))
            resValue("string", "app_name", "Android 架构模板(测试)")
        }

        //正式发布
        getByName("release") {
            isZipAlignEnabled = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            addManifestPlaceholders(mapOf("screenOrientation" to "portrait"))
            resValue("string", "app_name", "Android 架构模板")
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets.getByName("main") {
        if (!AppConfig.isMultiApp) {
            manifest {
                this.srcFile("src/app/AndroidManifest.xml")
            }
            java {
                srcDir("src/app/java")
            }
            res {
                srcDir("src/app/res")
            }
        }
    }

    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "${project.name}-${buildType.name}-${versionName}.apk"
        }
    }

    packagingOptions {
        pickFirst("lib/x86/libc++_shared.so")
        pickFirst("lib/x86_64/libc++_shared.so")
        pickFirst("lib/armeabi-v7a/libc++_shared.so")
        pickFirst("lib/arm64-v8a/libc++_shared.so")
    }
}

dependencies {
    testImplementation(TestLibraries.junit)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    if (!AppConfig.isMultiApp) {
        //modules
        implementation(project(":module_base"))
        implementation(project(":module_home"))
        implementation(project(":module_account"))
        /*Hilt*/
        api(AndroidLibraries.hiltDagger)
        kapt(AndroidLibraries.hiltDaggerApt)
        /*ARouter*/
        kapt(ThirdLibraries.arouterAnnotation)
    } else {
        /*Hilt*/
        api(AndroidLibraries.hiltDagger)
        kapt(AndroidLibraries.hiltDaggerApt)
        //app
        implementation(AndroidLibraries.appcompat)
        implementation(AndroidLibraries.material)
    }
}