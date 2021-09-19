object AndroidLibraries {

    private const val arch_version = "2.1.0"
    private const val appcompat_version = "1.3.1"
    private const val activity_version = "1.3.1"
    private const val fragment_version = "1.3.6"
    private const val lifecycle_version = "2.4.0-beta01"

    /*appcompat*/
    const val appcompat = "androidx.appcompat:appcompat:$appcompat_version"

    /*activity*/
    const val activityKtx = "androidx.activity:activity-ktx:$activity_version"

    /*fragment*/
    const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragment_version"

    //arch
    const val archRuntime = "androidx.arch.core:core-runtime:$arch_version"
    const val archCommon = "androidx.arch.core:core-common:$arch_version"

    //lifecycle, livedata, viewmodel
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common:$lifecycle_version"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    const val lifecycleLiveDataCore =
        "androidx.lifecycle:lifecycle-livedata-core:$lifecycle_version"
    const val lifecycleLiveKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    const val lifecycleReactiveStreams =
        "androidx.lifecycle:lifecycle-reactivestreams:$lifecycle_version"
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:$lifecycle_version"

    //https://android.github.io/android-ktx/core-ktx/index.html
    const val ktx = "androidx.core:core-ktx:1.6.0"

    /*paging*/
    const val paging = "androidx.paging:paging-common:3.0.0-beta01"
    const val pagingRuntime = "androidx.paging:paging-runtime:3.0.0-alpha13"
    const val pagingRxJava = "androidx.paging:paging-rxjava2:3.0.0-alpha05"

    /*room*/
    const val room = "androidx.room:room-common:2.3.0-beta01"
    const val roomKtx = "androidx.room:room-ktx:2.3.0-alpha02"
    const val roomGuava = "androidx.room:room-guava:2.3.0-alpha02"
    const val roomMigration = "androidx.room:room-migration:2.3.0-beta01"
    const val roomRuntime = "androidx.room:room-runtime:2.3.0-beta01"
    const val roomRxJava = "androidx.room:room-rxjava2:2.3.0-alpha02"
    const val roomApt = "androidx.room:room-compiler:2.3.0-alpha02"

    //persistence
    const val persistence = "androidx.sqlite:sqlite:2.1.0"
    const val persistenceFramework = "androidx.sqlite:sqlite-framework:2.1.0"

    //navigation
    //classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5"
    //apply plugin: 'androidx.navigation.safeargs.kotlin'
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:2.3.5"

    //tools
    const val annotations = "androidx.annotation:annotation:1.2.0"
    const val exifinterface = "androidx.exifinterface:exifinterface:1.3.3"
    const val biometric = "androidx.biometric:biometric:1.0.1"
    const val documentfile = "androidx.documentfile:documentfile:1.0.1"

    //multi dex
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val multidexInstrumentation = "androidx.multidex:multidex-instrumentation:2.0.0"

    /*ui component*/
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
    const val recyclerViewSelection = "androidx.recyclerview:recyclerview-selection:1.0.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0"
    const val percentLayout = "androidx.percentlayout:percentlayout:1.0.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val viewpager = "androidx.viewpager:viewpager:1.0.0"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val asyncLayoutInflater = "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val transition = "androidx.transition:transition:1.4.1"
    const val dynamicAnimation = "androidx.dynamicanimation:dynamicanimation:1.0.0"

    //https://github.com/google/flexbox-layout/
    const val flexbox = "com.google.android:flexbox:2.0.1"

    //https://github.com/material-components/material-components-android
    const val material = "com.google.android.material:material:1.4.0"

    // For loading and tinting drawables on older versions of the platform
    const val appcompatResources = "androidx.appcompat:appcompat-resources:$appcompat_version"

    //Hilt
    //classpath "com.google.dagger:hilt-android-gradle-plugin:2.36"
    //apply plugin: 'dagger.hilt.android.plugin'
    const val hiltDagger = "com.google.dagger:hilt-android:2.36"
    const val hiltDaggerApt = "com.google.dagger:hilt-android-compiler:2.36"

    //start-up
    //https://android-developers.googleblog.com/2020/07/decrease-startup-time-with-jetpack-app.html
    const val startup = "androidx.startup:startup-runtime:1.0.0-alpha03"

    //https://developer.android.com/jetpack/androidx/releases/concurrent
    const val concurrentFutures = "androidx.concurrent:concurrent-futures:1.1.0"
    const val concurrentFuturesKtx = "androidx.concurrent:concurrent-futures-ktx:1.1.0"
}

object KotlinLibraries {

    const val KOTLIN_VERSION = "1.5.30"
    const val COROUTINES_VERSION = "1.5.2"

    //Kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$KOTLIN_VERSION"

    //Coroutines
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${COROUTINES_VERSION}"
    const val kotlinAndroidCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${COROUTINES_VERSION}"
    const val kotlinxCoroutinesRx2 =
        "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${COROUTINES_VERSION}"

    //https://github.com/ReactiveX/RxKotlin
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"

    //https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm
    const val kotlinxMetadata = "org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.0.5"
}

object ThirdLibraries {

    private const val rxBindingVersion = "3.1.0"
    private const val autoDisposeVersion = "1.3.0"

    ///////////////////////////////////////////////////////////////////////////
    // Auto_Service
    ///////////////////////////////////////////////////////////////////////////
    const val autoService = "com.google.auto.service:auto-service:1.0"
    const val autoServiceApt = "com.google.auto.service:auto-service:1.0"

    //============================================================================
    //RxJava
    //============================================================================
    //https://github.com/ReactiveX/RxJava
    const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.19"

    //https://github.com/ReactiveX/RxAndroid
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"

    //https://github.com/JakeWharton/RxBinding
    const val rxBinding = "com.jakewharton.rxbinding3:rxbinding:$rxBindingVersion"
    const val rxBindingCore = "com.jakewharton.rxbinding3:rxbinding-core:$rxBindingVersion"
    const val rxBindingCompat = "com.jakewharton.rxbinding3:rxbinding-appcompat:$rxBindingVersion"
    const val rxBindingMaterial =
        "com.jakewharton.rxbinding3:rxbinding-material:3.0.0:$rxBindingVersion"
    const val rxBindingRecyclerView =
        "com.jakewharton.rxbinding3:rxbinding-recyclerview:$rxBindingVersion"

    //https://github.com/uber/AutoDispose
    const val autoDispose = "com.uber.autodispose:autodispose:$autoDisposeVersion"
    const val autoDisposeAndroid = "com.uber.autodispose:autodispose-android:$autoDisposeVersion"
    const val autoDisposeLifecycle =
        "com.uber.autodispose:autodispose-lifecycle:$autoDisposeVersion"
    const val autoDisposeLifecycleArchcomponents =
        "com.uber.autodispose:autodispose-android-archcomponents:$autoDisposeVersion"

    //https://github.com/uber/RxDogTag
    const val rxDogTag = "com.uber.rxdogtag:rxdogtag:0.2.0"

    //RxReplay https://github.com/JakeWharton/RxRelay
    const val rxReplay = "com.jakewharton.rxrelay2:rxrelay:2.1.1"

    //============================================================================
    //okHttp、retrofit、gson
    //============================================================================
    //https://github.com/square/okhttp
    const val okHttp = "com.squareup.okhttp3:okhttp:4.8.1"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:4.8.1"

    // https://github.com/square/retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val retrofitRxJavaCallAdapter = "com.squareup.retrofit2:adapter-rxjava:2.9.0"
    const val retrofitRxJava2CallAdapter = "com.squareup.retrofit2:adapter-rxjava2:2.9.0"

    //https://github.com/google/gson/blob/master/UserGuide.md#TOC-Overview
    const val gson = "com.google.code.gson:gson:2.8.6"

    //https://github.com/bumptech/glide
    const val glide = "com.github.bumptech.glide:glide:4.11.0"
    const val glideApt = "com.github.bumptech.glide:compiler:4.11.0"
    const val glideOkHttp = "com.github.bumptech.glide:okhttp3-integration:4.11.0"

    //https://github.com/coil-kt/coil
    const val coil = "io.coil-kt:coil:1.1.1"

    //https://github.com/alibaba/ARouter，注意：apt arouter 应该放在  apt dagger 后面
    const val arouter = "com.alibaba:arouter-api:1.5.0"
    const val arouterAnnotation = "com.alibaba:arouter-compiler:1.2.2"

    //https://github.com/tencent/mmkv, https://mp.weixin.qq.com/s/kTr1GVDCAhs5K7Hq3322FA
    const val mmkv = "com.tencent:mmkv:1.2.2"

    //https://github.com/dmstocking/support-optional
    const val supportOptional = "com.github.dmstocking:support-optional:1.2"

    //https://github.com/JakeWharton/timber
    const val timber = "com.jakewharton.timber:timber:4.7.1"

    //https://github.com/jOOQ/jOOR
    const val jOOR = "org.jooq:joor-java-6:0.9.7"

    //https://github.com/Blankj/AndroidUtilCode
    const val utilcode = "com.blankj:utilcodex:1.29.0"

    //https://github.com/yanzhenjie/AndPermission
    const val andPermission = "com.yanzhenjie:permission:2.0.3"

    //https://github.com/tiann/FreeReflection
    const val freeReflection = "me.weishu:free_reflection:2.2.0"

    //https://github.com/srikanth-lingala/zip4j
    const val zip4j = "net.lingala.zip4j:zip4j:2.6.4"

    //https://github.com/Tencent/mars
    const val xlog = "com.tencent.mars:mars-xlog:1.2.4"

    //https://github.com/drewnoakes/metadata-extractor
    const val metadataExtractor = "com.drewnoakes:metadata-extractor:2.14.0"

    //https://github.com/javakam/FileOperator
    //TODO
}

object UILibraries {
    //qmui：https://qmuiteam.com/
    const val qmui = "com.qmuiteam:qmui:2.0.0-alpha10"

    //https://github.com/drakeet/MultiType
    const val multiType = "com.drakeet.multitype:multitype:4.0.0"

    // https://github.com/Ztiany/WrapperAdapter
    const val wrapperAdapter = "com.github.Ztiany:LoadMoreAdapter:4.4.1"

    //https://github.com/hdodenhof/CircleImageView
    const val circleImageView = "de.hdodenhof:circleimageview:3.0.1"

    //https://github.com/Fotoapparat/Fotoapparat
    const val fotoapparat = "io.fotoapparat:fotoapparat:2.7.0"

    //https://github.com/yshrsmz/KeyboardVisibilityEvent
    const val keyboardVisibilityEvent =
        "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:3.0.0-RC2"

    //https://github.com/RuffianZhong/RWidgetHelper
    const val rWidget = "com.ruffian.library:RWidgetHelper-AndroidX:0.0.8"

    //https://github.com/B3nedikt/ViewPump
    const val viewpump = "dev.b3nedikt.viewpump:viewpump:4.0.7"
}

object DebugLibraries {
    //https://github.com/square/leakcanary
    const val debugLeakCanary = "com.squareup.leakcanary:leakcanary-android:2.1"

    //http://facebook.github.io/stetho
    const val stetho = "com.facebook.stetho:stetho:1.5.1"
    const val stethoOkhttp3 = "com.facebook.stetho:stetho-okhttp3:1.5.1"

    //https://github.com/eleme/UETool
    const val ueTool = "me.ele:uetool:1.2.1"

    //https://github.com/markzhai/AndroidPerformanceMonitor 卡顿监控
    //http://blog.zhaiyifan.cn/2016/01/16/BlockCanaryTransparentPerformanceMonitor/
    const val debugBlockCanary = "com.github.markzhai:blockcanary-android:1.4.0"
    const val releaseBlockCanary = "com.github.markzhai:blockcanary-no-op:1.4.0"

    //https://github.com/didi/DoraemonKit
    const val doraemonKit = "com.didichuxing.doraemonkit:dokitx:3.3.5"

    //https://github.com/didi/booster
    //TODO

    //https://github.com/square/radiography
    const val radiography = "com.squareup.radiography:radiography:2.3.0"
}

object TestLibraries {
    const val junit = "junit:junit:4.13"
    const val mockito = "org.mockito:mockito-core:1.10.19"
}