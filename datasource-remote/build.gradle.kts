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
    implementation(libs.bundles.retrofit)
    ksp(libs.moshi.codegen)
}