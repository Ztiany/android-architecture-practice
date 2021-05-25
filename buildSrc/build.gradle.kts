import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
    maven { url = uri("http://maven.aliyun.com/nexus/content/repositories/jcenter") }
    maven { url = uri("http://maven.aliyun.com/nexus/content/repositories/google") }
    maven { url = uri("http://maven.aliyun.com/nexus/content/repositories/gradle-plugin") }
}
