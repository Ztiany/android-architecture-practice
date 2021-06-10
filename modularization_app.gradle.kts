plugins {
    id("com.android.application")
    kotlin("android")
     id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

kapt{
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    signingConfigs {
        create("release") {
            isV1SigningEnabled =true
            isV2SigningEnabled =true
            keyAlias(AppConfig.releaseKeyAlias)
            keyPassword(AppConfig.releaseKeyPassword)
            storeFile(rootProject.file(AppConfig.releaseKeyFileName))
            storePassword(AppConfig.releaseKeyPassword)
        }
    }

    defaultConfig {
        applicationId = "me.ztiany.architecture.${project.name.toLowerCase()}"
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
       resConfigs("zh", "en")//只需要中文和英文资源
    }


    testOptions{
        unitTests.isReturnDefaultValues = true
    }

    dexOptions {
        //jumboMode = true 忽略方法数限制（这样做的缺点是apk无法在低版本的设备上安装，会出现错误：INSTALL_FAILED_DEXOPT），如果APP只兼容到19则不会有影响
        javaMaxHeapSize = "4g"
    }

    buildTypes {
        //测试
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            addManifestPlaceholders(mapOf("screenOrientation" to "unspecified"))
        }

        //正式发布
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                abiFilters("armeabi-v7a")
            }
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

    //如果不想生成某个布局的绑定类，可以在根视图添加 tools:viewBindingIgnore="true" 属性。
    buildFeatures {
        viewBinding = true
    }

    sourceSets.getByName("main") {
        manifest{
            this.srcFile("src/app/AndroidManifest.xml")
        }
        java {
            srcDir("src/app/java")
        }
        res {
            srcDir("src/app/res")
        }
    }

    applicationVariants.all(object : Action<ApplicationVariant> {
        override fun execute(variant: ApplicationVariant) {
            variant.outputs.all(object : Action<BaseVariantOutput> {
                override fun execute(t: BaseVariantOutput) {
                    val output = t as BaseVariantOutputImpl
                    output.outputFileName = "${project.name}-${versionName}.apk"
                }
            })
        }
    })

}

dependencies {
    //基础类库
    implementation(project(':module_base'))
    //测试
    testImplementation(TestLibraries.junit)
    /*Hilt*/
    api(AndroidLibraries.hiltDagger)
    kapt(AndroidLibraries.hiltDaggerApt)
    /*ARouter*/
    kapt(ThirdLibraries.arouterAnnotation)
    /* all jar*/
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}
