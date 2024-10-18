plugins {
    alias(libs.plugins.app.modularization.library)
}

android {
    namespace = "me.ztiany.wan.###template###"
    resourcePrefix = "###template###_"
}

dependencies {
    implementation(project(":feature:###template###:api"))
}