plugins {
    id("com.android.library")
    kotlin("android")
     id("kotlin-parcelize")
    kotlin("kapt")
    //id("dagger.hilt.android.plugin")
}

kapt{
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables.useSupportLibrary = true
        resConfigs(listOf("en","cn"))

        buildConfigField("boolean", "openDebug", "${AppConfig.isOpenDebug}")
        buildConfigField("boolean", "showDebugTools", "${AppConfig.showDebugTools}")
        buildConfigField("boolean", "trustHttpsCertification", "${AppConfig.trustHttpsCertification}")
        buildConfigField("boolean", "openLog", "${AppConfig.isOpenLog}")
        buildConfigField("String", "specifiedHost", "\"${AppConfig.HOST_ENV}\"")
    }

    testOptions{
        unitTests.isReturnDefaultValues = true
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
        java {
            srcDir("src/github/java")
            srcDir("src/common/java")
            if (AppConfig.isOpenDebug) {
                srcDir("src/tools-debug/java")
            } else {
                srcDir("src/tools-release/java")
            }
        }
        res {
            srcDir("src/github/res")
            srcDir("src/common/res")
            if (AppConfig.isOpenDebug) {
                srcDir("src/tools-debug/res")
            } else {
                srcDir("src/tools-release/res")
            }
        }
    }

}

dependencies {
    //测试
    testImplementation(TestLibraries.junit)

    //基础组件库
    api(project(":lib_base"))//基础类库
    api(project(":lib_common_ui"))//基础UI
    api(project(":lib_network"))//基础网络库
    api(project(":lib_permission"))//权限
    api(project(":lib_cache"))//缓存库
    api(project(":lib_media_selector"))//媒体选择器
    api(project(":lib_social"))//第三方登录
    api(project(":lib_upgrade"))//升级
    api(project(":lib_safekeyboard"))//安全键盘
    api(project(":lib_biometrics"))//生物识别
    api(project(":lib_webview"))//web view
    api(project(":lib_qrcode"))//二维码

    //Android
    api(AndroidLibraries.multidex)
    api(AndroidLibraries.exifinterface)
    api(AndroidLibraries.flexbox)
    api(AndroidLibraries.ktx)
    api(AndroidLibraries.room)
    api(AndroidLibraries.roomKtx)

    /*Hilt*/
    api(AndroidLibraries.hiltDagger)
    kapt(AndroidLibraries.hiltDaggerApt)

    /*ARouter*/
    api(ThirdLibraries.arouter)
    kapt(ThirdLibraries.arouterAnnotation)

    /*OkHttp*/
    implementation(ThirdLibraries.okHttp)
    implementation(ThirdLibraries.okHttpLogging)

    /*Gson*/
    api(ThirdLibraries.gson)

    /*Retrofit*/
    api(ThirdLibraries.retrofit)
    implementation(ThirdLibraries.retrofitConverterGson)
    implementation(ThirdLibraries.retrofitRxJava2CallAdapter)
    /*RxKotlin, RxBinding*/
    api(ThirdLibraries.rxBindingCompat)
    api(KotlinLibraries.rxKotlin)

    /*GlideAPT*/
    kapt(ThirdLibraries.glideApt)

    /*UI*/
    api(UILibraries.qmui)
    api(UILibraries.circleImageView)
    api(UILibraries.keyboardVisibilityEvent)

    //image compression
    implementation("id.zelory:compressor:3.0.0")

    //Channel
    implementation("com.leon.channel:helper:2.0.3")

    //Bugly
    implementation("com.tencent.bugly:crashreport:3.2.422")

    //Debug
    if (AppConfig.isOpenDebug) {
        //Stetho
        implementation(DebugLibraries.stetho)
        implementation(DebugLibraries.stethoOkhttp3)
        //LeakCanary
        implementation(DebugLibraries.debugLeakCanary)
        //UETool
        implementation(DebugLibraries.ueTool)
    }

}