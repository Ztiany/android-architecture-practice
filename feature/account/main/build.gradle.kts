plugins {
    alias(libs.plugins.app.modularization.library)
}

android {
    namespace = "me.ztiany.wan.account"
    resourcePrefix = "account_"
}

dependencies {
    implementation(project(":feature:home:api"))
    implementation(project(":feature:account:api"))
}