plugins {
    id("com.android.library")
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

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables.useSupportLibrary = true
        resConfigs(listOf("en","cn"))
    }

    testOptions{
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        //正式发布
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
            this.srcFile("src/library/AndroidManifest.xml")
        }
        java {
            srcDir("src/library/java")
        }
        res {
            srcDir("src/library/res")
        }
    }

}

dependencies {
    /* all jar*/
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //基础类库
    implementation(project(":module_base"))
    //测试
    testImplementation(TestLibraries.junit)
    /*Hilt*/
    api(AndroidLibraries.hiltDagger)
    kapt(AndroidLibraries.hiltDaggerApt)
    /*ARouter*/
    kapt(ThirdLibraries.arouterAnnotation)
}
