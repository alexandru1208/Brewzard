@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ksp)
    id("com.deskbird.android.lib")
    id("com.deskbird.android.di")
}

android {
    namespace = "com.deskbird.datasource.remote"
}

dependencies {
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(project(":domain:api"))
}