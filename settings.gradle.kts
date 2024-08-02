import groovy.json.JsonSlurper

pluginManagement {
    // build logic
    includeBuild("plugins")

    repositories {
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        maven { url = uri("https://jitpack.io") }
        google()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply("plugin" to DependencySubstitutionPlugin::class.java)

class DependencySubstitutionPlugin : Plugin<Settings> {

    /**
     * Ultimate switch for the plugin.
     */
    private val enableSubstitution: Boolean = true

    /**
     * Force using local substitution.
     */
    private val forceSubstitution: Boolean = true

    class Module(map: Map<String, Any>) {
        val useLocal: Boolean by map
        val moduleName: String by map
        val localPath: String by map
        val remotePath: String by map
    }

    override fun apply(settings: Settings) {
        if (!enableSubstitution) {
            println("====================================")
            println("DependencySubstitutionPlugin is disabled.")
            return
        }
        println("====================================")
        println("DependencySubstitutionPlugin is applied.")
        settings.gradle.settingsEvaluated {
            val projectFile = File(settings.rootDir, "settings.project.json")
            if (!projectFile.exists()) {
                return@settingsEvaluated
            }
            val projects = parseProjectFile(projectFile)
            configDependency(settings, projects)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseProjectFile(projectFile: File): List<Module> {
        println("====================================")
        println("parse modules: ")
        return (JsonSlurper().parse(projectFile) as List<Map<String, Any>>).map {
            println("parse module: $it")
            Module(it)
        }
    }

    private fun configDependency(settings: Settings, projectItems: List<Module>) {
        projectItems.forEach {
            if (it.useLocal || forceSubstitution) {
                settings.include(it.localPath)
            }
        }
        settings.gradle.projectsEvaluated {
            gradle.allprojects {
                println("config dependency for project: $name")
                // TODO: conditional substitution.
                // check <https://docs.gradle.org/current/userguide/resolution_rules.html> for more details.
                configurations.all {
                    resolutionStrategy.dependencySubstitution {
                        substituteProject(projectItems)
                    }
                }
            }
        }
    }

    private fun DependencySubstitutions.substituteProject(projectItems: List<Module>) {
        projectItems.forEach { moduleItem ->
            if (moduleItem.useLocal || forceSubstitution) {
                val remote = moduleItem.remotePath
                val local = moduleItem.localPath
                // println("substitute remote [$remote] with local [$local] ")
                substitute(module(remote)).using(project(local))
            }
        }
    }

}

// 性能监控部分
include(":apm:core")

// 功能演示模块（不参入 app 打包）
include(":feature:sample:main")
include(":feature:sample:app")

// 业务基础架构
include(":common:ui-theme")
include(":common:ui-dialog")
include(":common:api")
include(":common:http")
include(":common:core")

// 业务基础组件
include(":component:selector") // 选择器（多媒体以及文件选择）
include(":component:uitask") // ui 任务管理器（用于管理全局 ui 任务）

// 主业务模块
include(":feature:home:main")
include(":feature:home:api")
include(":feature:home:app")

// 账户业务模块
include(":feature:account:main")
include(":feature:account:api")

// APP 壳
include(":app")
rootProject.name = "android-architecture-practice"