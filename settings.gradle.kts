pluginManagement {
    // build logic
    includeBuild("plugins")

    repositories {
        maven { url= uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url= uri("https://maven.aliyun.com/repository/public") }
        maven { url= uri("https://maven.aliyun.com/repository/central") }
        maven { url= uri("https://maven.aliyun.com/repository/apache-snapshots") }
        maven { url= uri("https://jitpack.io") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url= uri("https://maven.aliyun.com/repository/public") }
        maven { url= uri("https://maven.aliyun.com/repository/central") }
        maven { url= uri("https://maven.aliyun.com/repository/apache-snapshots") }
        maven { url= uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// 基础模块
include("base:core")
include("base:activity")
include("base:adapter")
include("base:fragment")
include("base:mvi")
include("base:utils")
include(":base:view")
include(":base:viewbinding")

// 性能监控部分
include(":apm:core")

// 业务基础架构
include(":common:api")
include(":common:ui")
include(":common:core")


// 主业务模块
include(":feature:main:main")
include(":feature:main:api")
include(":feature:main:app")

// 账户业务模块
include(":feature:account:main")
include(":feature:account:api")

// APP 壳
include(":app")
