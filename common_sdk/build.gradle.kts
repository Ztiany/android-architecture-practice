plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    compileSdkVersion(31)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode(AppConfig.versionCode)
        versionName(AppConfig.versionName)
        vectorDrawables.useSupportLibrary = true
        resConfigs(listOf("en", "cn"))

        buildConfigField("boolean", "openDebug", "${AppConfig.isOpenDebug}")
        buildConfigField("boolean", "showDebugTools", "${AppConfig.showDebugTools}")
        buildConfigField(
            "boolean",
            "trustHttpsCertification",
            "${AppConfig.trustHttpsCertification}"
        )
        buildConfigField("boolean", "openLog", "${AppConfig.isOpenLog}")
        buildConfigField("String", "specifiedHost", "\"${AppConfig.HOST_ENV}\"")
    }

    testOptions {
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
            srcDir("src/common/java")
            srcDir("src/tools-sdk/java")
            srcDir("src/tools-web/java")
            srcDir("src/tools-gallery/java")
            srcDir("src/tools-debug/java")
        }
        res {
            srcDir("src/common/res")
            srcDir("src/tools-web/res")
            srcDir("src/tools-gallery/res")
            srcDir("src/tools-debug/res")
        }
    }

}

dependencies {
    //测试
    testImplementation(TestLibraries.junit)

    //基础组件库
    api(project(":lib_base"))//基础类库
    api(project(":lib_network"))//基础网络库
    api(project(":lib_cache"))//缓存库
    api(project(":lib_common_ui"))//基础UI
    api(project(":lib_permission"))//权限
    api(project(":lib_media_selector"))//媒体选择器
    api(project(":lib_social"))//第三方登录
    api(project(":lib_upgrade"))//升级
    api(project(":lib_safekeyboard"))//安全键盘
    api(project(":lib_biometrics"))//生物识别
    api(project(":lib_qrcode"))//二维码
    api(project(":lib_image_loader"))//图片加载

    //业务API
    implementation(project(":module_main_api"))

    //Android
    api(AndroidLibraries.multidex)
    api(AndroidLibraries.exifinterface)
    api(AndroidLibraries.flexbox)
    api(AndroidLibraries.ktx)

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

    /*GlideAPT*/
    kapt(ThirdLibraries.glideApt)

    /*UI*/
    api(UILibraries.qmui)
    api(UILibraries.circleImageView)
    api(UILibraries.keyboardVisibilityEvent)

    //image compression
    implementation("id.zelory:compressor:3.0.1")

    //Channel
    implementation("com.leon.channel:helper:2.0.3")

    //Bugly
    implementation("com.tencent.bugly:crashreport:3.4.4")

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